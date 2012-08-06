package classifier.logreg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import test.Debug;
import util.V;
import util.io.FileUtil;
import abs.ClassData;
import abs.Data;
import abs.Instance;
import classifier.Learner;
import classifier.Model;

/**
 * L- BFGS for multi-class log reg
 * @author xiaoling
 *
 */
public class LRLBFGSLearner extends Learner {
	public LRLBFGSLearner(){
		super();
	}
	
	public LRLBFGSLearner(int ms){
		this();
		memSize = ms;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 5994635787889891829L;
	@Override
	public void learn(Data data, Model m) {
		// FIXME gTg[0] = 0!
		//		gTg[1] = 0!
		F = m.featureFactory.all_features.size();
		ClassData classdata = (ClassData)data;
		N = classdata.instances.size();
		C = m.labelFactory.all_labels.size();
		double[] w = new double[F*C];
		double[][] wtx = new double[N][C];
		double[] g = new double[F*C];
		computeGradient(g, classdata.instances, w, wtx);
		double gTgSqrt = Math.sqrt(V.inner(g, g));
		double[] q = new double[F*C];
		for (int j = 0; j < g.length; j++){
			q[j] = g[j] / gTgSqrt;
		}

		//  qtx = q^T x
		double[][] qtx = new double[N][C];
		for (int i = 0; i < N; i++){
			Instance x = classdata.instances.get(i);
			for (int k = 0; k < x.featureIndex.size(); k++){
				for (int j = 0; j < C; j++){
					qtx[i][j] += q[x.featureIndex.get(k)+j*F] * x.featureValue.get(k);
				}
			}
		}
		
		double eta = lineSearchHal(classdata.instances, wtx, w, q, g, qtx);
//		double[] u = new double[g.length];
//		for (int i = 0; i < u.length; i++){
//			u[i] = -g[i];
//		}
//		double eta = lineSearch(u);
		for (int i = 0; i < N; i++){
			for (int j = 0; j < C; j++){
				wtx[i][j] += eta * qtx[i][j]   ; // FIX x[n,c]?
			}
		}

		// w' = w prime
		double[] wp = new double[F*C];
//		System.arraycopy(w, 0, wp, 0, F*C);
		for (int i = 0; i < F*C; i++){
				wp[i] += eta * q[i];
		}
		double[] gp = new double[F*C];
		Memory mem = new Memory(memSize);

		boolean converged = false;
		double[] gdiff = new double[g.length], wdiff = new double[w.length];
		int iter = 0;
//		while (!converged){
		while (iter++ < 100){
			Arrays.fill(gp, 0);
			computeGradient(gp, classdata.instances, wp, wtx);
//			Arrays.fill(gdiff, 0);
			for (int i = 0; i < g.length; i++){
				gdiff[i] = gp[i] - g[i];
			}
//			Arrays.fill(wdiff, 0);
			for (int i = 0; i < w.length; i++){
				wdiff[i] = wp[i] - w[i];
			}
			double alpha = V.inner(gdiff, wdiff);
			double sigma = alpha / V.inner(gdiff, gdiff);
			mem.push(gdiff, wdiff, alpha);
			System.arraycopy(gp, 0, q, 0, q.length);
			double[] beta = new double[mem.wm.size()];
			for (int i = 0; i < mem.wm.size(); i++){
				beta[i] = V.inner(mem.wm.get(i), g) / mem.am.get(i);
				double[] u = mem.gm.get(i);
				for (int j = 0; j < q.length; j++){
					q[j] -= beta[i] * u[j];
				}
			}
			for (int j = 0; j < q.length; j++){
				q[j] *= sigma;
			}
			for (int i = 0; i < mem.wm.size(); i++){
				double zeta = V.inner(mem.gm.get(i), q);
				for (int j = 0; j < F*C; j++){
					double ksi = mem.wm.get(i)[j] * (beta[i] - zeta / mem.am.get(i));
					q[j] += ksi;
					zeta += ksi;
				}
			}
			for (int i = 0; i < q.length; i++){
				q[i] = -q[i];
			}
			for (int i = 0; i < N; i++){
				Instance x = classdata.instances.get(i);
				for (int k = 0; k < x.featureIndex.size(); k++){
					for (int j = 0; j < C; j++){
						qtx[i][j] += q[x.featureIndex.get(k)+j*F] * x.featureValue.get(k);
					}
				}
			}
			eta = lineSearchHal(classdata.instances, wtx, wp, q, g, qtx);
			if (eta == 0){
				break;
			}
			Debug.errl("log post = "+computePosterior(classdata.instances, wp, q, wtx, qtx, eta));
			for (int i = 0; i < N; i++){
				for (int j = 0; j < C; j++){
					wtx[i][j] += eta * qtx[i][j]   ; // FIX x[n,c]?
				}
			}

			System.arraycopy(wp, 0, w, 0, F*C);
			for (int i = 0; i < F*C; i++){
					wp[i] += eta * q[i];
			}
			System.arraycopy(gp, 0, g, 0, F*C);
		}

		m.para = new LRParameter(F*C);
		System.arraycopy(w, 0, ((LRParameter)m.para).lambda, 0, F*C);
	}


	/**
	 * 
	 * @param instances
	 * @param w : F-dim
	 * @param wtx: N x C
	 * @return
	 */
	public double[] computeGradient(double[] g, ArrayList<Instance> instances, double[] w, double[][] wtx){
		for (int i = 0; i < F * C; i++){
			g[i] = - lambda * w[i];
		}
		for (int i = 0; i < instances.size(); i++){
			Instance inst = instances.get(i);
			double z = 0;
			double MAX = -Double.MAX_VALUE;
			for (int j = 0; j < C; j++){
				if (wtx[i][j] > MAX){
					MAX = wtx[i][j];
				}
			}
			for (int j = 0; j < C; j++){
				z += Math.exp(wtx[i][j] - MAX);
			}
			for (int j = 0; j < C; j++){
				double delta = 0;
				if (instances.get(i).label.index == j)
					delta = 1;
				delta = (delta - Math.exp(wtx[i][j]-MAX)/z);
				for (int k = 0; k < instances.get(i).featureIndex.size(); k++){
					g[inst.featureIndex.get(k)+j*F] +=  delta * /*inst.featureValue.get(k)*/1;
				}
			}
		}
		StringBuilder  sb = new StringBuilder();
		for (int i = 0; i < g.length; i++){
			sb.append(g[i]+"\n");
		}
		FileUtil.writeTextToFile(sb.toString(), "tmp");
		return g;
	}

	/**
	 * 
	 * @param instances
	 * @param w F 
	 * @param q F
	 * @param wtx N * C
	 * @param qtx N * C
	 * @param eta
	 * @return
	 */
	public double computePosterior(ArrayList<Instance> instances, double[] w, double[] q, double[][] wtx, double[][] qtx, double eta){
		double wtw = V.inner(w, w);
		double qtq = V.inner(q, q);
		double p = - lambda /2 * (wtw*wtw+ eta * eta * qtq*qtq + 2 * eta * V.inner(q, w));
//		double p = 0;
		for (int i = 0; i < instances.size(); i++){
			double s = 0;
			for (int c = 0;  c < C; c++){
				s += Math.exp(wtx[i][c] + eta * qtx[i][c]);
				if (instances.get(i).label.index == c){
					p += wtx[i][c] + eta * qtx[i][c];
				}
			}
			s = Math.log(s);
			p -= s;
		}
		return p;
	}

//	public double lineSearch(ArrayList<Instance> instances, double[][] wtx, double[] w, double[] q, double[] g, double[][] qtx){
//		double eta = 1;
//		double ug = -
//		return eta;
//	}
	
	/**
	 * FIXME : always return 0 at "tau*eta < minEta"
	 * Hal's backtracking line search
	 * @param instances
	 * @param wtx
	 * @param w
	 * @param q
	 * @param g
	 * @param qtx
	 * @return
	 */
	public double lineSearchHal(ArrayList<Instance> instances, double[][] wtx, double[] w, double[] q, double[] g, double[][] qtx){
		double tau = 100 / Math.sqrt(V.inner(q, q));
		if (tau > 1){
			tau = 1;
		}
		double oldF = computePosterior(instances, w, q, wtx, qtx, 0);
		
		double gamma = tau * V.inner(g, q);
		if (gamma < 0)
			return 0;
		double tmp = -Double.MAX_VALUE;
		for (int c = 0; c < C; c++){
			for (int i = 0; i < F; i++){
				if (Math.abs(w[c*F+i]) > 1){
					if (Math.abs(q[c*F+i]) / Math.abs(w[c*F+i]) > tmp){
						tmp = Math.abs(q[c*F+i]) / Math.abs(w[c*F+i]);
					}
				}
				else{
					if (Math.abs(q[c*F+i])  > tmp){
						tmp = Math.abs(q[c*F+i]);
					}
				}
			}
		}
		double minEta = 1e-10 / tau / tmp;
		double eta = 1, oldEta = 0, f2 = oldF;

		while (true){
			if (tau*eta < minEta){
				return 0;
			}
			else{
				double f = computePosterior(instances, w, q, wtx, qtx, eta * tau);
				if (f >= oldF + 1e-4 *  tau * eta * gamma){
					return tau * eta;
				}
				else if (Math.abs(eta - 1) < 1e-20){
					double tmpEta = gamma / (2 * (oldF + gamma - f));
					oldEta = eta;
					eta = tmpEta > eta / 10 ?tmpEta : eta/10;
					f2 = f;
				}
				else {
					double r1 = f - oldF - gamma * eta;
					double r2 = f2 - oldF - gamma * oldEta;
					double a = (r1 / (eta * eta) - r2 / oldEta /oldEta )/ (eta - oldEta);
					double b = (eta * r2 / oldEta / oldEta - oldEta * r1 / eta / eta) / (eta - oldEta);
					double tmpEta = 0;
					if (Math.abs(a) < 1e-20){
						tmpEta = - gamma / 2/b;
					}
					else{
						double d = b * b - 3 * a * gamma;
						if (d < 0){
							tmpEta = gamma /2;
						}
						else if (b <= 0 ){
							tmpEta = (Math.sqrt(d) - b) / 3 /a;
						}
						else{
							tmpEta = - gamma / (b + Math.sqrt(d));
						}
					}
					if (tmpEta > eta / 2){
						tmpEta = eta /2 ;
					}
					oldEta = eta ;
					eta = tmpEta > eta / 10 ?tmpEta : eta/10;
					f2 = f;
				}
			}
		}
		

//		return tau * eta;
	}

	public int N = 0, F = 0, C =0 ;
	public int memSize = 5;
	public static int MAX_ITER_NUM = 10;
	
	// step size
	public static double lambda = 1;
}


class Memory{
	public Memory(){
		
	}
	public Memory(int m){
		M = m;
	}
	public int M = 5;
	public LinkedList<double[]> gm = new LinkedList<double[]>(),
	wm = new LinkedList<double[]>();
	// alpha memory
	public LinkedList<Double> am = new LinkedList<Double>();
	public void push(double[] g, double[] w, double a){
		gm.add(g);
		wm.add(w);
		am.add(a);
		while (gm.size() > M){
			gm.removeFirst();
			wm.removeFirst();
			am.removeFirst();
		}
	}
}