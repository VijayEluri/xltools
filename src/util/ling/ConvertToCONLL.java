package util.ling;

import java.util.ArrayList;

public class ConvertToCONLL {
	/**
	 * 
	 * @param syn
	 * @param dep
	 * @param format
	 * @return
	 */
	public static String convertToCONLL(String syn, String dep, String format){
		if (format.equals("conll2008")){
			return convertToCONLL08(syn, dep);
		}
		else{
			System.err.println("format not supported!");
			return null;
		}
	}

	public static String convertToCONLL08(String syn, String dep) {
		ArrayList<String > words = getWordsFromSyn(syn );
		String[] heads = new String[words.size()], deps = new String[words.size()] ;
		
		
		StringBuilder sb = new StringBuilder(); 
		return null;
	}

	private static ArrayList<String> getWordsFromSyn(String syn) {
		ArrayList<String> list = new ArrayList<String>();
		String[] items = syn.split("\\s+");
		for (int i = 0; i < items.length; i++){
			if (!items[i].startsWith("("))
				list.add(items[i]);
		}
		
		return list;
	}
}
