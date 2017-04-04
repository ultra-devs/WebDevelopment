package com.dev.util.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {

	public static String getTwoWeekOldDate(String dataFormat) {

         String defaultFormat="yyyy-MM-dd";
		if(dataFormat!=null)
        {
        	//usedefault
			defaultFormat=dataFormat;
        }
		DateFormat dateFormat = new SimpleDateFormat(defaultFormat);

		Date date = new Date();
		String todate = dateFormat.format(date);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -14);
		Date todate1 = cal.getTime();
		System.out.println();
		return dateFormat.format(todate1);

	}
	
	public static String convertDataFormat(String inputFormat,String outPutFormat,String dateString) throws ParseException
	{
		String outformatted=null;
	     System.out.println(inputFormat+":"+outPutFormat);
		DateFormat inputDateFormat=new SimpleDateFormat(inputFormat);
		Date inputDate=inputDateFormat.parse(dateString);
		DateFormat outputDateFormat = new SimpleDateFormat(outPutFormat);
		outformatted=outputDateFormat.format(inputDate); 
		return outformatted;
		
		
		
		
		
	}
}
