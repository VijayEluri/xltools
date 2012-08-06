package abs;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.list.array.TIntArrayList;

import java.io.Serializable;

import classifier.Model;

public class Instance implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -851405716099693492L;
	public Label label = null;
	// public ArrayList<Feature> features = new ArrayList<Feature>();
	// public ArrayList<Integer> obs = new ArrayList<Integer>();
	/**
	 * if featureIndex has a small number of features, then TIntArrayList is fine.
	 * otherwise, TIntLinkedList is better.
	 */
	public TIntList featureIndex = new TIntArrayList();
//	public ArrayList<Integer> featureIndex = new ArrayList<Integer>();
	public TDoubleList featureValue = new TDoubleArrayList();
//	public ArrayList<Double> featureValue = new ArrayList<Double>();
	public void setLabel(Label l){
		label = l;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(label);
		for (int i = 0; i < featureIndex.size(); i++){
			sb.append(" "+/*Feature.all_features.get*/(featureIndex.get(i))/*.name*/+":"+featureValue.get(i));
		}
		return sb.toString();
	}
	
	public String toString(Model m){
		StringBuffer sb = new StringBuffer();
		sb.append(label);
		for (int i = 0; i < featureIndex.size(); i++){
			sb.append(" "+m.featureFactory.all_features.get(featureIndex.get(i)).name+":"+featureValue.get(i));
		}
		return sb.toString();
	}
}
