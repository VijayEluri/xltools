package classifier.logreg;

import java.util.ArrayList;
import java.util.Random;

import classifier.Parameter;


public class LRVotedParameter extends LRParameter {
	public ArrayList<double[]> pool = null;
//	public double[] lambda = null;
	public int dim = 0;
	public LRVotedParameter(int num){
		super(num);
		dim = num;
		 pool = new ArrayList<double[]>();
	}
    public LRVotedParameter(int num, int epoch)
    {
    	super(num);
    	dim = num;
    	pool = new ArrayList<double[]>();
//        init();
    }

    

}
