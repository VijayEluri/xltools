/*
 * 创建日期 Oct 12, 2010
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package eval;

import java.util.HashSet;
import java.util.Hashtable;

public class KuhnMunkres {
	/**
	 * Given U and V, find the maximal matching M
	 * S is a subset of U which is not covered by T
	 * T is an alternating tree (parent map)
	 * lu, lv are the feasible values
	 * 
	 * slack = lu(u)+ lv(v) - matrix[u][v]
	 * minSlackU is verteces in U
	 * 
	 * @param matrix
	 * @return the match value
	 */
	private static double[][] matrix = null;
	private static int n;
	private static double[] lu, lv;
	private static Hashtable<Integer, Integer> T ;
	private static HashSet<Integer> S ;
	private static double[] minSlackValue = null;
	private static int[] minSlackU = null;
	private static int[] mu, mv;
	public static double match(double[][] mat){
		//init
		matrix = mat;
		int n1 = mat.length;
		int n2 = mat[0].length;
		n = Math.max(n1, n2);
		matrix = new double[n][n];
		for (int i = 0; i < n1; i++){
			System.arraycopy(mat[i], 0, matrix[i], 0, n2);
		}
		
		//********test*******
//		for (int i = 0; i < n; i++){
//			for (int j = 0; j < n; j++){
//				System.out.print(matrix[i][j]+" ");
//			}
//			System.out.println();
//		}
		//***************
		
		lu = new double[n];
		lv = new double[n];
		mu = new int[n];
		mv = new int[n];
		for (int i = 0; i < n; i++){
			mu[i] = -1;
			mv[i] = -1;
			for (int j = 0; j < n2 ;j++){
				if (mat[i][j] > lu[i])
					lu[i] = mat[i][j];
			}
			lv[i] = 0;
		}
		minSlackValue = new double[n];
		minSlackU = new int[n];
		
		int u0 = -1; 
		
		while ((u0 = findNotAssigned(mu)) !=-1){
			// find free vertex in U, i.e. u0
			
			// calculate minSlack
			for (int i = 0; i < n; i++){
				minSlackValue[i] = slack(u0, i);
				minSlackU[i] = u0;
			}
			
			// build tree
			S = new HashSet<Integer>();
			T = new Hashtable<Integer, Integer>();
			S.add(u0);
			
			// modify matching
			modify();
			
		}
		
		
		double val = 0;
		for (int i = 0; i < n ;i++){
			if (mu[i]< n2)
				val += lu[i];
			if (mv[i]< n1)
				val+=lv[i];
		}
		return val;
	}
	/**
	 *  modify the matching, possibly improving the lu and lv
	 */
	private static void modify(){
	    while (true){
	        // select edge (u,v) with u in S, v not in T and min slack
	    	double value = 1E9;
	    	int u = -1, v = -1;
	    	for (int i = 0; i < n ;i++){
	    		if (!T.containsKey(i) && minSlackValue[i] < value){
	    			value = minSlackValue[i];
	    			u = minSlackU[i];
	    			v = i;
	    		}
	    	}
	        assert (S.contains(u));
	        if (value>0){        
	            modifyL(value);
	        }
	        // now we are sure that (u,v) is saturated
	        assert( slack(u,v)==0);
	        T.put(v, u); // add (u,v) to the tree
	        if (mv[v]!=-1){
	            int u1 = mv[v];                      // previous matched edge 
	            assert(!S.contains(u1));
	            S.add(u1);                    // add endpoint to tree 
	            for (int i = 0; i < n ; i++){                     // maintain minSlack
	                if (!T.containsKey(i) && minSlackValue[i] > slack(u1, i)){
	                    minSlackValue[i] = slack(u1,v);
	                    minSlackU[i] = u1;
	                }
	            }
	        }else{
	            modifyM(v);              // v is a free vertex
	            return;
	        }
	    }
	}
	/**
	 * modify lu and lv
	 * @param val
	 */
	private static void modifyL(double val){
		// change the labels, and maintain minSlack. 
		for (Integer i: S)
			lu[i] -= val;
		for (int i = 0; i < n ;i++){
			if ( T.containsKey(i))
				lv[i] += val;
			else
				minSlackValue[i] -= val;
		}
	}
	/**
	 * add matching to M or change previous matchings
	 * @param v
	 */
	private static void modifyM(int v){
		    // apply the alternating path from v to the root in the tree. 
		    int u = T.get(v);
		    if (mu[u]!=-1){
		        modifyM(mu[u]);
		    }
		    mu[u] = v;
		    mv[v] = u;
	}
	    
	private static double slack(int u, int v){
		return lu[u]+lv[v]-matrix[u][v];
	}
	
	
	private static int findNotAssigned(int[] m){
		int c = -1; 
		for (int i = 0; i < m.length; i++){
			if (m[i] == -1)
				return i;
		}
		return c;
	}
	
	
	public static void main(String[] args){
		double[][] a = new double[][]{{5,0},{0,2},{0,5}};
		System.out.println(match(a));
		for (int i = 0; i < n; i++){
			System.out.println(i+"->"+mu[i]+"\t"+mv[i]+"<-"+i);
		}
	}
}
