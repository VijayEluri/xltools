package classifier.logreg;

import java.lang.reflect.Array;
import java.util.Random;

import classifier.Parameter;

import edu.stanford.nlp.util.ArrayUtils;


public class LRParameter extends Parameter {
	public double[] lambda = null;

	public LRParameter(int num)
	{
		lambda = new double[num];
		//        init();
	}

	public void init()
	{
		if (lambda==null)
			return;
		Random r = new Random((int)(System.currentTimeMillis() % Integer.MAX_VALUE));
		for (int i = 0; i < lambda.length; i++)
		{
			lambda[i] = r.nextDouble() * var;
		}
	}

	public static double var = 0.2;

	public String toString()
	{
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < lambda.length; i++)
			b.append(lambda[i] + "\n");

		return b.toString();
	}

	@Override
	public void clear() {
		if (lambda !=null)
			lambda = new double[lambda.length];
	}

}
