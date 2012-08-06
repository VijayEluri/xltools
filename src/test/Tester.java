package test;

import abs.Data;
import classifier.Model;

//import java.util.ArrayList;

public abstract class Tester
{
    public Data testdata = null;

    public Model model = null;
    public abstract void test(Data data, Model m);
    public Tester(){}
    public Tester(Model m){
    	model = m;
    }
	public void test2(Data testdata2, Model m, String string) {
		// TODO Auto-generated method stub
		
	}
}

