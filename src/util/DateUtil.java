package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static Date parse(String s, String ptn){
		Date r = null;
		try{
			r = new SimpleDateFormat(ptn).parse(s);
		}catch(ParseException e){
			return null;
		}
		return r;
	}
	
	public static Date parse(String s, SimpleDateFormat format){
		Date r = null;
		try{
			r = format.parse(s);
		}catch(ParseException e){
			return null;
		}
		return r;
	}
}
