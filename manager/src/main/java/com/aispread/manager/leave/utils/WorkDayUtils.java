package com.aispread.manager.leave.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 本类是一个工作日计算类
 * 本类负责:1计算某个时间点加上相应的工作日后就会生成一个非假期的工作日期
 *       2计算两个日期的具体工作日
 *       3计算工作的小时数（排除双休节假日，涵盖了周末上班情况）
 * @author   刘强
 * @version  Ver 1.0 2018-06-29 改订  
 *
 */

public class WorkDayUtils {
	
	/**
	 * 此方法计算某个日期加上几个工作日后的一个工作日期(除周末)
	 * @param 	date(起始日期) , day(要添加的工作天数)
	 * @return	incomeDate(去除周末后的日期)
	 */
	public Date getIncomeDate(Date date,int days) throws NullPointerException{
	    Date incomeDate = date;
	    for(int i = 1 ; i <= days ; i++){
	    	incomeDate = getIncomeDate(incomeDate);
	    }
	    return incomeDate;
	}
	
	/**
	 * 此方法计算某个日期加上几个工作日后的一个工作日期(除周末和法定节假日)
	 * @param 	date(起始日期) , day(要添加的工作天数)
	 * @return	incomeDate(去除周末后的日期)
	 */
	public Date getIncomeDatePlus(Date date,int days) throws NullPointerException{
	    Date incomeDate = date;
	    for(int i = 1 ; i <= days ; i++){
	    	incomeDate = getIncomeDatePlus(incomeDate);
	    }

	    return incomeDate;
	}
	
	/**
	 * 此方法计算某个日期后一天的工作日期(除周末)
	 * @param 	date(起始日期)
	 * @return	incomeDate(去除周末后的日期)
	 */
	private Date getIncomeDate(Date date) throws NullPointerException{
	    if (null == date){
	        throw new NullPointerException("the date is null or empty!");
	    }
	 
	    //对日期的操作,我们需要使用 Calendar 对象
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(date);
	 
	    //+1天
	    calendar.add(Calendar.DAY_OF_MONTH, +1);
	 
	    //判断是星期几
	    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
	 
	    Date incomeDate = calendar.getTime();
	    if (dayOfWeek == 1 || dayOfWeek == 7){
	        //递归
	        return getIncomeDate(incomeDate);
	    }
	    return incomeDate;
	}
	
	/**
	 * 此方法计算某个日期后一天的工作日期(除周末和法定节假日)
	 * @param 	date(起始日期)
	 * @return	incomeDate(去除周末后的日期)
	 */
	public Date getIncomeDatePlus(Date date) throws NullPointerException{
	    if (null == date){
	        throw new NullPointerException("the date is null or empty!");
	    }
	 
	    //对日期的操作,我们需要使用 Calendar 对象
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(date);
	 
	    //+1天
	    calendar.add(Calendar.DAY_OF_MONTH, +1);
	 
	    Date incomeDate = calendar.getTime();
	 
	    if (isWeekend(calendar) || isHoliday(calendar)){
	        //递归
	        return getIncomeDatePlus(incomeDate);
	    }
	    return incomeDate;
	}
	/**
	 * 此方法计算两个日期的真实工作日(除周末和法定节假日)
	 * @param 	startTime(起始日期),endTime(结束时间)
	 * @return	workdays(去除周末和法定节假日后的日期)
	 */
	public int getWorkDays(Date startTime,Date endTime){
		return getDatesBetweenTwoDate(startTime,endTime).size();
	}
	/**
	 * 计算工时（小时）
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public int getWorkHours(Date startTime,Date endTime){
		double workHour = 0;
		DecimalFormat df = new DecimalFormat("######0");
		int workDays = getDatesBetweenTwoDate(startTime,endTime).size();
		if(workDays==1){
			double hour = this.calHourBetweenTwoDate(startTime,endTime);
			if(hour<7){
				workHour = Integer.parseInt(df.format(hour));
			}else if(hour>=7){
				workHour = 7;
			}
		}else if(workDays>=2){
			ResourceBundle resourceBundle = ResourceBundle.getBundle("WorkDayPlusConfig");
			String goOffWorkTime = "";
			if(isSummerTime(endTime)){
				goOffWorkTime=resourceBundle.getString("SummerGoOffWorkTime");
			}else if(!isSummerTime(endTime)){
				goOffWorkTime=resourceBundle.getString("WinterGoOffWorkTime");
			}
			
			double lastDay = this.calHourBetweenTwoDate(this.chang2TodayTime(resourceBundle.getString("WorkStartTime"),endTime),endTime);
			double firstDay = this.calHourBetweenTwoDate(startTime,this.chang2TodayTime(goOffWorkTime,startTime));
			if(firstDay>7){
				firstDay=7;
			}
			if(lastDay>7){
				lastDay=7;
			}
			
			workHour = Integer.parseInt(df.format(firstDay+lastDay+(workDays-2)*7));
			
			
		}
		if(workHour<=1){
			workHour=1;
		}
		return (int)workHour;
	}
	/**
	 * 判断一个日期是否是夏季时间
	 * @param date
	 * @return
	 */
	public boolean isSummerTime(Date date){
		ResourceBundle resourceBundle = ResourceBundle.getBundle("WorkDayPlusConfig");
		Calendar SummerbeginTime = String2Calendar(resourceBundle.getString("SummerBeginTime"));
		Calendar WinterBeginTime = String2Calendar(resourceBundle.getString("WinterBeginTime"));
		Calendar caldate = Calendar.getInstance();
		caldate.setTime(date);

        if (caldate.after(SummerbeginTime) && caldate.before(WinterBeginTime)) {
            return true;
        } else {
            return false;
        }
	}
	/**
	 * String转Calendar
	 * @param sdate
	 * @return
	 */
	public Calendar String2Calendar(String sdate){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		Date cdate = null;
		try {
			cdate = sdf.parse(sdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cdate);
		return calendar;
	}
	/**
	 * 计算两个时间差（小时）
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public double calHourBetweenTwoDate(Date startTime,Date endTime){
		long nh = 1000 * 60 * 60;
		long diff = endTime.getTime() - startTime.getTime();
		double hour = (double)diff/nh;
		return hour;
	}

	/**
	 * 生成一个上下班时间，因为夏季冬季不一样
	 * @param timeStr
	 * @param sedate
	 * @return
	 */
	private Date chang2TodayTime(String timeStr,Date sedate){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		String dateStr=sdf.format(sedate);
		String date = dateStr + " " + timeStr;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datetime = null;
		try {
			datetime= df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datetime;
	}
	/**
	 * 判断一个日期是不是周末.
	 * @param 	 calendar (要判断的日期)
	 * @return	Boolean(返回true为是周末)
	 */
	private boolean isWeekend(Calendar calendar){
	    //判断是星期几
	    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
	 
	    if (dayOfWeek == 1 || dayOfWeek == 7){
	    	if(isWeekendWorkDays(calendar)){
	    		return false;
	    	}else{
	    		return true;
	    	}
	        
	    }
	    return false;
	}
	 
	/**
	 * 一个日历是不是法定节假日
	 * WorkDayPlusConfig.properties为配置文件，其中存放了法定节假日期，格式为：holiday=2018-10-1,...,..
	 * @param 	 calendar (要判断的日期)
	 * @return	Boolean(返回true为是法定节假日)
	 */
	private boolean isHoliday(Calendar calendar){
	    String pattern = "yyyy-MM-dd";
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	    String dateString = simpleDateFormat.format(calendar.getTime());
	 
	    //节假日 这个可能不同地区,不同年份 都有可能不一样,所以需要有个地方配置, 可以放数据库, 配置文件,环境变量 等等地方
	    //这里以配置文件 为例子
	    ResourceBundle resourceBundle = ResourceBundle.getBundle("WorkDayPlusConfig");
	    String holidays = resourceBundle.getString("holiday");
	 
	    String[] holidayArray = holidays.split(",");
	 
	    boolean isHoliday = ArrayUtils.contains(holidayArray, dateString);
	    return isHoliday;
	}
	
	/**
	 * 一个日历是不是法定要工作的周末
	 *WorkDayPlusConfig.properties为配置文件，其中存放了法定节假日期，格式为：WeekendWorkDays=2018-9-29,...,..
	 * @param 	 calendar (要判断的日期)
	 * @return	Boolean(返回true为确实是要工作的周末)
	 */
	private boolean isWeekendWorkDays(Calendar calendar){
	    String pattern = "yyyy-MM-dd";
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	    String dateString = simpleDateFormat.format(calendar.getTime());
	 
	    //节假日 这个可能不同地区,不同年份 都有可能不一样,所以需要有个地方配置, 可以放数据库, 配置文件,环境变量 等等地方
	    //这里以配置文件 为例子
	    ResourceBundle resourceBundle = ResourceBundle.getBundle("WorkDayPlusConfig");
	    String weekendWorkDays = resourceBundle.getString("WeekendWorkDays");
	 
	    String[] weekendWorkDaysArray = weekendWorkDays.split(",");
	 
	    boolean isWeekendWorkDays = ArrayUtils.contains(weekendWorkDaysArray, dateString);
	    return isWeekendWorkDays;
	}
	
	/**
	 * 根据开始时间和结束时间返回时间段内的时间集合
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return List
	 */
	public List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("WorkDayPlusConfig");
		Date workStartTime = this.chang2TodayTime(resourceBundle.getString("WorkStartTime"),endDate);//结束时间那天的上班时间
		List<Date> lDate = new ArrayList<Date>();
		lDate.add(beginDate);// 把开始时间加入集合
		Calendar cal = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		cal.setTime(beginDate);
		boolean bContinue = true;
		while (bContinue) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			cal.add(Calendar.DAY_OF_MONTH, 1);
			// 测试此日期是否在指定日期之后//并且不是一个日期
			if (endDate.after(cal.getTime())
					&&(!DateUtils.isSameDay(endDate, cal.getTime()))) {
				if (!(isWeekend(cal) || isHoliday(cal))){
					lDate.add(cal.getTime());
			    }
			} else {
				break;
			}
		}
		if((!(DateUtils.isSameDay(beginDate, endDate)))&&endDate.after(workStartTime)&&endDate.after(beginDate)){//日期不是同一天且在当天工作时间（结束算起）之后
			lDate.add(endDate);// 把结束时间加入集合
		}
		return lDate;
	}
	/**
	 * 但某个流程结束，计算下一个流程开始时间=结束时间+1hour
	 * 如果不再工作时间内则从第二天早上开始
	 * @param previousTime
	 * @return
	 */
	public Date calNextStartTime(Date previousTime){
		ResourceBundle resourceBundle = ResourceBundle.getBundle("WorkDayPlusConfig");
		String goOffWorkTime = "";
		Date resultDate = null;
		if(isSummerTime(previousTime)){
			goOffWorkTime=resourceBundle.getString("SummerGoOffWorkTime");//获取夏季下班时间
		}else if(!isSummerTime(previousTime)){
			goOffWorkTime=resourceBundle.getString("WinterGoOffWorkTime");//获取冬季下班时间
		}
		Date todayGooffWorkTime = this.chang2TodayTime(goOffWorkTime,previousTime);//当天的下班时间
		Date workStartTime = this.chang2TodayTime(resourceBundle.getString("WorkStartTime"),previousTime);//结束工作那天上班时间
		Date todayEndTime = this.chang2TodayTime("23:59:59",previousTime);//结束工作那天凌晨12点
		Calendar ca=Calendar.getInstance();
		ca.setTime(previousTime);
		ca.add(Calendar.HOUR_OF_DAY, 1);
		Date workEndTime = ca.getTime();//结束的时间加一小时
		Date tomorrowStarTime = this.chang2TodayTime("00:00:00",workEndTime);//结束工作第二天天凌晨0点
		Date tomorrowWorkStartTime = this.chang2TodayTime(resourceBundle.getString("WorkStartTime"),workEndTime);
		if(workEndTime.before(todayGooffWorkTime)&&workEndTime.after(workStartTime)){//结束时间加一小时在当天上班下班之间
			resultDate = workEndTime;
		}else if(workEndTime.before(todayEndTime)&&workEndTime.after(todayGooffWorkTime)){//结束时间加一小时在下班和当天凌晨之间
			Calendar ca2=Calendar.getInstance();
			ca2.setTime(workStartTime);
			ca2.add(Calendar.DATE, 1);
			resultDate = this.chang2TodayTime(resourceBundle.getString("WorkStartTime"),ca2.getTime());
		}else if(workEndTime.before(tomorrowWorkStartTime)&&workEndTime.after(tomorrowStarTime)){//结束加一小时在当天上班时间之前即凌晨完成的
			Calendar ca2=Calendar.getInstance();
			ca2.setTime(tomorrowWorkStartTime);
			resultDate = this.chang2TodayTime(resourceBundle.getString("WorkStartTime"),ca2.getTime());
		}
		Calendar calresult = Calendar.getInstance();
		calresult.setTime(resultDate);

		return calnextDayIsWorkDay(calresult);
	}
	/**
	 * 使用递归，计算下次的工作日
	 * @param cal
	 * @return
	 */
	public Date calnextDayIsWorkDay(Calendar cal){
		Date date = cal.getTime();
		if (isWeekend(cal) || isHoliday(cal)){
			cal.add(Calendar.DATE, 1);
			date = calnextDayIsWorkDay(cal);
	    }
		return date;
	}
	/**
	 * 使用递归，计算上次的工作日
	 * @param cal
	 * @return
	 */
	public Date callastDayIsWorkDay(Calendar cal){
		Date date = cal.getTime();
		if (isWeekend(cal) || isHoliday(cal)){
			cal.add(Calendar.DATE, -1);
			date = calnextDayIsWorkDay(cal);
	    }
		return date;
	}
	/**
	 * 计算某月第一个工作日 yyyy-mm-dd 08:30:00
	 * @param startTime
	 * @param whichmonth
	 * @return
	 */
	public Date calStartWorkInThisMonth(Date startTime,Date whichmonth){
		Date resultDate = null;
		if(isThisMonth(startTime,whichmonth)){
			resultDate = startTime;
		}else{
			resultDate = calThismonthFirstWorkDay(whichmonth);
		}
		return resultDate;
	}
	/**
	 * 计算某月最后一个工作日yyyy-mm-dd 17:30:00
	 * @param endTime
	 * @param whichmonth
	 * @return
	 */
	public Date calEndWorkInThisMonth(Date endTime,Date whichmonth){
		Date resultDate = null;
		if(isThisMonth(endTime,whichmonth)){
			resultDate = endTime;
		}else{
			resultDate = calThismonthLastWorkDay(whichmonth);
		}
		return resultDate;
	}
	/**
	 * 计算某月的第一天工作日
	 * @param whichmonth
	 * @return
	 */
	public Date calThismonthFirstWorkDay(Date whichmonth){
		Calendar cale = null;
        cale = Calendar.getInstance();
        cale.setTime(whichmonth);
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        Date workDay = calnextDayIsWorkDay(cale);
        return calWorkStartTime(workDay);
	}
	/**
	 * 计算某月的第一天
	 * @param whichmonth
	 * @return
	 */
	public Date calThismonthFirstDay(Date whichmonth){
		Calendar cale = null;
        cale = Calendar.getInstance();
        cale.setTime(whichmonth);
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        Date workDay = calnextDayIsWorkDay(cale);
        return workDay;
	}
	/**
	 * 计算某月的最后一天工作日
	 * @param whichmonth
	 * @return
	 */
	public Date calThismonthLastWorkDay(Date whichmonth){
		Calendar cale = null;
        cale = Calendar.getInstance();
        cale.setTime(whichmonth);
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        Date workDay = callastDayIsWorkDay(cale);
        return calWorkOffTime(workDay);
	}
	/**
	 * 计算某月的最后一天
	 * @param whichmonth
	 * @return
	 */
	public Date calThismonthLastDay(Date whichmonth){
		Calendar cale = null;
        cale = Calendar.getInstance();
        cale.setTime(whichmonth);
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        Date workDay = callastDayIsWorkDay(cale);
        return workDay;
	}
	/**
	 * 计算当天的上班时间
	 * @param date
	 * @return
	 */
	public Date calWorkStartTime(Date date){
		ResourceBundle resourceBundle = ResourceBundle.getBundle("WorkDayPlusConfig");
		Date workStartTime = this.chang2TodayTime(resourceBundle.getString("WorkStartTime"),date);
		return workStartTime;
	}
	/**
	 * 计算当天的下班时间
	 * @param date
	 * @return
	 */
	public Date calWorkOffTime(Date date){
		ResourceBundle resourceBundle = ResourceBundle.getBundle("WorkDayPlusConfig");
		String goOffWorkTime = "";
		if(isSummerTime(date)){
			goOffWorkTime=resourceBundle.getString("SummerGoOffWorkTime");//获取夏季下班时间
		}else if(!isSummerTime(date)){
			goOffWorkTime=resourceBundle.getString("WinterGoOffWorkTime");//获取冬季下班时间
		}
		Date workOffTime = this.chang2TodayTime(goOffWorkTime,date);
		return workOffTime;
	}
	/**
	 * 将Date类型转换String yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public String date2String(Date date){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	/**
	 * 判断是否是本周
	 * @param time
	 * @return
	 */
	public static boolean isThisWeek(long time)
    {
    	Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(time));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if(paramWeek==currentWeek){
     	   return true;
        }
        return false;
    }
    /**
     * 判断是否是某一天
     * @param date 要比较的日期
     * @param day 哪一个天
     * @return
     */
    public static boolean isToday(Date date,Date day)
    {
       return isThisTime(date,day,"yyyy-MM-dd");
    }
    /**
     * 判断选择的日期是否是本月
     * @param date 要比较的日期
     * @param Month 哪一个月
     * @return
     */
    public static boolean isThisMonth(Date date,Date Month)
    {
    	 return isThisTime(date,Month,"yyyy-MM");
    }
    
    private static boolean isThisTime(Date date,Date Month,String pattern) {
         SimpleDateFormat sdf = new SimpleDateFormat(pattern);
         String param = sdf.format(date);//参数时间
         String now = sdf.format(Month);//当前时间
         if(param.equals(now)){
      	   return true;
         }
         return false;
	}

}
