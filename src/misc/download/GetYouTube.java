package misc.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * 
 * @author xiaoling
 *
 */
public class GetYouTube {
	public static void main(String[] args) throws Exception{
//		GetYouTube.ParseVideoPlayList("view_play_list.htm");
		GetYouTube.ParseVideoLink("convex");
	}
	
	public static void ParseVideoLink(String folder) throws Exception{
		String[] list = new File(folder).list();
		for (String f : list){
			BufferedReader reader= new BufferedReader(new FileReader(folder+"\\"+f));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine())!=null){
				sb.append(line);
			}
			reader.close();
			String text = sb.toString();
			int start = text.indexOf("HREF")+6;
			StringBuffer link = new StringBuffer();
			while (text.charAt(start)!='\'')
				link.append(text.charAt(start++));
			System.out.println(link.toString());
		}
	}
	
	public static void ParseVideoPlayList(String filename) throws Exception{
		BufferedReader reader= new BufferedReader(new FileReader(filename));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = reader.readLine())!=null){
			sb.append(line);
		}
		reader.close();
		String text = sb.toString();
		int start = 0;
		PrintWriter writer = new PrintWriter(new FileWriter("output.txt"));
		while ((start = text.indexOf(LINK_CUE, start+1))!=-1){
			int linkStart = text.indexOf(LINK_PTN, start)+9; //start from "/watch"
			StringBuffer link = new StringBuffer();
			while (text.charAt(linkStart)!='&')
				link.append(text.charAt(linkStart++));
			writer.println(link.toString());
		}		
		writer.close();
	}
	
	public static final String LINK_CUE = "<div class=\"title\">";
	public static final String LINK_PTN = "<a href=";
}
