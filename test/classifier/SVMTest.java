package classifier;

import junit.framework.TestCase;
import abs.ClassData;
import classifier.svm.SVMWrapper;
import data.reader.SVMReader;

public class SVMTest extends TestCase {
	public void test(){
		SVMWrapper svm = new SVMWrapper();
		svm.debug = true;
		ClassData train = SVMReader.read("train.dat", svm, true);
		ClassData test = SVMReader.read("test.dat", svm, false);
		svm.learn(train);
		svm.predict(test, new MultiClassPerf(svm.labelFactory.all_labels.size()));
		svm.writeModel("svm.model.bin");
		SVMWrapper svm2 = new SVMWrapper();
		svm2.readModel("svm.model.bin");
		svm2.predict(test, new MultiClassPerf(svm.labelFactory.all_labels.size()));
		
	}
}
