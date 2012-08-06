package classifier.logreg;

import java.io.FileWriter;
import java.io.PrintWriter;

import util.Serializer;
import util.V;

import abs.ClassData;
import abs.Data;
import abs.Instance;
import abs.Performance;
import classifier.Inference;
import classifier.Learner;
import classifier.Model;

public class LogisticRegression extends Model {
	public boolean voted = false;
//	public Inference infer = null;
//	public Learner learner = null;
	public LogisticRegression(Learner l){
		super(l, new LRInference());
//		this.infer = ;
//		learner = l;
	}
	public LogisticRegression(){
		super(new LRPerceptronLearner(), new LRInference());
//		infer.m = this;
//		((LRInference)infer).lr = this;
	}
//	public static String targetState = "I";
//	public static int targetIndex = -1;

	@Override	
	public void learn(Data data) {
		learner.learn(data, this);
	}
	
	@Override
	public void writeModel(String file) {
		if (debug){
		PrintWriter writer = null; 
		try{
			writer = new PrintWriter(new FileWriter(file+".debug"));
			int k = 0; 
			for (int i = 0; i < labelFactory.all_labels.size(); i++){
				for (int j = 0; j < featureFactory.all_features.size(); j++){
					writer.println(labelFactory.all_labels.get(i)+":"+featureFactory.all_features.get(j)+"\t"+( (LRParameter)para).lambda[k]);
					k++;
				}
			}
			writer.close();
		}catch(Exception e)
		{
			if (writer !=null)
				writer.close();
		}
		}
		Serializer.serialize(this, file);
	}
	@Override
	public void readModel(String file) {
		LogisticRegression lr = (LogisticRegression)Serializer.deserialize(file);
		this.featureFactory = lr.featureFactory;
		this.labelFactory = lr.labelFactory;
		this.infer = lr.infer;
		this.learner = lr.learner;
		this.para = lr.para;
	}
	@Override
	public void predict(Data data, Performance perf) {
		predict(data, "lr_predictions", perf);
	}
	@Override
	public void predict(Data data, String predFile, Performance perf) {
		try{
			ClassData cData = (ClassData)data;
			PrintWriter pw = new PrintWriter(new FileWriter(predFile));;
			//			int posNum = 0, posCorrect = 0, posPred = 0;
			//			String[] lines = FileUtil.getTextFromFile(filename).split("\n");
					
			for (Instance inst: cData.instances){
				double max = -2e10;
				int c = labelFactory.index(inst.label);
				int pred =  labelFactory.index(infer.findBestLabel(inst, this).label);
				perf.update(pred, c);
			}
			
			pw.close();
			perf.print();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}

}
