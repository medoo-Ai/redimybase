/* **************************************************************
 *
 * 文件名称：DateUtil.java
 *
 * 包含类名：com.icloudpower.cpmobile.dto.umc
 * 创建日期：2014-1-21
 * 创建作者：yangming
 * 版权声明：Copyright 2014 大唐云动力科技股份有限公司 保留所有权利。
 *
 * **************************************************************/
package com.aispread.manager.leave.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Title 时间处理帮助类
 * @Description 用于时间 处理的
 * @Package com.icloudpower.cpmobile.dto.umc
 * @ClassName DateUtil
 * @author yangming
 * @date 2014-1-13
 * @version V1.0
 */
public class DateUtil {

	/** 日志 */
	@SuppressWarnings("unused")
	private Log logger = LogFactory.getLog(this.getClass());

	public static String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public static String DATE_FORMAT_YMD_ = "yyyy-MM-dd";
	
	public static String DATE_FORMAT_YMD = "yyyyMMdd";
	public static String DATE_FORMAT_Y = "yyyy";
	
	public static final float MONTH_MILLI = (float) (30.41 * 24 * 60 * 60 * 1000);
	
	public static final long DAY_MILLI = 24 * 60 * 60 * 1000; // 一天的MilliSecond

	/**
	 * @Title 时间字符串[格式必须 为： yyyy-mm-dd hh:mm:ss]转 Timestamp
	 * @Description 时间字符串[格式必须 为： yyyy-mm-dd hh:mm:ss]转 Timestamp
	 * @param datestr
	 *            时间字符串
	 * @param datestr
	 *            时间字符串格式
	 * @return Timestamp 返回TimeStamp类型的时间
	 */
	public static Timestamp dateStrToTime(String datestr) {
		return Timestamp.valueOf(datestr);
	}
	
	/**
	 * Description: 格式化成时间
	 * @Version 1.0 May 9, 2016 4:10:20 PM 王斌(wangb@unimlink.com) 创建
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String str) throws ParseException{
		DateFormat format = new SimpleDateFormat(DATE_FORMAT_YMDHMS);
		return format.parse(str);
	}
	
	/**
	 * Description: 格式化成时间
	 * @Version 1.0 May 9, 2016 4:10:20 PM 王斌(wangb@unimlink.com) 创建
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String str,String formater) throws ParseException{
		DateFormat format = new SimpleDateFormat(formater);
		return format.parse(str);
	}
	
	
	/**
	 * Description: 取得两个日期之间的月数
	 * @Version 1.0 May 9, 2016 3:59:50 PM 王斌(wangb@unimlink.com) 创建
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static float monthBetween(Date t1,Date t2){
		float temptime = (t1.getTime() - t2.getTime());
		return  temptime / MONTH_MILLI;
	} 
	
	/**
	 * 格式化时间 返回yyyy-MM-dd HH:mm:ss
	 */
	public static String parseDateToString(Date date) {
		SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT_YMDHMS);
		return dateformat.format(date);
	}
	/**
	 * Description: 格式化时间
	 * @Version 1.0 2016-5-25 上午10:16:34 王斌(wangb@unimlink.com) 创建
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static String parseDateToString2(Date str){
		DateFormat format = new SimpleDateFormat(DATE_FORMAT_YMD_);
		return format.format(str);
	}
	
	/**
	 * 获取年yyyy
	 * TODO
	 * @param str
	 * @return
	 */
	public static String parseDateToString3(Date str){
		DateFormat format = new SimpleDateFormat(DATE_FORMAT_Y);
		return format.format(str);
	}
	
	/**
	 * Description: 取得两个日期之间的天数
	 * @Version 1.0 May 9, 2016 4:44:46 PM 王斌(wangb@unimlink.com) 创建
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static float dayBetween(Date t1,Date t2){
		float temptime = (t1.getTime() - t2.getTime());
		return  temptime / DAY_MILLI;
	} 
	//yyyy-MM-dd HH:mm:ss
	public static void main(String[] args) {
		String str = "2016-2-3 8:20:25";
		String str1 = "2016-05-13 02:35";
		String str2 = "2016/5/15 18:12:59";
		String str3 = "2016/5/15 18:12";
		
		try {
			System.out.println(dateStrToDate(str));
			System.out.println(dateStrToDate(str1));
			System.out.println(dateStrToDate(str2));
			System.out.println(dateStrToDate(str3));
			
			Date date = addDay(dateStrToDate(str3), -3);
//			Date date = addMonth(dateStrToDate(str3), -3);
			System.out.println(parseDateToString2(date));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Description: 根据类型获取时间，年月日
	 * @Version 1.0 2016-5-24 下午7:59:40 王斌(wangb@unimlink.com) 创建
	 * @param type
	 * @return
	 */
	public static int getDateByType(Date date,int type){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(type);
	}
	
	/**
	 * Description: 当前时间推后天小时
	 * @Version 1.0 2016年3月2日 下午5:15:51 王斌(wangb@unimlink.com) 创建
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDay(Date date,int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}
	
	/**
	 * Description: 当前时间推后几月
	 * @Version 1.0 2016-1-26 下午02:59:00 王斌(wangb@unimlink.com) 创建
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date addMonth(Date date,int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MARCH,month);
		return cal.getTime();
	}
	
	/**
	 * Description: 字符串转换时间(验证并判断格式)
	 * @Version 1.0 2016-5-16 下午2:51:49 王斌(wangb@unimlink.com) 创建
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date dateStrToDate(String str) throws ParseException{
		String formatstr = "yyyy-MM-dd ";
		str = str.replaceAll("/", "-");
		formatstr += (str.length() > 16)?"HH:mm:ss":"HH:mm";
		DateFormat format = new SimpleDateFormat(formatstr);
		return format.parse(str);
	}
	
	
	/**
	 * @Title Date转 Timestamp
	 * @Description Date转 Timestamp
	 * @param date
	 *            Date 时间
	 * @return Timestamp 返回TimeStamp类型的时间
	 */
	public static Timestamp dateToTime(Date date) {
		return new Timestamp(date.getTime());
	}

	/**
	 * @Title Timestamp转 String
	 * @Description Timestamp转 String
	 * @param timestamp
	 *            Timestamp 时间
	 * @return Timestamp 返回String类型的时间 [格式为 yyyy-mm-dd hh:mm:ss]
	 */
	public static String timestampToDateStr(Timestamp timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT_YMDHMS);
		return sdf.format(timestamp);
	}

	/**
	 * @Title Timestamp转 Date
	 * @Description Timestamp转 Date
	 * @param timestamp
	 *            Date 时间
	 * @return Timestamp 返回Date类型的时间
	 */
	public static Date timeStampToDate(Timestamp timestamp) {
		Date date = new Date();
		date = timestamp;
		return date;
	}
	
	
	public static Timestamp now() {
		return DateUtil.dateToTime(new Date());
	}
	

	/**
	 * 
	 * 
	 * @Title 获得当前的系统时间
	 * @Description
	 * @param
	 * @return
	 * @throws
	 */
	public static String getSystemTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT_YMDHMS);
		return sdf.format(new Date());
	}
	
	public static String getSystemDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT_YMD);
		return sdf.format(new Date());
	}

	/**
	 * 将Date转化为 DATE_FORMAT_YMDHMS格式的字符串
	 * @param date
	 * @return
	 */
	public static String dateToDateStr(Date date) {
		return timestampToDateStr(dateToTime(date));
	}

	/**
	 * 
	 * 
	 * @Title 给当前时间延长多少秒
	 * @Description
	 * @param
	 * @return
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	public static Date getDateAddSeconds(Date date, int seconds) {
		date.setSeconds(date.getSeconds() + seconds);
		return date;
	}
	
	public static String timestampToDateStr(Timestamp timestamp,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(timestamp);
	}
	
	/**
	 * 把yyyy-MM-dd格式的字符串转换成Timestamp
	 * 
	 * @param dateStr
	 * @return Timestamp
	 */
	public final static Timestamp getTimeOfDateStr(String dateStr) {
		if(StringUtils.isEmpty(dateStr))
			return null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp time = null;
		try {
			Date da = df.parse(dateStr);
			time = new Timestamp(da.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;

	}
	/**
	 * Description: 格式化时间 返回自定义格式
	 * @Version 1.0 2015-9-7 下午2:23:49 王斌(wangb@unimlink.com) 创建
	 * @param str
	 * @return
	 */
	public static String parseTimestampToString(String str,String formatStr) {
		if(StringUtils.isBlank(str)){
			return null;
		}
		if(StringUtils.isBlank(formatStr)){
			formatStr = "yyyy-MM-dd HH:mm:ss";
		}
		try {
			DateFormat format = new SimpleDateFormat(formatStr);
			Date date = format.parse(str);
			return format.format(date);
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
}
