package classifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import abs.Data;
import abs.Feature;
import abs.FeatureFactory;
import abs.Label;
import abs.LabelFactory;
import abs.Performance;

public abstract class Model implements Serializable{
	public Model(){
		featureFactory= new FeatureFactory();
		labelFactory = new LabelFactory();
	}
	public Model(Learner l, Inference i){
		this();
		learner = l;
		infer = i;
	}
	
	public  void clear(){
		featureFactory.clear();
		labelFactory.clear();
		
	}

	public  void restore(Model m){
		featureFactory = m.featureFactory;
//		m.featureFactory.all_features = new ArrayList<Feature>(all_features);
//		m.featureFactory.featureNames = new Hashtable<String, Feature>(featureNames);
		labelFactory = m.labelFactory;
		//		m.labelFactory.clear();
//		m.labelFactory.labelNames = new Hashtable<String, Label>( labelNames);
//		m.labelFactory.labelIndex = new Hashtable<Label, Integer>(labelIndex);
//		m.labelFactory.all_labels = new ArrayList<Label>(all_labels);
		//FIXME: restore totalNums
//		System.err.println("entity feature restored ");
//		System.err.println(all_labels);
	}

	
	public abstract void learn(Data data);
	public abstract void predict(Data data, Performance perf);
	public abstract void predict(Data data, String predFile, Performance perf);
//	public abstract void setTrainFile(String file);
//	public abstract void setTestFile(String file);
	public abstract void writeModel(String file);
	public abstract void readModel(String file);
	public Parameter para = null;
	public FeatureFactory featureFactory = null;
	public LabelFactory labelFactory = null;
	public boolean debug = false;
	public Learner learner = null;
	public Inference infer = null;
}
