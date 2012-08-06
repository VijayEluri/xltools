package classifier.logreg;

import test.Debug;
import abs.ClassData;
import abs.Data;
import abs.Instance;
import classifier.Model;
import classifier.MultiClassPerf;

public class LRGradientAscentLearner  extends classifier.Learner{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2250859581701667658L;

	@Override
	public void learn(Data data, Model m) {
		this.m = m;
		if (m == null)
		{
			Debug.println("learner doesn't have a model");
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
		int F = m.featureFactory.all_features.size()/*+1*/, N = classdata.instances.size(), C = lr.labelFactory.all_labels.size();
		m.para = new LRParameter(C*(F));
		LRParameter para = (LRParameter)(m.para);

		//loops each iteration, each instances
		//        m.tester.testdata = seqdata;//test

		//		Tester tester = new BinaryTester();

		//		double timeInMinutes = timeInSeconds / 60.0

		// variable definitions
		double[] w = para.lambda;
		double[][] wtx = new double[N][C]; // w^T x
		// conditional prob
		double[][] p = new double[N][C];
		double prev = 0, cur = 0;		
		//***init*
		//compute wtx and p
		for (int i = 0; i < N; i++){
			double max = -Double.MAX_VALUE;
			Instance x = classdata.instances.get(i);
			for (int j = 1; j < C; j++){
				
				wtx[i][j] = 0;
				// features
				for (int f = 0; f < x.featureIndex.size(); f++){
					wtx[i][j] += w[j*F+ x.featureIndex.get(f)] * x.featureValue.get(f); 
				}
				// bias term REMOVED, instead change the original data
//				wtx[i][j] += w[j*F+F-1];
				
				if (wtx[i][j] > max){
					max = wtx[i][j];
				}
			}
			double pf = 0;
			for (int j = 0; j < C; j++){
				pf += Math.exp(wtx[i][j] - max);
			}
			for (int j = 0; j < C; j++){
				p[i][j] = Math.exp(wtx[i][j] - max) / pf;
				if (lr.labelFactory.index(x.label) == j){
					cur += wtx[i][j] - max - Math.log(pf);
				}
			}
		}
		

		int idx_iter = 0;
		do
		{
			//        	Debug.println(idx_iter+"... ");
			long startBuild = System.currentTimeMillis();
			//			m.predict(classdata, new MultiClassPerf(m.labelFactory.all_labels.size()));//test	
			//        	tester.test(seqdata, m);//test
			Debug.println("conditional probability = "+cur);
			prev = cur;

			// l2 regularization
			if (l2){
				for (int k = 0; k < w.length; k++)
					w[k] -= eta*lambda*w[k];
			}
			
			for (int idx_inst = 0; idx_inst < N; idx_inst++){
				Instance x = classdata.instances.get(idx_inst);
				for (int f = 0; f < x.featureIndex.size(); f++){
					int fi = x.featureIndex.get(f);
					double fv = x.featureValue.get(f);
					for (int c = 1; c < C; c++){
						if (c == lr.labelFactory.index(x.label))
							w[c*F+fi] += eta * fv * (1 - p[idx_inst][c]);
						else
							w[c*F+fi] += eta * fv * ( - p[idx_inst][c]);
					}
				}
				//bias term  REMOVED, instead change the original data
//				for (int c = 1; c < C; c++){
//					if (c == lr.labelFactory.index(x.label))
//						w[c*F+F-1] += lambda  * (1 - p[idx_inst][c]);
//					else
//						w[c*F+F-1] += lambda  * ( - p[idx_inst][c]);
//				}
			}
			

			//compute wtx and p
			cur = 0;
			for (int i = 0; i < N; i++){
				double max = -Double.MAX_VALUE;
				Instance x = classdata.instances.get(i);
				for (int j = 1; j < C; j++){
					wtx[i][j] = 0;
					// features
					for (int f = 0; f < x.featureIndex.size(); f++){
						wtx[i][j] += w[j*F+ x.featureIndex.get(f)] * x.featureValue.get(f); 
					}
					// bias term REMOVED, instead change the original data
//					wtx[i][j] += w[j*F+F-1];
					
					if (wtx[i][j] > max){
						max = wtx[i][j];
					}
				}
				double pf = 0;
				for (int j = 0; j < C; j++){
					pf += Math.exp(wtx[i][j] - max);
				}
				for (int j = 0; j < C; j++){
					p[i][j] = Math.exp(wtx[i][j] - max) / pf;
					if (lr.labelFactory.index(x.label) == j){
						cur += wtx[i][j] - max - Math.log(pf);
					}
				}
			}			
			long endBuild = System.currentTimeMillis();
			double timeInSeconds = (double) (endBuild - startBuild) / (double) 1000;
			Debug.println(idx_iter+"th iteration took " + timeInSeconds+" seconds");
			idx_iter++;
		}while (Math.abs(prev-cur) >= eps && idx_iter < MAX_ITER_NUM);
		Debug.println("final conditional probability = "+cur);
		double sum = 0;
		for (int i = 0; i < F; i++){
			sum += Math.abs(w[F+i]);
		}
		System.out.println(sum);
	}

	
	public static int MAX_ITER_NUM = 100;
	// l2-reg
	public static boolean l2 = false;
	// regularization constant
    public static double lambda = 0;
	// step size
	public static double eta = 0.05;
	// stop criterion
	public static double eps = 1e-2;

}

