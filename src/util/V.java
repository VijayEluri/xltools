package util;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.TIntList;

import java.util.ArrayList;

import abs.Instance;

public class V {
	public static double sumprod(ArrayList<Integer> fi, ArrayList<Double> fv, double[][] w, int cl){
		double result = 0;
		for (int i = 0; i < fi.size(); i++){
			result += w[cl][ fi.get(i)] * fv.get(i);
		}
		return result;
	}
	public static double sumprod(ArrayList<Integer> fi, ArrayList<Double> fv, double[] w, int start){
		double result = 0;
		for (int i = 0; i < fi.size(); i++){
			result += w[start + fi.get(i)] * fv.get(i);
		}
		return result;
	}
	
	public static double sumprod(Instance x, double[][] w, int cl){
		double result = 0;
		for (int i = 0; i < x.featureIndex.size(); i++){
			result += w[cl][ x.featureIndex.get(i)] * x.featureValue.get(i);
		}
		return result;
	}
	public static double sumprod(Instance x, double[] w, int start){
		double result = 0;
		for (int i = 0; i < x.featureIndex.size(); i++){
			result += w[start + x.featureIndex.get(i)] * x.featureValue.get(i);
		}
		return result;
	}
	
	public static double sumprod(TIntList fi, ArrayList<Double> fv, double[][] w, int cl){
		double result = 0;
		for (int i = 0; i < fi.size(); i++){
			result += w[cl][ fi.get(i)] * fv.get(i);
		}
		return result;
	}
	public static double sumprod(TIntList fi, ArrayList<Double> fv, double[] w, int start){
		double result = 0;
		for (int i = 0; i < fi.size(); i++){
			result += w[start + fi.get(i)] * fv.get(i);
		}
		return result;
	}
	
	public static double sumprod(TIntList fi, TDoubleList fv, double[][] w, int cl){
		double result = 0;
		for (int i = 0; i < fi.size(); i++){
			result += w[cl][ fi.get(i)] * fv.get(i);
		}
		return result;
	}
	public static double sumprod(TIntList fi, TDoubleList fv, double[] w, int start){
		double result = 0;
		for (int i = 0; i < fi.size(); i++){
			result += w[start + fi.get(i)] * fv.get(i);
		}
		return result;
	}
	
	public static double inner(double[] g, double[] g2) {
		double s = 0;
		for (int i = 0; i < g.length; i++){
			s += g[i] * g2[i];
		}
		return s;
	}
}
