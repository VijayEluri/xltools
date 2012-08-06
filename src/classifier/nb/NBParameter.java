package classifier.nb;

import classifier.Parameter;

public class NBParameter extends Parameter{
	double[][] para =null ;
	//	double[] negPara;
	double[] prior =null;
	int[][] occurrence = null;
	int[] all = null;
	//	int[] negOccurrence = new int[dim];
	//	int posAll = 0, negAll = 0;
	int classNum = 0;
	int dim = 8000;
	public NBParameter(int classSize, int FeatureSize){
		dim = FeatureSize;
		//********** assume the class set is incremental integers***********
		classNum = classSize;
		para = new double[classNum][dim];
		prior = new double[classNum];
		all = new int[classNum];
		occurrence = new int[classNum][dim];
	}
	
	public NBParameter(){
		super();
		occurrence = null;
		para = null;
		prior = null;
		all = null;
		classNum = 0;
		dim = 0;
	}
	@Override
	public void clear(){
		occurrence = null;
		para = null;
		prior = null;
		all = null;
		classNum = 0;
		dim = 0;
	}
	
}
