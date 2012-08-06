package abs;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

import java.util.ArrayList;

public class SeqInstance extends Instance {
	public TIntList states = new TIntArrayList();
	// public ArrayList<Feature> features = new ArrayList<Feature>();
	// public ArrayList<Integer> obs = new ArrayList<Integer>();
	public ArrayList<TIntList> features = new ArrayList<TIntList>();
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < states.size(); i++){
			sb.append(features.get(i).get(0)+"->"+states.get(i)+"\n");
		}
		return sb.toString();
	}
	
}

