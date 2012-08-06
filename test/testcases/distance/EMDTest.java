package testcases.distance;
import org.junit.Test;

import distance.EMD;

import junit.framework.TestCase;


public class EMDTest extends TestCase {
	@Test public void testExample1(){
		double[][] d = new double[4][3];
		double[][] f1 = new double[][]{ {100,40,22}, {211,20,2}, {32,190,150}, {2,100,100} };
		double[][] f2 = new double[][] { {0,0,0}, {50,100,80}, {255,255,255} };
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 3; j++){
				d[i][j] += (f1[i][0]-f2[j][0]) * (f1[i][0]-f2[j][0]);
				d[i][j] += (f1[i][1]-f2[j][1]) * (f1[i][1]-f2[j][1]);
				d[i][j] += (f1[i][2]-f2[j][2]) * (f1[i][2]-f2[j][2]);
				d[i][j] = Math.sqrt(d[i][j]);
			}
		}
		
		double[] wp = new double[]{0.4, 0.3, 0.2, 0.1}, wq = new double[]{ 0.5, 0.3, 0.2};
		
		assertTrue(Math.abs(EMD.distance(d, wp, wq) - 160.542770) < 1e-4);
	}
}
