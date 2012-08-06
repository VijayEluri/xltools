package data.reader;

import java.io.File;
import java.util.HashSet;

import test.Debug;
import util.QuickSort;
import util.io.FileUtil;
import abs.ClassData;
import abs.Feature;
import abs.Instance;
import abs.Label;
import abs.MultiLabelInstance;
import classifier.Model;

public class SVMReader {
	public static ClassData read(String file, Model m, boolean isTrain){
		m.featureFactory.isTrain = isTrain;
		m.labelFactory.isTrain = isTrain;
		if (!new File(file).exists())
			Debug.errl(" file can't be found:"+file);
		ClassData data = new ClassData();
		String[] lines = FileUtil.getTextFromFile(file).split("\n");
		//		trainFile = filename;
		//*******scan*********
		HashSet<Integer> classSet = new HashSet<Integer>();
		int maxDim = 0;
		for (String line: lines){
			if (line.charAt(0) == '#'){
				continue;
			}
			else if (line.charAt(0) == '0'){
				// unlabeled data
				continue;
			}
			else{
				Instance inst = new Instance();
				String[] items = line.split(" ");
				if (m.labelFactory.numLabels==0)
					inst.setLabel(m.labelFactory.getState("C"+Integer.parseInt(items[0].replace("+",""))));
				else{
					if (m.labelFactory.numLabels == 2)
						inst.setLabel(m.labelFactory.all_labels.get((Integer.parseInt(items[0].replace("+",""))+1)/2));
					else
						inst.setLabel(m.labelFactory.all_labels.get(Integer.parseInt(items[0].replace("+",""))-1));
				}
				for (int i = 1; i < items.length; i++){
					String[] pair = items[i].split(":");
					int p1 = Integer.parseInt(pair[0]);
					double p2 = 1;
					try{
						p2 = Double.parseDouble(pair[1]);
					}catch(Exception e){
						//nothing
					}
					if (m.featureFactory.all_features.size()==0){
						Feature f = m.featureFactory.getFeature("F"+p1);
						if (f!=null){
							inst.featureIndex.add(f.id);
							inst.featureValue.add(p2);
						}
					}else{
						if (p1-1 < m.featureFactory.all_features.size()){
							Feature f = m.featureFactory.all_features.get(p1-1);
							inst.featureIndex.add(f.id);
							inst.featureValue.add(p2);
						}
					}
					if (p1>maxDim)
						maxDim = p1;
				}
				data.instances.add(inst);
			}
		}
		return data;
	}

	public static void write(ClassData data,String file, Model m){
		StringBuilder sb = new StringBuilder();
		boolean binaryLabel = true;
		if (m.labelFactory.all_labels.size() != 2){
			binaryLabel = false;
		}
		for (int i =0; i < data.instances.size(); i++){
			Instance inst = data.instances.get(i);
			try{
				if (inst instanceof MultiLabelInstance){
					if (inst.label==null){
						boolean first = true;
						for (Label l:((MultiLabelInstance)inst).labels){
							if (first){
								first = false;
								sb.append(m.labelFactory.index(l));
							}
							else{
								sb.append(","+m.labelFactory.index(l));
							}
						}
					}
					else{
						int l = -1;
						if (binaryLabel){
							l = m.labelFactory.index(inst.label)*2 -1;
						}
						else{
							l = m.labelFactory.index(inst.label) + 1;
						}
						sb.append(l);
					}
				}else{
					int l = -1;
					if (binaryLabel){
						l = m.labelFactory.index(inst.label)*2 -1;
					}
					else{
						l = m.labelFactory.index(inst.label) + 1;
					}
					sb.append(l);
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			double [] fi = new double[inst.featureIndex.size()];
			for (int j = 0; j < inst.featureIndex.size(); j++){
				fi[j] = inst.featureIndex.get(j);
			}
			int [] idx = QuickSort.quicksort(fi);
			for (int j = 0; j < idx.length; j++){
				sb.append(" "+(inst.featureIndex.get(idx[j])+1)+":"+inst.featureValue.get(idx[j]));
			}
			sb.append("\n");
		}
		FileUtil.writeTextToFile(sb.toString(), file);
	}
}
