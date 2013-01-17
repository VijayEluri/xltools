package classifier.logreg;

import util.io.Debug;
import abs.ClassData;
import abs.Data;
import abs.Instance;
import classifier.Model;
import classifier.MultiClassPerf;

/**
 * LRLearn Perceptron 
 *  
 */
public class LRPerceptronLearner extends classifier.Learner{
	// if using average perceptron
	public static boolean average = false;
	@Override
	public void learn(Data data, Model m) {
		this.m = m;
        if (m == null)
        {
            Debug.pl("learner doesn't have a model");
            return;
        }
        
        LogisticRegression lr = (LogisticRegression) m;
        ClassData cdata = (ClassData)data;

     // find the index of targetState
//		for (int i = 0; i < lr.all_states.size(); i++){
//			if (lr.all_states.get(i).name.equals(LogisticRegression.targetState))
//			{
//				LogisticRegression.targetIndex = i;
//				break;
//			}
//		}
		
        boolean voted = ((LogisticRegression)m).voted;
        if (!voted){
        	if (m.para ==null)
        	m.para = new LRParameter(lr.labelFactory.all_labels.size()*lr.featureFactory.all_features.size());
        }
        else
        	m.para = new LRVotedParameter(lr.labelFactory.all_labels.size()*lr.featureFactory.all_features.size(), MAX_ITER_NUM);
        
        LRParameter para = (LRParameter)(m.para), avgPara = null;
        if (average){
        	avgPara = new LRParameter(lr.labelFactory.all_labels.size()*lr.featureFactory.all_features.size());
        }

        //loops each iteration, each instances
//        m.tester.testdata = seqdata;//test
        
//        Tester tester = new BinaryTester();
//        Tester tester = new MultiTester();
        
//		double timeInMinutes = timeInSeconds / 60.0
        int lSize = m.labelFactory.all_labels.size();
        for (int idx_iter = 0; idx_iter < MAX_ITER_NUM; idx_iter++)
        {
//        	Debug.pl(idx_iter+"... ");
        	long startBuild = System.currentTimeMillis();
        	if (((LogisticRegression)m).voted){
        		((LogisticRegression)m).voted = false;
//        		tester.test(cdata, m);//test
        		m.predict(cdata, new MultiClassPerf(lSize));
        		((LogisticRegression)m).voted = true;
        	}
        	else{
        		m.predict(cdata, new MultiClassPerf(lSize));//test
        	}
        	long endBuild = System.currentTimeMillis();
    		double timeInSeconds = (double) (endBuild - startBuild) / (double) 1000;
    		Debug.pl("  @"+idx_iter+"th iteration: testing took " + timeInSeconds+" seconds");
//        	tester.test(seqdata, m);//test
        	int F = lr.featureFactory.all_features.size();
            for (int idx_inst = 0; idx_inst < cdata.instances.size(); idx_inst++)
            {
            	// positive  = 1, negative = 0
            	int  pred = -1;
            	if (((LogisticRegression)m).voted){
            		((LogisticRegression)m).voted = false;
            		pred = lr.labelFactory.index((lr.infer).findBestLabel(cdata.instances.get(idx_inst), lr).label);
            		((LogisticRegression)m).voted = true;
            	}
            	else{
            		pred = lr.labelFactory.index((lr.infer).findBestLabel(cdata.instances.get(idx_inst), lr).label);
            	}
            	
            	int truth = lr.labelFactory.index(cdata.instances.get(idx_inst).label);
            	
//                for (Integer state : seqdata.instances.get(idx_inst).states)
//                	if (state == LogisticRegression.targetIndex)
//                	{
//                		truth = 1;
//                		break;
//                	}
            	Instance x = cdata.instances.get(idx_inst);
            	
            	if (pred != truth)
            	{
            			for (int idx_fea = 0; idx_fea < x.featureIndex.size(); idx_fea++){
            				para.lambda[pred * F + x.featureIndex.get(idx_fea)] -= STEP * x.featureValue.get(idx_fea);
            				para.lambda[truth* F + x.featureIndex.get(idx_fea)] += STEP * x.featureValue.get(idx_fea);
            			}
            	}
            	
            }
            if (average){
        		for (int i = 0; i < avgPara.lambda.length; i++){
        			avgPara.lambda[i] += para.lambda[i] / MAX_ITER_NUM;
        		}
        	}
            if (voted){
            	((LRVotedParameter)m.para).pool.add(para.lambda.clone());
            }
        }
        
        if (average){
        	m.para = avgPara;
        }
	}
        public static int MAX_ITER_NUM = 10;
        public static double STEP = 1;
}
