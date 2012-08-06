package classifier;

import junit.framework.TestCase;
import abs.ClassData;
import classifier.nb.NaiveBayes;
import data.reader.SVMReader;

public class NBTest extends TestCase{
	public void test(){
		Model nb = new NaiveBayes();
		ClassData train = SVMReader.read("train.dat", nb, true);
		ClassData test = SVMReader.read("test.dat", nb, false);
		nb.learn(train);
		nb.predict(test, new MultiClassPerf(nb.labelFactory.all_labels.size()));
		nb.writeModel("nb.model.bin");
		Model nb2 = new NaiveBayes();
		nb2.readModel("nb.model.bin");
		nb2.predict(test, new MultiClassPerf(nb2.labelFactory.all_labels.size()));
	}
}
