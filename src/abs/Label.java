
package abs;

import java.io.Serializable;



public class Label implements Serializable  {
	 public int index = -1;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7791472947840136786L;
	public String name = null;
	Label(String s) {
		name = s;
		// index = num++;
	}

	public String toString(){
		return "Label:"+name;
	}
	
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
//		if (this.getClass() != obj.getClass())
//			return false;
		return name.hashCode() == ((Label) obj).name.hashCode();
//		return name.equals(((State) obj).name);
	}

	public int hashCode() {
		return name.hashCode();
	}

	
}