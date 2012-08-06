package abs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

import test.Debug;
import classifier.Model;

public class ClassData extends Data {

	String filename = null;

	// public ArrayList<String> dict = new ArrayList<String>();

	public ArrayList<Instance> instances = new ArrayList<Instance>();

//	public ClassData(Model m, String filename) {
//		this.filename = filename;
//		read(m, this.filename, true);// by default, reading as training data
		// which means new features would be added.
//	}

	public ClassData() {
	}


}


