package util.ling;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.DateUtil;

public class TarsqiUtil {
	public static void timeAnalyze(String filename, String outputname) {
		try{		

			Process p = Runtime.getRuntime().exec("python /homes/gws/xiaoling/ttk-1.0/code/tarsqi.py timebank "+filename+" "+outputname);
			int exitVal = p.waitFor(); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
/**
 * Get Timex from parsed text
 * @param text
 * @return a list of 3-item array: type, value, text
 */
	public static ArrayList<String[]> getTimeFromText(String text){
		
		// TODO: substring exception : -1
		ArrayList<String[]> result = new ArrayList<String[]>();
		String[] item = null;
		try{
			Process p = Runtime.getRuntime().exec(new String[]{"./testTimex.pl",text});
			int exitVal = p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = reader.readLine();
			Pattern ptn = Pattern.compile("<TIMEX3.*?TYPE=\"(.*?)\"(.*?)>(.*?)</TIMEX3>");
			reader.close();
			boolean prevMonthDate = false;
			int prevEndPos = -1;
			if(line == null)
				System.out.println(text);
			Matcher m = ptn.matcher(line);
			while (m.find()){

				String timeText = m.group(3).replaceAll("<.*?>", "").trim();
				if (!timeText.matches(".*\\d+.*"))
					continue;

				item = null;
				// either month + year or month + date
				if (timeText.matches("\\S+\\s+\\S+")){

					if (m.group(2).length() == 0){
						// Case: Month (more than 1 month away from the current month) Day 

						//						out.print("1*");
						//						String temp = m.group(3).replaceAll("<.*?>", "").trim();
						Date d = null;
						d =  DateUtil.parse(timeText,"MMM d");
						if (d ==null)
							d =  DateUtil.parse(timeText, "MMMM d");
						if (d != null){
							item = new String[3];
							item[0] = m.group(1);
							item[1] = "X"+new SimpleDateFormat("MMdd").format(d);
							item[2] = timeText;
						}
						prevMonthDate = true;
					}
					else if (m.group(2).length()==15){
						// Case: Month (less than 1 month away from the current month) Day
						// len(_VAL="xxxxyyzz") = 15

						//						out.print("2**");
						item = new String[3];
						item[0] = m.group(1);
						try{
							int start = m.group(2).indexOf("\"");
							item[1] = m.group(2).substring(start+1, m.group(2).indexOf("\"", start+1));
//							System.out.print(m.group(2)+"++");
							item[1] = "X"+item[1].substring(4);
						}catch(Exception e){
							System.out.println("substring");
						}
						item[2] =  timeText;
						prevMonthDate = true;
					}
					else{
						// Case: Year Month; len = 13
						item = new String[3];
						item[0] = m.group(1);
//						System.out.print(m.group(2)+"++" + timeText + "Year Month");
						int start = m.group(2).indexOf("\"");
						item[1] = m.group(2).substring(start+1, m.group(2).indexOf("\"", start+1));
						item[2] =  timeText;
						prevMonthDate = false;
					}
				}
				else{
					if (m.group(2).length()==11){
						if (prevMonthDate & m.start()- prevEndPos < 25){
							// Month Day, Year : mis-identified
							item = result.get(result.size()-1);
//							System.out.print(m.group(2)+"++");
							int start = m.group(2).indexOf("\"");
							item[1] = m.group(2).substring(start+1, m.group(2).indexOf("\"", start+1))+ item[1].substring(1);
							item[2] = item[2] + ", "+timeText;
							prevMonthDate = false;
						}else{
							item = new String[3];
							item[0] = m.group(1);
//							System.out.print(m.group(2)+"++" + timeText + "Year");
							int start = m.group(2).indexOf("\"");
							item[1] = m.group(2).substring(start+1, m.group(2).indexOf("\"", start+1));
							item[2] =  timeText;
							prevMonthDate = false;
						}
					}
					else{
						item = new String[3];
						item[0] = m.group(1);
//						System.out.print(m.group(2)+"++"+ timeText + "Full");
						int start = m.group(2).indexOf("\"");
						item[1] = m.group(2).substring(start+1, m.group(2).indexOf("\"", start+1));
						item[2] = timeText;
					}

				}
				prevEndPos = m.end();
				if (item != null){
					result.add(item);
				}
//				System.out.println();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	public static void main(String[] args){
//		String text = "<lex pos=\"IN\">In</lex> <lex pos=\"DET\">a</lex> <lex pos=\"JJ\">blinding</lex> <lex pos=\"NN\">snowstorm</lex> <lex pos=\"PPC\">,</lex> <lex pos=\"NNP\">Henry</lex> <lex pos=\"NNP\">V</lex> <lex pos=\"VBD\">was</lex> <lex pos=\"VBN\">crowned</lex> <lex pos=\"NNP\">King</lex> <lex pos=\"IN\">of</lex> <lex pos=\"NNP\">England</lex> <TIMEX3 tid=\"t1\" TYPE=\"DATE\" VAL=\"20090519\"><lex pos=\"NN\">yesterday</lex></TIMEX3> <lex pos=\"PP\">.</lex>";
//		String text = "<lex pos=\"IN\">In</lex>";
//		Pattern ptn = Pattern.compile("<TIMEX3.*?TYPE=\"(.*?)\" VAL=\"(.*?)\">(.*?)</TIMEX3>");
//		Matcher m = ptn.matcher(text);
//		if (m.find())
//			System.out.println(m.group(2));
//		System.out.println("Apr 14".matches(".*\\d+.*"));
		
		getTimeFromText("In a blinding snowstorm, Henry V was crowned King of England in April 1413.");
	}

	public static void makeTimeBankFile(String line, String tmpfile) {
		try{
			PrintWriter pw = new PrintWriter(new FileWriter(tmpfile));
			pw.println("<TimeML><DOCNO>1</DOCNO><TEXT>");
			pw.println(line);
			pw.println("</TEXT></TimeML>");
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
