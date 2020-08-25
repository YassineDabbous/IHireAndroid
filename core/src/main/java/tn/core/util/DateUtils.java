package tn.core.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;


import tn.core.util.time.TimeAgo;

public class DateUtils {


	public static String prettyDate (String date) throws Exception
	{
		SimpleDateFormat sdfmt1 = new SimpleDateFormat("yy-MM-dd");
		SimpleDateFormat sdfmt2= new SimpleDateFormat("dd MMM yyyy");
		Date dDate = sdfmt1.parse( date );
		String strOutput = sdfmt2.format( dDate );
		return  strOutput;
	}


	public static String timeToPretty(String time)
	{
		SimpleDateFormat f = new SimpleDateFormat("dd MMM yyyy");
		Date date = new Date();
		try {
			date.setTime(Long.parseLong(time));
			return  f.format(date);
		}catch (NumberFormatException e){
			Log.w("NumberFormatException" , time);
			return  f.format(date);
		}
	}

	public static String getTimeAgo(String date){
		if(!date.isEmpty())
			return (new TimeAgo()).timeAgo(Long.parseLong(date));
		else
			return "";
	}
	/*public static String getTimeAgo(long date){
		return (new TimeAgo()).timeAgo(date, Action.time);
	}*/

}
