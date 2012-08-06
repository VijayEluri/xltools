package abs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import test.Debug;

public class FeatureFactory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 660660786014221315L;
	private  int totalNum = 0;
	public static int FREQ_THRESHOLD = 3;

	public  void clear(){
		totalNum = 0;
		featureNames.clear();
		all_features.clear();
		isTrain = true;
	}
	// public int index{get; set;}
	public Hashtable<String, Feature> featureNames = new Hashtable<String, Feature>();
	public ArrayList<Feature> all_features = new ArrayList<Feature>();

	// public static int num = 0;

	// public Feature(String s, int i)
	// {
	// name = s;
	// index = i;
	// num = index + 1;
	// }
	public void filter(ClassData data){
		Debug.println("before filtering "+all_features.size());
		long sum = 0;
		for (int i = all_features.size() - 1; i >=0 ; i--){
			sum+=all_features.get(i).freq;
			if (all_features.get(i).freq < FREQ_THRESHOLD){
				featureNames.remove(all_features.get(i).name);
				all_features.remove(i);
			}
		}
		Debug.println("average # = "+sum/all_features.size());
		Hashtable<Integer, Integer> old2new = new Hashtable<Integer, Integer>();
		for (int i = 0; i < all_features.size(); i++){
			old2new.put(all_features.get(i).id, i);
			all_features.get(i).id = i;
		}
		totalNum = all_features.size();
		Debug.println("after filtering "+all_features.size());
		for (Instance inst: data.instances){
			for (int i = inst.featureIndex.size()-1; i >= 0; i--){
				if (!old2new.containsKey(inst.featureIndex.get(i))){
					inst.featureIndex.removeAt(i);
					inst.featureValue.removeAt(i);
				}
				else{
					inst.featureIndex.set(i, old2new.get(inst.featureIndex.get(i)));
				}
			}
		}
	}
	public boolean isTrain = true;
	public Feature getFeature(String s){
		if (featureNames.containsKey(s)){
			Feature f =featureNames.get(s);
			if (isTrain)
				f.freq++;
			return f;
		}
		else{
			if (isTrain){
				Feature f = new Feature(s, totalNum);
				f.freq++;
				totalNum++;
				all_features.add(f);
				featureNames.put(s, f);
				return f;
			}
			else
				return null;
		}

	}
}
