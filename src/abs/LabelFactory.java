package abs;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import test.Debug;

public class LabelFactory implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7272496705680760370L;
	public  Hashtable<String, Label> labelNames = new Hashtable<String, Label>();
//	public Hashtable<Label, Integer> labelIndex = new Hashtable< Label, Integer>();
	public  TObjectIntMap<Label> labelIndex = new TObjectIntHashMap<Label>();
	public int numLabels = 0;
	public  ArrayList<Label> all_labels = new ArrayList<Label>();

	public  void clear(){
		labelNames.clear();
		labelIndex.clear();
		all_labels.clear();
	}
	public  int index(Label label) {
		if (labelIndex ==null || label ==null ){
			Debug.println(""+labelIndex +" "+label);
		}
		Integer l = labelIndex.get(label);
		if (l ==null){
			Debug.println(""+labelIndex +" "+label+" "+l);
		}
		return l;
	}
	// public static int num = 0;
	// public State(String s, int i)
	public  Label getState(String s){
		if (labelNames.containsKey(s)){
			return labelNames.get(s);
		}
		else{
			Label l  =new Label(s);
			l.index = numLabels;
			numLabels++;
			all_labels.add(l);
			labelNames.put(s, l);
			labelIndex.put(l, labelIndex.size());
			return l;
		}
	}
	public  boolean isTrain = true;
}
