package test;

import abs.ClassData;
import abs.Data;
import abs.Instance;
import abs.Label;
import classifier.Model;
import classifier.Prediction;
import classifier.logreg.LogisticRegression;

public class MultiTester extends Tester {

	@Override
	public void test(Data data, Model m) {
		// TODO Auto-generated method stub
		ClassData seq = (ClassData) data;
		LogisticRegression lr = (LogisticRegression) m;
		int numPos = 0, numPredPos = 0, numPredPosCorrect = 0;


		double negLogLikelihood = 0;
		for (int i = 0; i < seq.instances.size(); i++){
			Instance inst = seq.instances.get(i);
			boolean pred = false;
			Prediction prediction = lr.infer.findBestLabel(inst, lr);
			negLogLikelihood -= Math.log(prediction.prob); 
			Label predlabel = prediction.label;
			if (predlabel == inst.label)
			{
				//					pred = true;
				numPredPos++;
			}
			//				if (inst.label == pos)
			//				{
			//					numPos++;
			//					if (pred)
			//						numPredPosCorrect++;
			//				}
			//				System.out.println(predlabel.name);
		}

		//			double prec = (double)numPredPosCorrect / numPredPos, rec = (double) numPredPosCorrect/ numPos;
		//		Debug.println("precision: "+prec+"("+numPredPosCorrect +"/"+ numPredPos+")");
		//		Debug.println("recall: "+rec+"("+numPredPosCorrect +"/"+ numPos+")");
		double acc = (double) numPredPos / seq.instances.size();
		System.out.println("#correct:\t"+numPredPos);
		System.out.println("#total:\t"+seq.instances.size());
		System.out.println("accuracy:\t"+acc);
		System.out.println(String.format("negative log likelihood:\t%1$5.3f", negLogLikelihood));
	}

}
