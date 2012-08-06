package abs.ontology;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * preprocessing class for NELL 
 * @author xiaoling
 *
 */
public class NELLReader {

	
	public static int readCategories() throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader("data/NELL/catogories.htm"));
		String line = null;
		int numClasses = 0;
		while ((line = reader.readLine())!=null){
			if (line .startsWith("<ul")){
				numClasses = processCatLine(line);
			}
		}
		reader.close();
		System.out.println(numClasses +" classes in total");
		return numClasses;
	}
	
	private static int processCatLine(String line){
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Integer> parents = new ArrayList<Integer>(), levels = new ArrayList<Integer>();
		Pattern tag = Pattern.compile("<(.+?)>");
		Matcher m = tag.matcher(line);
		int previousIdx = -1, parentIdx = -1, level = -1;
		
		while (m.find()){
			String text = m.group(1);
			if (text.equals("ul")){
				parentIdx = previousIdx;
				level ++;
			}
			else if (text.equals("/ul")){
				level --;
				previousIdx = parents.get(parentIdx);
			}
			else if (text.startsWith("a name")){
				names .add(text.substring(8, text.length() -1));
				parents.add(parentIdx);
				levels.add(level);
				previousIdx = names.size() -1;
			}
		}
		for (int i = 0; i < names.size(); i++){
			int indent = levels.get(i);
			while (indent -- > 0){
				System.out.print("=");
			}
			System.out.println(names.get(i)+"::level"+levels.get(i));
		}
		return names.size();
	}
	
	public static void readRelations(){
		
	}
	
	public static final String categoryUrl = "rtw.ml.cmu.edu/rtw/kbbrowser/ontology.php?mode=cat";
	public static final String relationUrl = "rtw.ml.cmu.edu/rtw/kbbrowser/ontology.php?mode=rel";
	
}
