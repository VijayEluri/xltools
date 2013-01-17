package classifier.logreg;

import util.io.Debug;
import abs.ClassData;
import abs.Data;
import abs.Instance;
import classifier.Model;
import classifier.MultiClassPerf;

/**
 * Conjugate Descent for binary log reg
 * 
 * Please refer to
 * Notes on CG and LM-BFGS Optimization of Logistic Regression
 * by Hal Daume III
 * @author xiaoling
 *
 */

public class LRConjugateLearner extends classifier.Learner{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2250859581701667658L;

	@Override
	public void learn(Data data, Model m) {
		this.m = m;
		if (m == null)
		{
			Debug.pl("learner doesn't have a model");
			return;
		}

		LogisticRegression lr = (LogisticRegression) m;
		//********************
		// Sequential Data FIXME not any more!
		//********************
//		SeqData seqdata = (SeqData)data;
		ClassData classdata = (ClassData)data;

		// find the index of targetState
//		for (int i = 0; i < lr.all_states.size(); i++){
//			if (lr.all_states.get(i).name.equals(LogisticRegression.targetState))
//			{
//				LogisticRegression.targetIndex = i;
//				break;
//			}
//		}


		// FIXME binary problem, 0 negative, 1 positive
		// also, potentially only "1*size" suffices
		int F = m.featureFactory.all_features.size(), N = classdata.instances.size();
		m.para = new LRParameter(2*F);
		LRParameter para = (LRParameter)(m.para);

		//loops each iteration, each instances
		//        m.tester.testdata = seqdata;//test

//		Tester tester = new BinaryTester();

		//		double timeInMinutes = timeInSeconds / 60.0

		// variable definitions
		double[] w = para.lambda;
		double[] wtx = new double[classdata.instances.size()]; // w^T x
		double[] g = new double[F]; // gradient
		double[] u = new double[F]; // arbitrary direction
		// new gradient, g prime        
		double[] gp = new double[F];

		
		// NOTE: translate Label to +1/-1
		int[] y = new int[N];
		for (int i = 0; i < N; i++){
			y[i] = m.labelFactory.index(classdata.instances.get(i).label)*2 - 1;
		}
		for (int idx_iter = 0; idx_iter < MAX_ITER_NUM; idx_iter++)
		{
			//        	Debug.pl(idx_iter+"... ");
			long startBuild = System.currentTimeMillis();
			m.predict(classdata, new MultiClassPerf(m.labelFactory.all_labels.size()));//test	
			//        	tester.test(seqdata, m);//test

			for (int i = 0; i < F; i++){
				gp[i] = - lambda * w[i+F];
			}

			for (int idx_inst = 0; idx_inst < N; idx_inst++){
				double sm = y[idx_inst] * sigmoid(-y[idx_inst] * wtx[idx_inst]);
				Instance x = classdata.instances.get(idx_inst);
//				for (ArrayList<Integer> fati: x.features){
					// features at i position
					for (int i = 0; i < x.featureIndex.size(); i++){
						
						gp[x.featureIndex.get(i)] += sm* x.featureValue.get(i); // default feature value = 1 FIXME
					}
//				}
			}

			double beta = 0, beta0 = 0 ;
			if (idx_iter == 0){
				//  u can be zeros initially, so initialize it by gp instead for the first epoch
				beta = 1;
				for (int i = 0; i < F; i++){
					u[i] = gp[i];
				}
			}
			else{
				for (int i = 0; i< F; i++){
					beta += gp[i] * (gp[i] - g[i]);
					beta0 += u[i] * (gp[i] - g[i]);
				}
				beta /= beta0;
			}
			
			for (int i = 0; i < F; i++){
				u[i] = g[i] - beta * u[i];
			}
			
			double z0 = 0, sm = 0;
			double[] ux = new double[N];
			for (int i = 0; i < N; i++){
				sm = sigmoid(wtx[i]);
				Instance x = classdata.instances.get(i);
//				for (ArrayList<Integer> fati: x.features){
					// features at i position
//					for (Integer f : fati){
				    for (int j = 0; j < x.featureIndex.size(); j++){
						ux[i] += u[x.featureIndex.get(j)] * x.featureValue.get(j); 
					}
//				}
				z0 += sm*(1-sm) * ux[i]*ux[i];
			}
			double z = sumprod(gp, u)/ (lambda* sumprod(u, u) + z0);

			for (int i = 0; i < F; i++){
				w[i+F] += z * u[i];
				w[i] = - w[i+F];
			}
			
			for (int idx_inst = 0; idx_inst < classdata.instances.size(); idx_inst++)
			{
				wtx[idx_inst] += z * ux[idx_inst]; 
			}
			
			System.arraycopy(gp, 0, g, 0, F);
			
			long endBuild = System.currentTimeMillis();
			double timeInSeconds = (double) (endBuild - startBuild) / (double) 1000;
			Debug.pl(idx_iter+"th iteration took " + timeInSeconds+" seconds");
		}
	}

	private static double sigmoid(double f){
		return 1/(1+ Math.exp(-f));
	}
	private static double sumprod(double[] a, double[] b){
		double c = 0;
		for (int i = 0; i < a.length; i++){
			c+= a[i] * b[i];
		}
		return c;
	}
	public static int MAX_ITER_NUM = 10;

	// step size
	public static double lambda = 1;
}
