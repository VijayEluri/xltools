
package abs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;


public class Feature implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1900972523053178672L;


	public int freq = 0;
	// public int prev_state = -1;
	// public int cur_state = -1;
	// public int[] words = null;
	// public int[] pos = null;
	public int id = -1;
	
	
	
	public String name = null;

	//	public Learner learner = null;
	
	
	Feature(String s, int i) {
		name = s;
		id = i;
		// index = num++;
	}

	public String toString(){
		return "Feature:"+name;
	}
	
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
//		if (this.getClass() != obj.getClass())
//			return false;
		return name.hashCode() == ((Feature) obj).name.hashCode();
//		return name.equals(((Feature) obj).name);
	}

	public int hashCode() {
		return name.hashCode();
	}
	// public int eval(int p_state, int c_state, int[] words, int[] pos){
	// try
	// {
	// if (p_state == prev_state &&
	// c_state == cur_state && pos.Length == this.pos.Length)
	// {
	// for (int i = 0; i < pos.Length; i++)
	// {
	// if (pos[i] != this.pos[i])
	// return 0;
	// }
	// for (int i = 0; i < pos.Length; i++)
	// {
	// if (words[i] != this.words[i])
	// return 0;
	// }
	// }
	// else
	// return 0;
	// return 1;
	// }
	// catch (IndexOutOfRangeException e)
	// {
	// Debug.pl("words.length != pos.length ", e.Message);
	// return 0;
	// }
	// }
}