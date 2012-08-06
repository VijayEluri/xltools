package classifier;

import java.io.Serializable;

import abs.Data;

public abstract class Learner implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -600512262908205095L;
	public Model m = null;
    public abstract void learn(Data data, Model m);
    
}
