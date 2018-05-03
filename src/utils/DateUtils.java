package utils;

import java.util.Date;

public class DateUtils {
	public  static int getHour(){
		Date d = new Date();
		return d.getHours();
	}
	
	public static int getMinute(){
		Date d =  new Date();
		return d.getMinutes();
	}

}
