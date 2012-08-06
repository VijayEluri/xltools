package test;

import abs.ClassData;
import abs.Data;
import abs.Instance;
import abs.Label;
import classifier.Model;
import classifier.logreg.LogisticRegression;

public class BinaryTester extends Tester {


	@Override
	public void test(Data data, Model m) {
		Label pos = m.labelFactory.getState("POSITIVE");
		
		ClassData seq = (ClassData) data;
		LogisticRegression lr = (LogisticRegression) m;
		int numPos = 0, numPredPos = 0, numPredPosCorrect = 0;



		for (int i = 0; i < seq.instances.size(); i++){
			Instance inst = seq.instances.get(i);
			boolean pred = false;
			Label predlabel = lr.infer.findBestLabel(inst, m).label;
			if (predlabel == pos)
			{
				pred = true;
				numPredPos++;
			}
			if (inst.label == pos)
			{
				numPos++;
				if (pred)
					numPredPosCorrect++;
			}
//			System.out.println(predm.labelFactory.name);
		}

		double prec = (double)numPredPosCorrect / numPredPos, rec = (double) numPredPosCorrect/ numPos;
		//		Debug.println("precision: "+prec+"("+numPredPosCorrect +"/"+ numPredPos+")");
		//		Debug.println("recall: "+rec+"("+numPredPosCorrect +"/"+ numPos+")");
		Debug.print(prec+"\t"+rec+"\t" +2*prec*rec/ (prec+rec));
	}

}
