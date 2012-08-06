package misc.kaixin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.io.FileUtil;
import util.web.Downloader;

public class Kitchen {
	public static void main(String[] args) throws IOException, Exception{
		Writer out = new OutputStreamWriter((new FileOutputStream("out.txt")), "UTF-8");
		String arg = "cook";
		String[] list = FileUtil.getTextFromFile("orderid.list").split("\n");
		String url = "";/*"http://wap.kaixin001.com/cafe/cook.php?cafeid=6439572&orderid=280212324&dishid=5&clean=1&verify=8732139_8732139_1269197556_1a4b47f58bb8b4632eb5cb02d513c20d_kx";*/
		if (arg.equals(STATUS)){
			Downloader.getInstance().downloadPage(url, "temp.html", 1000);
			Pattern p = Pattern.compile("<div class=\"list\">([^<]+?)<br /><font color=\"#999999\">(.+?)</font></div>");
			String text = FileUtil.getTextFromFile("temp.html");
			Matcher m = p.matcher(text);
			while (m.find()){
				out.write(m.group(1)+"\t"+m.group(2)+"\n");
			}
			out.close();
		}else if (arg.equals(COOK)){
//			Thread.sleep(16*60000);
			Random r = new Random(918);
			while (true){
				System.out.println("Start");
				for (String line: list){
					if (line.startsWith("//"))
						continue;
					System.out.println(line);
					//dish 2 counter
					Downloader.getInstance().downloadPage(dish2counter.replace("$1", foodnum+"").replace("$2", line), "temp.html", 500+r.nextInt(2000));

					// cook
					Downloader.getInstance().downloadPage(cook.replace("$1", line).replace("$2", dishID), "temp.html", 500+r.nextInt(2000));
				}

				Thread.sleep(r.nextInt(10*60000)+30*60000);
			}
		}
	}
	
	static String dish2counter = "http://wap.kaixin001.com/cafe/dish2counter.php?foodnum=$1&cafeid=6439572&orderid=$2&verify=8732139_8732139_1269197333_3ae1f32126db56db2e60c34b31c88079_kx&url=%2Fcafe%2Findex.php%3Fverify%3D8732139_8732139_1269194058_27417013cbd07c55894415ca102fbac7_kx";
	static String cook = "http://wap.kaixin001.com/cafe/cook.php?cafeid=6439572&orderid=$1&dishid=$2&clean=1&verify=8732139_8732139_1269197556_1a4b47f58bb8b4632eb5cb02d513c20d_kx";
	static int foodnum = 60;
	static String dishID = "64"; 
	
	static String STATUS = "status";
	static String COOK = "cook";
	static String CLEAN = "status";
	
}
