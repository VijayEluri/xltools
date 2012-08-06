package classifier.svm;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Map.Entry;

import test.Debug;
import util.Serializer;
import abs.ClassData;
import abs.Data;
import abs.FeatureFactory;
import abs.LabelFactory;
import abs.Performance;
import classifier.Model;
import data.reader.SVMReader;

public class SVMWrapper extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3446323746683019522L;
	public String trainCmd = "./svm_learn";
	public String testCmd = "./svm_classify ";
	public String modelFile = "svm_model";
	public SVMWrapper(){		
		labelFactory = new LabelFactory();
		featureFactory = new FeatureFactory();
	}
	String optionStr = "";
	public SVMWrapper(Hashtable<String, String> options){

		StringBuilder sb = new StringBuilder();
		for (Entry e: options.entrySet()){
			sb.append(" -"+e.getKey()+" "+e.getValue());
		}
		optionStr = sb.toString();
		labelFactory = new LabelFactory();
		featureFactory = new FeatureFactory();
	}

	//	String trainFile = null, testFile = null;
	public void train(String trainfile, String modelfile){
		Runtime run = Runtime.getRuntime();
		int exitVal = Integer.MAX_VALUE;
		try{
			Process p = run.exec(trainCmd + optionStr+" "+ trainfile + " "+modelfile );

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = null;
			while ((line = reader.readLine())!=null){
				if (debug)
					Debug.errl(line);
			}
			reader.close();
			exitVal = p.waitFor();   
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	public void train(String trainfile){
		train(trainfile, modelFile);      
	}
	
	public double[] test( String testfile,Performance perf) {
		return test(testfile, modelFile, "svm_predictions",perf);
	}
	public double[] test( String testfile, String modelfile, String predfile, Performance perf) {
		//        String re = "";
		double[] results = new double[3];
		//test
		//        System.out.println("command: " + cmd);

		//Create a buffer to hold the output from MINIPAR
		//        StringBuffer buf = new StringBuffer();

		//Call MINIPAR and wait for it to terminate before continuing
		// SHOULD PROBABLY CHECK THE RETURN VALUE AT THIS POINT
		// TO TRY AND SPOT ANY ERRORS BEFORE WE GO ANY FURTHER
		Runtime run = Runtime.getRuntime();

		int exitVal = Integer.MAX_VALUE;

		try
		{
			Process p = run.exec(testCmd+" "+ testfile + " " + modelfile+" "+predfile);
			//            Thread inputThread = new Thread(new InputReader(p.getInputStream(), buf));
			//            Thread errorThread = new Thread(new InputReader(p.getErrorStream(), new StringBuffer()));
			//            Thread outputThread = new Thread(new InputReader(new ByteArrayInputStream(text.getBytes()), p.getOutputStream()));
			//
			//            inputThread.start();
			//            errorThread.start();
			//            outputThread.start();
			//
			//            inputThread.join();
			//            errorThread.join();
			//            outputThread.join();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			exitVal = p.waitFor(); 
			String line = null;
			while ((line = reader.readLine())!=null){
				if (debug)
					Debug.errl(line);
				//            	System.out.println(line);
				//            	buf.append(line);
				if (line.startsWith("Accuracy"))
					try{
						results[0] = Double.parseDouble(line.substring(line.indexOf(":")+1, line.indexOf("%")));
					}catch(NumberFormatException e){
						results[0] = 0;
					}
					if (line.startsWith("Precision"))
					{
						try{
							results[1] = Double.parseDouble(line.substring(line.indexOf(":")+1, line.indexOf("%")));	
						}catch(NumberFormatException e){
							results[1] = 0;
						}
						try{
							results[2] = Double.parseDouble(line.substring(line.lastIndexOf("/")+1, line.lastIndexOf("%")));
						}catch(NumberFormatException e){
							results[2] = 0;
						}
					}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return results;
	}
	@Override
	public void learn(Data data) {
		File tmp = null;
		try {
			tmp = File.createTempFile("svm", "train");
			tmp.deleteOnExit();
			SVMReader.write((ClassData)data,tmp.getPath(), this );
			//			if (new File(trainFile).exists())
			train(tmp.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void predict(Data data, Performance perf) {
		File tmp = null;
		try {
			tmp = File.createTempFile("svm", "train");
			tmp.deleteOnExit();
			SVMReader.write((ClassData)data,tmp.getPath(), this );
			//			if (new File(trainFile).exists())
			double[] r = test(tmp.getPath(), perf);
			Debug.println(r[0]+"\t"+r[1]+"\t"+r[2]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 

	@Override
	public void predict(Data data, String predFile, Performance perf) {
		File tmp = null;
		try {
			tmp = File.createTempFile("svm", "train");
			tmp.deleteOnExit();
			SVMReader.write((ClassData)data,tmp.getPath(), this );
			//			if (new File(trainFile).exists())
			double[] r = test(tmp.getPath(), modelFile, predFile, perf);
			Debug.println(r[0]+"\t"+r[1]+"\t"+r[2]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void readModel(String file) {

		SVMWrapper model = (SVMWrapper)Serializer.deserialize(file);
		this.trainCmd = model.trainCmd;
		this.testCmd = model.testCmd;
		modelFile = model.modelFile;
		labelFactory = model.labelFactory;
		featureFactory = model.featureFactory;
	}

	@Override
	public void writeModel(String file) {
		Serializer.serialize(this,file);
//		FileUtil.copyFile(modelFile, file);
//		modelFile = file;
	}
}
