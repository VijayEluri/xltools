package util.ml;

import java.util.ArrayList;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;

public class LpSolveUtil {
	class LpSolveConstraint{
		public double[] coef = null;
		public int op = -1;
		public double val = 0;
	}
	
	public static LpSolve lpsolve(int numVar, ArrayList<LpSolveConstraint> constraints, double[] objParams) {
		LpSolve solver =null;
		try {
			// Create a problem with 4 variables and 0 constraints
			solver = LpSolve.makeLp(constraints.size(), numVar);

			// add constraints
			for (LpSolveConstraint c: constraints){
				solver.addConstraint(c.coef, c.op, c.val);
			}
			//      solver.strAddConstraint("3 2 2 1", LpSolve.LE, 4);
			//      solver.strAddConstraint("0 4 3 1", LpSolve.GE, 3);

			// set objective function
			//      solver.strSetObjFn("2 3 -2 3");
			solver.setObjFn(objParams);

			// solve the problem
			solver.solve();


			// delete the problem and free memory
			//      solver.deleteLp();
		}
		catch (LpSolveException e) {
			e.printStackTrace();
		}
		return solver;
	}

	public static void print(LpSolve solver){
		// print solution
		try{
			System.out.println("Value of objective function: " + solver.getObjective());
			double[] var = solver.getPtrVariables();
			for (int i = 0; i < var.length; i++) {
				System.out.println("Value of var[" + i + "] = " + var[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}


