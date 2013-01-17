package classifier.logreg;

import java.util.Arrays;
import java.util.Collections;

import util.io.Debug;
import abs.ClassData;
import abs.Data;
import abs.Instance;
import classifier.Model;

/**
 * confidence-weighted for binary, 0 negative, 1 positive
 * cf. COnfidence-weighted Linear Classification icml 2008
 * @author xiaoling
 *
 */
public class LRCWLearner extends classifier.Learner{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8494498667687750050L;
	// confidence parameter: 0.6 -> 0.253, 0.7 -> 0.524, 0.8 -> 0.842 
	public static double phi = 0.9;
//	init variance parameter
	public static double var = 0.1;
	// iteration number
	public static int MAX_ITER_NUM = 100;
	// convergence
	public static double eps = 1e-2;
	@Override
	public void learn(Data data, Model m) {
		
		this.m = m;
		if (m == null)
		{
			Debug.pl("learner doesn't have a model");
			return;
		}

		LogisticRegression lr = (LogisticRegression) m;
		ClassData classdata = (ClassData)data;

		// also, potentially only "1*size" suffices
		int F = m.featureFactory.all_features.size()/*+1*/, N = classdata.instances.size(), C = lr.labelFactory.all_labels.size();
		m.para = new LRParameter(C*(F));
		LRParameter para = (LRParameter)(m.para);

		// variable definitions
		double[] w = para.lambda;
		double[] sigma = new double[F];
		double[] ywtx = new double[N];
		double[] xsx = new double[N];
		double prev = 0, cur = 0;		
		//***init**
		Arrays.fill(sigma, var);
		//compute ywtx and xsx
		for (int i = 0; i < N; i++){
			Instance x = classdata.instances.get(i);
				
			// features
			for (int f = 0; f < x.featureIndex.size(); f++){
				double fv = x.featureValue.get(f);
				int fi = x.featureIndex.get(f);
				ywtx[i] += w[F+ fi] * fv; 
				xsx[i] += fv*fv*sigma[fi];
			}
			if (m.labelFactory.index(x.label) == 0){
				
				if (ywtx[i] < Double.MAX_EXPONENT)
					cur -= Math.log (Math.exp(ywtx[i])+1);
				else
					cur -= ywtx[i];
				
				ywtx[i] = -ywtx[i];
			}
			else{
				cur += ywtx[i];
				if (ywtx[i] < Double.MAX_EXPONENT)
					cur -= Math.log (Math.exp(ywtx[i])+1);
				else
					cur -= ywtx[i];
			}
				
		}
		
		int idx_iter = 0;
		do
		{
			long startBuild = System.currentTimeMillis();
			Debug.pl("conditional probability = "+cur);
			prev = cur;
			Collections.shuffle(classdata.instances);
			for (int idx_inst = 0; idx_inst < N; idx_inst++){
				Instance x = classdata.instances.get(idx_inst);
				
				// compute alpha_i
				double alpha = 0;
				double gamma = - (1+2*phi*ywtx[idx_inst]) 
				+ Math.sqrt((1+2*phi*ywtx[idx_inst])*(1+2*phi*ywtx[idx_inst])
						-8 * phi * (ywtx[idx_inst] - phi * xsx[idx_inst]));
				if (gamma > 0){
					alpha = gamma / (4 * phi * xsx[idx_inst]);
				}
				else{
					continue;
				}
					
				double updateConstant = alpha;
				if (m.labelFactory.index(x.label)==0)
					// negative
					updateConstant = -alpha;
				for (int f = 0; f < x.featureIndex.size(); f++){
					int fi = x.featureIndex.get(f);
					double fv = x.featureValue.get(f);
					w[F+fi] += fv * updateConstant * sigma[fi];
					sigma[fi] = 1 / (1/sigma[fi]+2*alpha*phi*fv*fv);
				}
			}
			
			//compute wtx
			cur = 0;
			for (int i = 0; i < N; i++){
				Instance x = classdata.instances.get(i);
				ywtx[i] = 0;
				xsx[i] = 0;
				// features
				for (int f = 0; f < x.featureIndex.size(); f++){
					double fv = x.featureValue.get(f);
					int fi = x.featureIndex.get(f);
					ywtx[i] += w[F+ fi] * fv; 
					xsx[i] += fv*fv*sigma[fi];
				}
				if (m.labelFactory.index(x.label) == 0){
					
					if (ywtx[i] < Double.MAX_EXPONENT)
						cur -= Math.log (Math.exp(ywtx[i])+1);
					else
						cur -= ywtx[i];
					ywtx[i] = -ywtx[i];
				}
				else{
					cur += ywtx[i];
					if (ywtx[i] < Double.MAX_EXPONENT)
						cur -= Math.log (Math.exp(ywtx[i])+1);
					else
						cur -= ywtx[i];
				}
			}	
			long endBuild = System.currentTimeMillis();
			double timeInSeconds = (double) (endBuild - startBuild) / (double) 1000;
			Debug.pl(idx_iter+"th iteration took " + timeInSeconds+" seconds");
			idx_iter++;
		}while (Math.abs(prev-cur) >= eps && idx_iter < MAX_ITER_NUM);
		Debug.pl("final conditional probability = "+cur);
//		System.out.println(Arrays.toString(w));
	}

	
	
  
}
