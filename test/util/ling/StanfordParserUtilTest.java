package util.ling;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import edu.stanford.nlp.trees.Tree;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;

public class StanfordParserUtilTest extends TestCase {
	public void testFindMentions(){
		String parse = "(ROOT (S (NP (NP (DT The) (NN crew)) (PP (IN of) (NP (NNP Royal) (NNP Australian) (NNP Navy) (NN frigate) (NNP HMAS) ('' '') (NNP Canberra))) ('' '')) (VP (VBD had) (NP (DT the) (NN association)) (PP (IN with) (NP (DT the) (NN song))) (SBAR (IN after) (S (NP (PRP they)) (VP (VBD adopted) (NP (PRP it)) (PP (IN as) (NP (PRP$ their) (`` `) (NN anthem) ('' '))))))) (. .)))";
		Tree t;
		StringBuilder sb = new StringBuilder();
		try {
			t = StanfordParserUtil.tf.newTreeReader(new StringReader(parse)).readTree();
			List<Tree> list = StanfordParserUtil.findMentions(t);
			List<Tree> leaves = t.getLeaves();
			TIntIntMap map = new TIntIntHashMap();
			int i = 0;
			for (Iterator<Tree> it = leaves.iterator(); it.hasNext();){
				map.put( it.next().nodeNumber(t), i++);
			}
			for (Tree li:list){
				
				for (Tree tr:li.getLeaves()){
					System.out.println(tr.parent(t)+" "+map.get(tr.nodeNumber(t)) );
					
				}
				Tree head = StanfordParserUtil.getHeadNode(li);
				
				System.out.println(li.getLeaves()+ " HEAD:"+head+"@"+map.get(head.nodeNumber(t)));
				System.out.println("========");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
