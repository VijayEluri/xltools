package classifier.nb;


import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;

import java.io.FileWriter;
import java.io.PrintWriter;

import test.Debug;
import util.Serializer;
import util.V;
import abs.ClassData;
import abs.Data;
import abs.FeatureFactory;
import abs.Instance;
import abs.LabelFactory;
import abs.Performance;
import classifier.Model;
/**
 * Naive Bayes
 * TODO: 1. semi-supervised with unlabeled data
 * 2. feature selection
 * @author xiaoling
 *
 */
public class NaiveBayes extends Model {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6130871745051166597L;

	/**
	 * reads file in svm_light format
	 */
	public NaiveBayes(){
		super();
//		para = new NBParameter();
		labelFactory = new LabelFactory();
		featureFactory = new FeatureFactory();
	}

	NBParameter nbpara = null;

	public void clear(){
		para.clear();
	}
	@Override
	public void learn(Data data){

		// init parameter
		para = new NBParameter(labelFactory.all_labels.size(), featureFactory.all_features.size());
		nbpara = (NBParameter)para;

		//**********estimate*********		

		ClassData cData = (ClassData)data;
		for (Instance inst: cData.instances){
			//				Label  l =labelFactory.getState("C"+Integer.parseInt(items[0].replace("+","")));
			int cl = labelFactory.index(inst.label);
			nbpara.prior[cl]+=1;
			for (int i = 0; i < inst.featureIndex.size(); i++){
				//					String[] pair = items[i].split(":");
				//					int p1 = Integer.parseInt(pair[0]);
				//					int p2 = 1;
				//					try{
				//						p2 = Integer.parseInt(pair[1]);
				//					}catch(Exception e){
				//nothing
				//					}
				int v = ((int)inst.featureValue.get(i))+1;
				nbpara.occurrence[cl][inst.featureIndex.get(i)] += v;
				nbpara.all[cl] += v;
			}
		}
		// ****laplace smoothing****
		int numInst = 0;
		for (int cl = 0; cl < nbpara.classNum ; cl++){
			for (int i = 0; i < nbpara.dim; i++){
				nbpara.para[cl][i] = Math.log((double)(nbpara.occurrence[cl][i]+1)) - Math.log(nbpara.dim + nbpara.all[cl]);
			}
			numInst += nbpara.prior[cl];
		}
		// prior
		for (int cl = 0; cl < nbpara.classNum ; cl++){
			nbpara.prior[cl] = Math.log(nbpara.prior[cl] / numInst);
		}
	}

	@Override
	public void predict(Data data, Performance perf){
		predict(data, "nb_predictions", perf);
	}

	@Override
	public void predict(Data data, String predFile, Performance perf){
		if (nbpara==null){
			Debug.errl("parameter missing");
		}
		try{
			ClassData cData = (ClassData)data;
			PrintWriter pw = new PrintWriter(new FileWriter(predFile));;
			//			int posNum = 0, posCorrect = 0, posPred = 0;
			//			String[] lines = FileUtil.getTextFromFile(filename).split("\n");
					
			for (Instance inst: cData.instances){
				double max = -2e10;
				int maxCl = 0;
				double[] score = new double[nbpara.classNum];
				int c = labelFactory.index(inst.label);
				for (int cl = 0; cl < nbpara.classNum ;cl++){
					TDoubleList realValues = new TDoubleArrayList();
					for (int i = 0; i < inst.featureIndex.size(); i++)
						realValues.add(((int)inst.featureValue.get(i))+1.0);
					score[cl] = V.sumprod(inst.featureIndex, realValues, nbpara.para, cl);
					score[cl] += nbpara.prior[cl];
					pw.print(score[cl]+"\t");
					if (score[cl] > max)
					{
						max = score[cl];
						maxCl = cl;
					}
				}
				perf.update(maxCl, c);
				
				pw.println(maxCl);
			}
			
			pw.close();
			perf.print();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void readModel(String file) {
		NaiveBayes model = (NaiveBayes)Serializer.deserialize(file);
		labelFactory = model.labelFactory;
		para = model.para;
		nbpara = model.nbpara;
		featureFactory = model.featureFactory;
	}

	@Override
	public void writeModel(String file) {
		Serializer.serialize(this, file);
	}
}
