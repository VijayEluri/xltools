package testcases.abs.ontology;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import abs.ontology.NELLReader;


public class TestCategories {
	@Test public void test(){
		try{
			assertTrue(NELLReader.readCategories()==141);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
