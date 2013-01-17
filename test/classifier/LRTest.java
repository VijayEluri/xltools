package classifier;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import junit.framework.TestCase;
import test.BinaryTester;
import util.io.Debug;
import test.Tester;
import util.io.FileUtil;
import abs.ClassData;
import abs.Instance;
import abs.Label;
import abs.Performance;
import classifier.logreg.LRGradientAscentLearner;
import classifier.logreg.LRLBFGSLearner;
import classifier.logreg.LRParameter;
import classifier.logreg.LogisticRegression;
import classifier.nb.NaiveBayes;
import data.reader.SVMReader;

public class LRTest extends TestCase{

	public void TestHw3Heart(){
		LogisticRegression m = new LogisticRegression(new LRGradientAscentLearner());
		String path = "/scratch/ps3/heart/";
		/*String[] lines = FileUtil.getTextFromFile(path+"all.data").split("\n");
		m.labelFactory.getState("0");
		 m.labelFactory.getState("1");
		 HashMap<String, Integer> map = new HashMap<String, Integer>();
		 map.put("0", 1);
		 map.put("1", 2);
		 ArrayList<Instance> train = new ArrayList<Instance>();
			ArrayList<Instance> valid = new ArrayList<Instance>();
			ArrayList<Instance> test = new ArrayList<Instance>();
			
		for (int i = 0; i < 187; i++){
			String[] items = lines[i].split(",");
			Instance inst = new Instance();
			inst.label = m.labelFactory.getState(items[0]);
			for (int j = 1; j< 23; j++){
				inst.featureIndex.add(j-1);
				inst.featureValue.add(map.get(items[j]));
			}
			train.add(inst);
		}
		for (int i = 187; i < 214; i++){
			String[] items = lines[i].split(",");
			Instance inst = new Instance();
			inst.label = m.labelFactory.getState(items[0]);
			for (int j = 1; j< 23; j++){
				inst.featureIndex.add(j-1);
				inst.featureValue.add(map.get(items[j]));
			}
			valid.add(inst);
		}
		for (int i = 214; i < 267; i++){
			String[] items = lines[i].split(",");
			Instance inst = new Instance();
			inst.label = m.labelFactory.getState(items[0]);
			for (int j = 1; j< 23; j++){
				inst.featureIndex.add(j-1);
				inst.featureValue.add(map.get(items[j]));
			}
			test.add(inst);
		}
		ClassData data = new ClassData();
		data.instances = train;
		SVMReader.write(data, path+"train.data", m);
		data.instances = valid;
		SVMReader.write(data, path+"valid.data", m);
		data.instances = test;
		SVMReader.write(data, path+"test.data", m);
		System.out.println(m.featureFactory.all_features.size());
		*/
		int F = 22;
		for (int i = 0; i < F+1; i++){
			m.featureFactory.getFeature("F"+(i+1));
		}
		for (int i = 0; i < 2; i ++)
			m.labelFactory.getState(""+i);
		m.labelFactory.numLabels = 2;
		ClassData data = SVMReader.read(path+"train.data", m, true);
		 for (Instance inst: data.instances){
			inst.featureIndex.add(F);
			inst.featureValue.add(1.0);
		}
		//		m.labelFactory.all_labels.get(0).index = 1;
		//		m.labelFactory.all_labels.get(1).index = 0;
		//		m.labelFactory.all_labels.add(m.labelFactory.all_labels.remove(0));
		//		m.labelFactory.labelIndex.put(m.labelFactory.all_labels.get(0), 1);
		//		m.labelFactory.labelIndex.put(m.labelFactory.all_labels.get(1), 0);
		m.learn(data);
		m.predict(data, new MultiClassPerf(m.labelFactory.all_labels.size()));
		
		
		data = SVMReader.read(path+"valid.data", m, false);
		 for (Instance inst: data.instances){
				inst.featureIndex.add(F);
				inst.featureValue.add(1.0);
			}
		 m.predict(data, new MultiClassPerf(m.labelFactory.all_labels.size()));
		 data = SVMReader.read(path+"test.data", m, false);
		 for (Instance inst: data.instances){
				inst.featureIndex.add(F);
				inst.featureValue.add(1.0);
			}
		 m.predict(data, new MultiClassPerf(m.labelFactory.all_labels.size()));
		System.exit(-1);
	}
	
	
	public void TestHw3Digits(){
		LogisticRegression m = new LogisticRegression(new LRGradientAscentLearner());
		String path = "/scratch/ps3/digits/";
		String[] lines = FileUtil.getTextFromFile(path+"all.data").split("\n");
		for (int i = 0; i < 10; i++)
			m.labelFactory.getState("C"+i);
		 ArrayList<Instance> train = new ArrayList<Instance>();
			ArrayList<Instance> valid = new ArrayList<Instance>();
			ArrayList<Instance> test = new ArrayList<Instance>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 7700; i++){
			 sb.append(Integer.parseInt(lines[i].substring(0,1))+1);
			 sb.append(" "+lines[i].substring(2)+"\n");
		}
		FileUtil.writeTextToFile(sb.toString(), path+"train.data");
		sb = new StringBuilder();
		for (int i = 7700; i < 8800; i++){
			 sb.append(Integer.parseInt(lines[i].substring(0,1))+1);
			 sb.append(" "+lines[i].substring(2)+"\n");
		}
		FileUtil.writeTextToFile(sb.toString(), path+"valid.data");
		sb = new StringBuilder();
		for (int i = 8800; i < 10992; i++){
			 sb.append(Integer.parseInt(lines[i].substring(0,1))+1);
			 sb.append(" "+lines[i].substring(2)+"\n");
		}
		FileUtil.writeTextToFile(sb.toString(), path+"test.data");
		int F = 16;
		for (int i = 0; i < F+1; i++){
			m.featureFactory.getFeature("F"+(i+1));
		}
		for (int i = 0; i < 10; i ++)
			m.labelFactory.getState("C"+(i+1));
		m.labelFactory.numLabels = 10;
		ClassData data = SVMReader.read(path+"train.data", m, true);
		 for (Instance inst: data.instances){
			inst.featureIndex.add(F);
			inst.featureValue.add(1.0);
		}
		//		m.labelFactory.all_labels.get(0).index = 1;
		//		m.labelFactory.all_labels.get(1).index = 0;
		//		m.labelFactory.all_labels.add(m.labelFactory.all_labels.remove(0));
		//		m.labelFactory.labelIndex.put(m.labelFactory.all_labels.get(0), 1);
		//		m.labelFactory.labelIndex.put(m.labelFactory.all_labels.get(1), 0);
		m.learn(data);
		m.predict(data, new MultiClassPerf(m.labelFactory.all_labels.size()));
		
		
		data = SVMReader.read(path+"valid.data", m, false);
		 for (Instance inst: data.instances){
				inst.featureIndex.add(F);
				inst.featureValue.add(1.0);
			}
		 m.predict(data, new MultiClassPerf(m.labelFactory.all_labels.size()));
		 data = SVMReader.read(path+"test.data", m, false);
		 for (Instance inst: data.instances){
				inst.featureIndex.add(F);
				inst.featureValue.add(1.0);
			}
		 m.predict(data, new MultiClassPerf(m.labelFactory.all_labels.size()));
		 System.exit(-1);
	}
	
	public void TestHw3Vote(){

		LogisticRegression m = new LogisticRegression(new LRGradientAscentLearner());
		String path = "/scratch/ps3/voting/";
		/*String[] lines = FileUtil.getTextFromFile(path+"all.data").split("\n");
		m.labelFactory.getState("republican");
		 m.labelFactory.getState("democrat");
		 HashMap<String, Integer> map = new HashMap<String, Integer>();
		 map.put("y", 1);
		 map.put("n", 2);
		 map.put("?", 3);
		 ArrayList<Instance> train = new ArrayList<Instance>();
			ArrayList<Instance> valid = new ArrayList<Instance>();
			ArrayList<Instance> test = new ArrayList<Instance>();
			
		for (int i = 0; i < 300; i++){
			String[] items = lines[i].split(",");
			Instance inst = new Instance();
			inst.label = m.labelFactory.getState(items[0]);
			for (int j = 1; j< 17; j++){
				inst.featureIndex.add(j-1);
				inst.featureValue.add(map.get(items[j]));
			}
			train.add(inst);
		}
		for (int i = 300; i < 345; i++){
			String[] items = lines[i].split(",");
			Instance inst = new Instance();
			inst.label = m.labelFactory.getState(items[0]);
			for (int j = 1; j< 17; j++){
				inst.featureIndex.add(j-1);
				inst.featureValue.add(map.get(items[j]));
			}
			valid.add(inst);
		}
		for (int i = 345; i < 435; i++){
			String[] items = lines[i].split(",");
			Instance inst = new Instance();
			inst.label = m.labelFactory.getState(items[0]);
			for (int j = 1; j< 17; j++){
				inst.featureIndex.add(j-1);
				inst.featureValue.add(map.get(items[j]));
			}
			test.add(inst);
		}
		ClassData data = new ClassData();
		data.instances = train;
		SVMReader.write(data, path+"train.data", m);
		data.instances = valid;
		SVMReader.write(data, path+"valid.data", m);
		data.instances = test;
		SVMReader.write(data, path+"test.data", m);
		System.out.println(m.featureFactory.all_features.size());*/
		
		int F = 16;
		for (int i = 0; i < F+1; i++){
			m.featureFactory.getFeature("F"+(i+1));
		}
		for (int i = 0; i < 2; i ++)
			m.labelFactory.getState("C"+(i+1));
		m.labelFactory.numLabels = 2;
		ClassData data = SVMReader.read(path+"train.data", m, true);
		 for (Instance inst: data.instances){
			inst.featureIndex.add(F);
			inst.featureValue.add(1.0);
		}
		//		m.labelFactory.all_labels.get(0).index = 1;
		//		m.labelFactory.all_labels.get(1).index = 0;
		//		m.labelFactory.all_labels.add(m.labelFactory.all_labels.remove(0));
		//		m.labelFactory.labelIndex.put(m.labelFactory.all_labels.get(0), 1);
		//		m.labelFactory.labelIndex.put(m.labelFactory.all_labels.get(1), 0);
		m.learn(data);
		m.predict(data, new MultiClassPerf(m.labelFactory.all_labels.size()));
		System.out.println(m.para);
		
		data = SVMReader.read(path+"valid.data", m, false);
		 for (Instance inst: data.instances){
				inst.featureIndex.add(F);
				inst.featureValue.add(1.0);
			}
		 m.predict(data, new MultiClassPerf(m.labelFactory.all_labels.size()));
		 data = SVMReader.read(path+"test.data", m, false);
		 for (Instance inst: data.instances){
				inst.featureIndex.add(F);
				inst.featureValue.add(1.0);
			}
		 m.predict(data, new MultiClassPerf(m.labelFactory.all_labels.size()));
//		System.exit(-1);
	}

	public static void convert(String file1, String file2, int V){
		String[] lines = FileUtil.getTextFromFile(file1).split("\n");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lines.length; i++){
			String[] items = lines[i].split(" ");
			sb.append(items[0]);
			for (int j = 1; j < items.length; j++){
				String[] pair = items[j].split(":");
				int fi = Integer.parseInt(pair[0]);
				int fv =(int) Double.parseDouble(pair[1]);
				sb.append(" "+ ((fi-1)*V +fv)+":1");
			}
			sb.append("\n");
		}
		FileUtil.writeTextToFile(sb.toString(), file2);
	}
	
	public void testHw3Wiki(){
//		LogisticRegression m = new LogisticRegression(new LRGradientAscentLearner());//LRPerceptronLearner());
		NaiveBayes m = new NaiveBayes();
//		LRGradientAscentLearner.MAX_ITER_NUM = 10000;
//		LRGradientAscentLearner.eta = 0.1;
//		LRGradientAscentLearner.eps = 1e-2;
//		String path = "/scratch/ps3/wiki/";
		LRGradientAscentLearner.MAX_ITER_NUM = 500;
		LRGradientAscentLearner.eta = 0.01;
		LRGradientAscentLearner.eps = 0.01;
		String path = "/home/xiaoling/Dropbox/cse446/hw3/wiki/";
		int V = 2;
		int F = 32641*V;//22*V;
		int C = 6;
		int ITER = 100;
		int seed = 10;
//		convert(path+"train.data", path+"train2.data", V);
//		convert(path+"test.data", path+"test2.data", V);
//		convert(path+"valid.data", path+"valid2.data", V);
//		String[] classes = new String[]{"actor", "book", "company", "stadium", "university", "writer"};
		ArrayList<Instance> train = new ArrayList<Instance>();
		ArrayList<Instance> valid = new ArrayList<Instance>();
		ArrayList<Instance> test = new ArrayList<Instance>();
		/*for (String cl:classes){
			Label  l = m.labelFactory.getState(cl);
			String[] files =  new File(path+cl+"/train").list();
			// train
			for (int i = 0; i < 140; i++){
				String[] tokens = FileUtil.getTextFromFile(path+cl+"/train/"+files[i]).split("\\s+");
				HashSet<String> set=  new HashSet<String>();
				Instance inst = new Instance();
				inst.label = l;
				for (String token: tokens){
					set.add(token.toLowerCase());
				}
				for (String token: set){
					inst.featureIndex.add(m.featureFactory.getFeature(token).id);
					inst.featureValue.add(1.0);
				}
				train.add(inst);
			}
			// valid
			for (int i = 140; i < 160; i++){
				String[] tokens = FileUtil.getTextFromFile(path+cl+"/train/"+files[i]).split("\\s+");
				HashSet<String> set=  new HashSet<String>();
				Instance inst = new Instance();
				inst.label = l;
				for (String token: tokens){
					set.add(token.toLowerCase());
				}
				for (String token: set){
					inst.featureIndex.add(m.featureFactory.getFeature(token).id);
					inst.featureValue.add(1.0);
				}
				valid.add(inst);
			}
			// test
			for (int i = 160; i < 200; i++){
				String[] tokens = FileUtil.getTextFromFile(path+cl+"/train/"+files[i]).split("\\s+");
				HashSet<String> set=  new HashSet<String>();
				Instance inst = new Instance();
				inst.label = l;
				for (String token: tokens){
					set.add(token.toLowerCase());
				}
				for (String token: set){
					inst.featureIndex.add(m.featureFactory.getFeature(token).id);
					inst.featureValue.add(1.0);
				}
				test.add(inst);
			}
		}
		ClassData data = new ClassData();
		data.instances = train;
		SVMReader.write(data, path+"train.data", m);
		data.instances = valid;
		SVMReader.write(data, path+"valid.data", m);
		data.instances = test;
		SVMReader.write(data, path+"test.data", m);
		System.out.println(m.featureFactory.all_features.size());
*/

		// logreg
		
		for (int i = 0; i < F+1; i++){
			m.featureFactory.getFeature("F"+(i+1));
		}
		for (int i = 0; i < C; i ++)
			m.labelFactory.getState("C"+(i+1));
		m.labelFactory.numLabels = C;
		ClassData data = SVMReader.read(path+"train2.data", m, true);
		 for (Instance inst: data.instances){
			inst.featureIndex.add(F);
			inst.featureValue.add(1.0);
		}
		//		m.labelFactory.all_labels.get(0).index = 1;
		//		m.labelFactory.all_labels.get(1).index = 0;
		//		m.labelFactory.all_labels.add(m.labelFactory.all_labels.remove(0));
		//		m.labelFactory.labelIndex.put(m.labelFactory.all_labels.get(0), 1);
		//		m.labelFactory.labelIndex.put(m.labelFactory.all_labels.get(1), 0);
		 train = data.instances;
		 Collections.shuffle(train, new Random(seed));
		 data.instances = train;
		 SVMReader.write(data, path+"train.data.neworder", m);
		 
//		 int ITER = 20;
		 double[] acc = new double[ITER];
		 
		 m.learn(data);
		 ClassData data2 = SVMReader.read(path+"valid2.data", m, false);
		 for (Instance inst: data.instances){
			 inst.featureIndex.add(F);
			 inst.featureValue.add(1.0);
		 }
		 m.predict(data2, new MultiClassPerf(m.labelFactory.all_labels.size()));
		 {
			 double cur = 0;
			 double[][] wtx = new double[data2.instances.size()][C], p = new double[data2.instances.size()][C];
			 double[] w = ((LRParameter)m.para).lambda;
			 for (int i = 0; i < data2.instances.size(); i++){
					double max = -Double.MAX_VALUE;
					Instance x = data2.instances.get(i);
					for (int j = 1; j < C; j++){
						
						wtx[i][j] = 0;
						// features
						for (int f = 0; f < x.featureIndex.size(); f++){
							wtx[i][j] += w[j*F+ x.featureIndex.get(f)] * x.featureValue.get(f); 
						}
						// bias term REMOVED, instead change the original data
//						wtx[i][j] += w[j*F+F-1];
						
						if (wtx[i][j] > max){
							max = wtx[i][j];
						}
					}
					double pf = 0;
					for (int j = 0; j < C; j++){
						pf += Math.exp(wtx[i][j] - max);
					}
					for (int j = 0; j < C; j++){
						p[i][j] = Math.exp(wtx[i][j] - max) / pf;
						if (m.labelFactory.index(x.label) == j){
							cur += wtx[i][j] - max - Math.log(pf);
						}
					}
				}
			 System.out.println(cur+" = testCLL");
		 }
System.exit(-1);
		 data = new ClassData();
		 data2 = SVMReader.read(path+"test2.data", m, false);
		 for (Instance inst: data2.instances){
			 inst.featureIndex.add(F);
			 inst.featureValue.add(1.0);
		 }
		 
		 for (int i = 0; i < ITER; i++){
			 data.instances.add(train.get(i));
			 m.learn(data);
			 m.predict(data, new MultiClassPerf(m.labelFactory.all_labels.size()));
			 
			 
			 Performance perf = new MultiClassPerf(m.labelFactory.all_labels.size());
			 m.predict(data2, perf);
			 acc[i] = ((double)((MultiClassPerf)perf).correct) / ((MultiClassPerf)perf).all;
		 }
		 System.out.println("((******");
		 for (int i = 0; i < ITER; i++)
		 System.out.println(acc[i]);
//		System.exit(-1);
	}

	public void TestReuters(){
		LogisticRegression m = new LogisticRegression(new LRGradientAscentLearner());
		LRGradientAscentLearner.MAX_ITER_NUM = 10000;
		LRGradientAscentLearner.eta = 0.1;
		LRGradientAscentLearner.eps = 0.1;
//		m.debug = true;
		//		LRConjugateLearner.MAX_ITER_NUM = 10;
		//		LRConjugateLearner.lambda = 0.1;
		Label neg = m.labelFactory.getState("NEGATIVE");
		Label pos = m.labelFactory.getState("POSITIVE");

		int F = 9947;
		for (int i = 0; i < F+1; i++){
			m.featureFactory.getFeature("F"+(i+1));
		}
		m.labelFactory.numLabels = 2;
		ClassData train = SVMReader.read("trainb.dat", m, true), test = SVMReader.read("testb.dat", m, false);
		for (Instance inst: train.instances){
			inst.featureIndex.add(F);
			inst.featureValue.add(1.0);
		}
		//		m.labelFactory.all_labels.get(0).index = 1;
		//		m.labelFactory.all_labels.get(1).index = 0;
		//		m.labelFactory.all_labels.add(m.labelFactory.all_labels.remove(0));
		//		m.labelFactory.labelIndex.put(m.labelFactory.all_labels.get(0), 1);
		//		m.labelFactory.labelIndex.put(m.labelFactory.all_labels.get(1), 0);
		m.learn(train);
		m.predict(train, new MultiClassPerf(m.labelFactory.all_labels.size()));
		for (Instance inst: test.instances){
			inst.featureIndex.add(F);
			inst.featureValue.add(1.0);
		}
		m.predict(test, new MultiClassPerf(m.labelFactory.all_labels.size()));

		//        m.learn();
//		m.writeModel("reuter.lr.model.bin");
//		LogisticRegression m2 = new LogisticRegression();
//		m2.readModel("reuter.lr.model.bin");
//		m2.predict(test,  new MultiClassPerf(m.labelFactory.all_labels.size()));

	}

	public void TestToy(){
		LogisticRegression m = new LogisticRegression(new LRGradientAscentLearner());
		LRGradientAscentLearner.MAX_ITER_NUM = 10000;
		LRGradientAscentLearner.eta = 0.01;
		LRGradientAscentLearner.eps = 0.01;
//		m.debug = true;
		//		LRConjugateLearner.MAX_ITER_NUM = 10;
		//		LRConjugateLearner.lambda = 0.1;
		Label neg = m.labelFactory.getState("NEGATIVE");
		Label pos = m.labelFactory.getState("POSITIVE");

		int F = 2;
		for (int i = 0; i < F+1; i++){
			m.featureFactory.getFeature("F"+(i+1));
		}
		m.labelFactory.numLabels = 2;
		ClassData train = SVMReader.read("/scratch/toy.data", m, true), test = SVMReader.read("/scratch/toy.data", m, false);
		for (Instance inst: train.instances){
			inst.featureIndex.add(F);
			inst.featureValue.add(1.0);
		}
		//		m.labelFactory.all_labels.get(0).index = 1;
		//		m.labelFactory.all_labels.get(1).index = 0;
		//		m.labelFactory.all_labels.add(m.labelFactory.all_labels.remove(0));
		//		m.labelFactory.labelIndex.put(m.labelFactory.all_labels.get(0), 1);
		//		m.labelFactory.labelIndex.put(m.labelFactory.all_labels.get(1), 0);
		m.learn(train);
		Debug.pl(m.para+"");
		m.predict(train, new MultiClassPerf(m.labelFactory.all_labels.size()));
		for (Instance inst: test.instances){
			inst.featureIndex.add(F);
			inst.featureValue.add(1.0);
		}
		m.predict(test, new MultiClassPerf(m.labelFactory.all_labels.size()));

		//        m.learn();
//		m.writeModel("reuter.lr.model.bin");
//		LogisticRegression m2 = new LogisticRegression();
//		m2.readModel("reuter.lr.model.bin");
//		m2.predict(test,  new MultiClassPerf(m.labelFactory.all_labels.size()));

	}

	public static void TestSimulated(){
		Random r = new Random(0);
		int N = 1000, F = 100;
		LogisticRegression m = new LogisticRegression(new LRLBFGSLearner(5));
		Label neg = m.labelFactory.getState("NEGATIVE");
		Label pos = m.labelFactory.getState("POSITIVE");
		for (int i = 0; i < F; i++){
			m.featureFactory.getFeature("F"+i);
		}

		ClassData data1 = new ClassData(), data2 = new ClassData();
		PrintWriter writer = null; 
		double[] w = new double[F];
		try{
			writer = new PrintWriter(new FileWriter("toy_true.model"));
			for (int i = 0 ; i < F; i ++){
				w[i] = r.nextDouble() * 5 - 2.5;
				writer.println(w[i]);
			}
			writer.close();
		}catch(Exception e)
		{
			if (writer !=null)
				writer.close();
		}


		for (int i = 0 ; i < N/2 ; i++){
			Instance inst = new Instance();
			TIntList list = new TIntArrayList();
			int s = r.nextInt(F-30)+10;
			double score = 0;
			for (int j = 0; j < s; j++){
				int f = r.nextInt(F);
				while (list.contains(f)){
					f = (f+1)%F;
				}
				list.add(f);
				inst.featureValue.add(1.0);
				score += w[f];
			}
			inst.featureIndex = (list);
			if (score > 0){
				inst.label = pos;// pos
			}
			else{
				inst.label = neg;// neg
			}
			data1.instances.add(inst);
		}
		for (int i = 0 ; i < N/2 ; i++){
			Instance inst = new Instance();
			TIntList list = new TIntArrayList();
			int s = r.nextInt(F-30)+10;
			double score = 0;
			for (int j = 0; j < s; j++){
				int f = r.nextInt(F);
				while (list.contains(f)){
					f = (f+1)%F;
				}
				list.add(f);
				inst.featureValue.add(1.0);
				score += w[f];
			}
			inst.featureIndex = (list);
			if (score > 0){
				inst.label = pos;// pos
			}
			else{
				inst.label = neg;// neg
			}
			data2.instances.add(inst);
		}

		//		Learner l =  new LRLBFGSLearner(5);//new LRPerceptronLearner();
		//		Learner l = new LRConjugateLearner();
		//		l.learn(data1, m);
		m.learn(data1);
		m.debug = true;
		//		m.writeModel("toy.model");
		Tester t = new BinaryTester();
		t.test(data2, m);
	}
	//	public static void testSeq(SequenceModel mm, SeqData traindata,
	//			SeqData testdata) {
	//		LogisticRegression m = new LogisticRegression();
	//		m.all_labels = new ArrayList<Label>(mm.all_states);
	//		m.all_features = new ArrayList<Feature>(mm.all_features);
	////		SeqData traindata = new SeqData();
	////		traindata.read(m, s+".train", true);
	//		//        m.readData();
	//		Learner l = new LRPerceptronLearner();
	//		l.learn(traindata, m);
	//		//        m.learn();
	////		m.write_model(s+".model");
	//		Tester t = new BinaryTester();
	////		SeqData testdata = new SeqData();
	////		testdata.read(m, s+".test", false);
	//		t.test(testdata, m);
	//	}
}
