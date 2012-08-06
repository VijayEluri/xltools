package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Random;

import util.io.FileUtil;
import abs.ClassData;
import classifier.Model;
import classifier.MultiClassPerf;
import classifier.svm.SVMWrapper;
import data.reader.SVMReader;

public class CrossValidation {
	public static void main(String[] args){
		CrossValidation cv = new CrossValidation();
		cv.k = 20;
		cv.split = 0.8;
		cv.dataFile = "data2.file";
		Hashtable<String, String> options = new Hashtable<String, String>();
		options.put("j","2.5");
		cv.run(new SVMWrapper(options));
//		cv.run(new NaiveBayes());
		System.out.println("");
		System.out.println("");
//		cv.run(new NaiveBayes(), "train.aux");
		cv.run(new SVMWrapper(options),"train.aux");
	}
	
	public CrossValidation(){
		
	}
	
	public int k = 5; // default : 5-fold
	
	public double split = 0.6; // default : 60% train, 40% test
	
	public String dataFile = null;
	
	private Random rand = new Random(System.currentTimeMillis());
	
	public void run(Model m){
		if (dataFile == null )
			return;
		new File("CV").delete();
		new File("CV").mkdir();
		for (int i = 0; i < k; i++){
			try{
				new File("CV/"+i).mkdir();
				BufferedReader reader = new BufferedReader(new FileReader(dataFile));
				PrintWriter writer1 = new PrintWriter(new FileWriter("CV/"+i+"/train.data"));
				PrintWriter writer2 = new PrintWriter(new FileWriter("CV/"+i+"/test.data"));
				String line = null;
				while ((line = reader.readLine())!=null){
					if (rand.nextDouble() >= split)
						writer2.println(line);
					else
						writer1.println(line);
				}
				writer1.close();
				writer2.close();
				reader.close();
				ClassData train = SVMReader.read("CV/"+i+"/train.data", m, true),
				test = SVMReader.read("CV/"+i+"/test.data", m, false);
				m.learn(train);
				m.predict(test, new MultiClassPerf(m.labelFactory.all_labels.size()));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * WithAuxTrain
	 * @param m
	 * @param auxFile: additional training file for each split
	 */
	public void run(Model m, String auxFile){
		if (dataFile == null || auxFile == null)
			return;
		if (!new File("CV").exists())
			new File("CV").mkdir();
		String content = FileUtil.getTextFromFile(auxFile);
		for (int i = 0; i < k; i++){
			try{
				if (!new File("CV/"+i).exists())
					new File("CV/"+i).mkdir();
				FileUtil.writeTextToFile(content, "CV/"+i+"/train.data");
				BufferedReader reader = new BufferedReader(new FileReader(dataFile));
				PrintWriter writer1 = new PrintWriter(new FileWriter("CV/"+i+"/train.data", true));
				PrintWriter writer2 = new PrintWriter(new FileWriter("CV/"+i+"/test.data"));
				String line = null;
				while ((line = reader.readLine())!=null){
					if (rand.nextDouble() >= split)
						writer2.println(line);
					else
						writer1.println(line);
				}
				writer1.close();
				writer2.close();
				reader.close();
				
				ClassData train = SVMReader.read("CV/"+i+"/train.data", m, true),
				test = SVMReader.read("CV/"+i+"/test.data", m, false);
				m.learn(train);
				m.predict(test, new MultiClassPerf(m.labelFactory.all_labels.size()));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
