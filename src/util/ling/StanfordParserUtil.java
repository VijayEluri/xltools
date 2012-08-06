package util.ling;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import util.io.FileUtil;
import edu.stanford.nlp.trees.CollinsHeadFinder;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.HeadFinder;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.trees.PennTreeReader;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreeReader;
import edu.stanford.nlp.trees.TreeReaderFactory;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.tregex.ParseException;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.trees.tregex.tsurgeon.Tsurgeon;
import edu.stanford.nlp.trees.tregex.tsurgeon.TsurgeonPattern;
import edu.stanford.nlp.util.Pair;

public class StanfordParserUtil {
	public static void main(String[] args){
		generateDependency("test.parse", "test.dep");
	}
	public static TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	public static GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	public static TreePrint tp = new TreePrint("typedDependenciesCollapsed");
	// The fancy footwork here is to keep -TMP functional tags if present.
	// If not available, you could just use the no-argument constructor.
	public static TreeReaderFactory tf = new TreeReaderFactory() {
		public TreeReader newTreeReader(Reader in) {
			return new PennTreeReader(in, new LabeledScoredTreeFactory()/*,
					new NPTmpRetainingTreeNormalizer()*/);
		}};
	public static HeadFinder headFinder = new CollinsHeadFinder();
	public static void generateDependency(String inputFile, String outputFile){
		/** Convert Penn Treebank parse trees in a file to typed dependencies
		 *  (collapsed).  It does it two different ways for pedagogical reasons.
		 *  This is for English, but is easy to generalize. 
		 *  Usage: java TypedDependenciesDemo filename
		 */
		String filename = inputFile;

		
			//		Treebank tb = new DiskTreebank();
			//		tb.loadPath(filename);
			
			
			
			StringBuilder sb = new StringBuilder();
			//		for (Tree t : tb) {
			String[] lines = FileUtil.getTextFromFile(filename).split("\n");

			for (int i = 0; i < lines.length; i++){
				try{
					Tree t = tf.newTreeReader(new StringReader(lines[i])).readTree();
					GrammaticalStructure gs = gsf.newGrammaticalStructure(t);

					Iterator<TypedDependency> it = gs.typedDependenciesCollapsed().iterator();
					while (it.hasNext()){
						sb.append(it.next()+"\t");
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				sb.append("\n");
			}

			//			tp.printTree(t);
			//			System.out.println("----------");

			FileUtil.writeTextToFile(sb.toString(), outputFile);
	}
	
	public static String getDep(String parse){
		Tree t;
		StringBuilder sb = new StringBuilder();
		try {
			t = tf.newTreeReader(new StringReader(parse)).readTree();
			GrammaticalStructure gs = gsf.newGrammaticalStructure(t);
			Iterator<TypedDependency> it = gs.typedDependenciesCollapsed().iterator();
			while (it.hasNext()){
				sb.append(it.next()+"\t");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public static List<Tree> findMentions(Tree root){
		List<Tree> res = new ArrayList<Tree>();

		String patS = "NP !>># NP"; //needs to be the maximum projection of a head word, or a conjunction

		try {
			TregexPattern pat;
			pat = TregexPattern.compile(patS);
			TregexMatcher matcher = pat.matcher(root);
			while (matcher.find()) {
				Tree t =  matcher.getMatch();
				if(t.numChildren() == 0) continue; //added to handle when NP is a word (i.e., terminal/leaf node)
				res.add(matcher.getMatch());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return res;
	}
	
	public static Tree getHeadNode(Tree node){
		Tree res = node.headTerminal(headFinder);
		String yield = res.yield().toString();
		
		if(yield.equals("'s")){
			Tree copy = node.deeperCopy();
			List<Pair<TregexPattern, TsurgeonPattern>> ops = new ArrayList<Pair<TregexPattern, TsurgeonPattern>>();
			List<TsurgeonPattern> ps = new ArrayList<TsurgeonPattern>();
			TregexPattern matchPattern;
			try {
				matchPattern = TregexPattern.compile("POS=pos");
				ps.add(Tsurgeon.parseOperation("prune pos"));
				TsurgeonPattern p = Tsurgeon.collectOperations(ps);
				ops.add(new Pair<TregexPattern,TsurgeonPattern>(matchPattern,p));
				Tsurgeon.processPatternsOnTree(ops, copy);

				res = copy.headTerminal(headFinder);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		return res;

	}
}
