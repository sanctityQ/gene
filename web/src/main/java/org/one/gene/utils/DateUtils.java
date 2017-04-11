package org.one.gene.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.jexl.parser.ParseException;


public class DateUtils {

	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	* 获取星期的第一天日期
	* @param date
	* @return
	* @throws ParseException
	 * @throws java.text.ParseException 
	 * @throws Exception 
	*/
	public static String getFirstWeekDate(String date) throws ParseException, Exception{
        Calendar cal = Calendar.getInstance();  
        Date time= dateFormat.parse(date);
        cal.setTime(time);  
        System.out.println("要计算日期为:"+dateFormat.format(cal.getTime())); //输出要计算日期  
        
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
        if(1 == dayWeek) {  
           cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
        
       cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
       
       int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
       cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值   
       System.out.println("所在周星期一的日期："+dateFormat.format(cal.getTime()));
		return dateFormat.format(cal.getTime());
	}
	
	/**
	* 获取星期的第七天日期
	* @param date
	* @return
	* @throws ParseException
	 * @throws java.text.ParseException 
	 * @throws Exception 
	*/
	public static String getLastWeekDate(String date) throws ParseException, Exception{
        Calendar cal = Calendar.getInstance();  
        Date time= dateFormat.parse(date);
        cal.setTime(time);  
        System.out.println("要计算日期为:"+dateFormat.format(cal.getTime())); //输出要计算日期  
        
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
        if(1 == dayWeek) {  
           cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
        
       cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
       
       int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
       cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值   
       
       cal.add(Calendar.DATE, 6);
       System.out.println("所在周星期日的日期："+dateFormat.format(cal.getTime()));  
       
		return dateFormat.format(cal.getTime());
	}
	
	/**
	* 获取日期年份
	* @param date
	* @return
	* @throws ParseException
	 * @throws java.text.ParseException 
	*/
	public static int getYear(String date) throws ParseException, Exception{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFormat.parse(date));
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	* 获取日期月份
	* @param date
	* @return
	* @throws ParseException
	 * @throws java.text.ParseException 
	*/
	public static int getMonth(String date) throws ParseException, Exception{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFormat.parse(date));
		return (calendar.get(Calendar.MONTH) + 1);
	}
	
	/**
	* 获取日期号
	* @param date
	* @return
	* @throws ParseException
	 * @throws java.text.ParseException 
	*/
	public static int getDay(String date) throws ParseException, Exception{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFormat.parse(date));
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	/**
	* 获取月份起始日期
	* @param date
	* @return
	* @throws ParseException
	 * @throws java.text.ParseException 
	 * @throws Exception 
	*/
	public static String getMinMonthDate(String date) throws ParseException, Exception{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFormat.parse(date));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return dateFormat.format(calendar.getTime());
	}
	
	/**
	* 获取月份最后日期
	* @param date
	* @return
	* @throws ParseException
	 * @throws Exception 
	*/
	public static String getMaxMonthDate(String date) throws ParseException, Exception{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFormat.parse(date));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return dateFormat.format(calendar.getTime());
	}


}
