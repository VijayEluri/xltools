package util;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;

import junit.framework.TestCase;
import util.io.Debug;
import util.ling.Morpha;


public class MorphaTest extends TestCase {
	
	public void test(){
		Morpha m = new Morpha(new StringReader("Kan-Etsu_NNP"));
		
		try {
			System.out.println(m.next());
//			m.next();
			Hashtable<String, String> cache = new Hashtable<String, String>();
//			cache.put("", m.next());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
