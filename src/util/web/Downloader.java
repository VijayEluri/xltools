package util.web;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Downloader {
	public static void main(String[] args){
		try{
		String data = URLEncoder.encode("q", "UTF-8") + "=" + URLEncoder.encode("Astrodome+opens", "UTF-8");
        data += "&" + URLEncoder.encode("num", "UTF-8") + "=" + URLEncoder.encode("50", "UTF-8");
        data += "&" + URLEncoder.encode("scoring", "UTF-8") + "=" + URLEncoder.encode("t", "UTF-8");

		getInstance().downloadPage("http://www.google.com/archivesearch?num=50&scoring=t&q=Astrodome+opens", "test.html", 0);
		}catch(Exception e){e.printStackTrace();}
	}
	public static Downloader getInstance(){
		if (dl == null){
			dl = new Downloader();
		}
		return dl;
	}
	private static Downloader dl = null;
	public void downloadPage(String link, String output, int to){
		URL url;
		try {
			url = new URL(link);
			HttpURLConnection urlconn=(HttpURLConnection)url.openConnection();    
//	        urlconn.addRequestProperty("Accept-Language", "zh-cn");  
			urlconn.setRequestProperty ( "User-agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.13 (KHTML, like Gecko) Chrome/0.X.Y.Z Safari/525.13");
//			urlconn.setRequestProperty ( "From", "Shawn");
			urlconn.setRequestMethod("GET");
			urlconn.setDoOutput(true);			
	        

	        urlconn.connect();    
	        HttpURLConnection httpconn=(HttpURLConnection)urlconn;    
	  
	        int HttpResult=httpconn.getResponseCode();   
	  
	        while (HttpResult!=HttpURLConnection.HTTP_OK) {
	        	urlconn.disconnect();
	        	System.out.println("waiting 60mins");
	        	Thread.sleep(60*60*1000*3);	        	
	        	urlconn.connect();    
	        	HttpResult=httpconn.getResponseCode();   
	        }
//	        } else {   
//	            String charset=httpconn.getContentType();  
//	            //System.out.println(charset);  
//	            String charType=charset.substring(charset.lastIndexOf("=")+1);  
//	            //System.out.println(charType);  
//	            if("text/html".equals(charType))  
//	                charType="GBK";  
	        InputStreamReader isr = new InputStreamReader(httpconn.getInputStream(),"UTF8");   
	        BufferedReader in = new BufferedReader(isr);   
	        String inputLine;   
//	        if(!SavePath.endsWith("/")) {   
//	        SavePath+="/";   
////	        }   
	        FileOutputStream fout = new FileOutputStream(output);   
	        while ((inputLine = in.readLine()) != null)   
	        {   
	        //System.out.println(inputLine);  
//	        if(inputLine.toLowerCase().equals("<head>"))  
//	        inputLine=inputLine+System.getProperty("line.separator")+baseHref; 
//	        inputLine=inputLine+System.getProperty("line.separator");  
	        fout.write((inputLine+"\n").getBytes( "UTF-8"));   
	        }   
	        in.close();   
	        fout.close();   
			Thread.sleep(to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		try {
//			HttpClient client = new HttpClient();   
//	        client.getHostConfiguration().setHost("www.amazon.com",80,"http");
//	        HttpMethod method = new GetMethod(link);
//	        HttpClientParams params = new HttpClientParams();
//	        params.setParameter("showViewpoints", "0");
//	        params.setParameter("filterBy", "addOneStar");
//	        client.setParams(params);
////	        HttpMethod method = new PostMethod(link);
//	        client.executeMethod(method);
////	        System.out.println(method.getStatusLine());
//	        System.out.println(method.getStatusText());
//	        String text = method.getResponseBodyAsString();
//	        
//	        method.releaseConnection();
//	        return text;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		return null;
	}
}
