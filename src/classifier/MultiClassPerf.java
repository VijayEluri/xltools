package classifier;

import test.Debug;
import abs.Performance;

public class MultiClassPerf implements Performance{
	public MultiClassPerf(int cSize){
		this.cSize = cSize;
		confusion  = new int[cSize][cSize];
	}
	int cSize = 0;
	int[][] confusion ;
	int correct = 0, all = 0;
	@Override
	public void update(Object pred, Object truth) {
		if (pred == truth)
		{
			correct++;						
		}
		all ++;
		confusion[(Integer)pred][(Integer)truth] ++;
	}

	@Override
	public void print() {
		Debug.println("accuracy:" + (double)correct / all + ", "+correct +" / "+ all);
		for (int i = 0; i < cSize ; i++){
			for (int j = 0; j < cSize ; j++){
				Debug.print(confusion[i][j]+"\t");
			}
			Debug.println("");
		}
	}

}
