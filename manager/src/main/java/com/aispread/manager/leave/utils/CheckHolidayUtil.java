package com.aispread.manager.leave.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CheckHolidayUtil {
	// 通过获得一个时间区间获得该区间内的每一天。
	public static List<String> getDateList(String startDate, String endDate) {
		DateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<String> dateList = new ArrayList<>();
		Calendar calendar = Calendar.getInstance(Locale.CHINA);// 定义日期实例
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = dfFormat.parse(startDate);// 定义开始日期
			d2 = dfFormat.parse(endDate);// 定义结束日期
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(d1);
		while (calendar.getTime().before(d2) || calendar.getTime().equals(d2)) {
			// dateList.add(calendar.get(Calendar.HOUR_OF_DAY)+"");
			dateList.add(dfFormat.format(calendar.getTime()));
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}

		return dateList;
	}

	// 设置法定节假日
	public static List<Calendar> initHolidayList(List<String> dateList) {
		List<Calendar> holidayList = new ArrayList<>();
		if (dateList != null && dateList.size() > 0) {
			for (String date : dateList) {
				String[] da = date.split("-");
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, Integer.valueOf(da[0]));
				calendar.set(Calendar.MONTH, Integer.valueOf(da[1]) - 1);// 月份比正常小1,0代表一月
				calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(da[2]));
				holidayList.add(calendar);
			}
		}
		return holidayList;
	}

	// 初始化周末被调整为工作日的数据
	public static List<Calendar> initWeekendList(List<String> dateList) {
		List<Calendar> weekendList = new ArrayList<>();
		if (dateList != null && dateList.size() > 0) {
			for (String date : dateList) {
				String[] da = date.split("-");
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, Integer.valueOf(da[0]));
				calendar.set(Calendar.MONTH, Integer.valueOf(da[1]) - 1);// 月份比正常小1,0代表一月
				calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(da[2]));
				weekendList.add(calendar);
			}
		}
		return weekendList;
	}

	/**
	 * 判断日期是否是节假日
	 * 
	 * @param dateList
	 * @param holidayList
	 * @param weekendList
	 * @return
	 * @throws Exception
	 */
	public static double checkHoliday(List<String> dateList, List<Calendar> holidayList, List<Calendar> weekendList,
			String startTime, String endTime, String startSelect, String endSelect) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(df.parse(startTime));
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(df.parse(endTime));
		double days = 0;
		//用来记录在法定节假日或者周末法定调休日,用来后面删减周末天数中的排除
		List<Date> outWithDate = new ArrayList<Date>();
		//增加排除调休日start
		for(Calendar tempCalendar : weekendList){
			String yearStr = tempCalendar.get(Calendar.YEAR)+"";//获取年份
	        String month = tempCalendar.get(Calendar.MONTH) + 1 +"";//获取月份
	        String day = tempCalendar.get(Calendar.DATE)+"";//获取日
			outWithDate.add(df.parse(yearStr+"-"+month+"-"+day));	//添加当前日期为被过滤日期
		}
		//增加排除调休日end
		
		//增加排除节假日start
		for(Calendar tempCalendar : holidayList){
			String yearStr = tempCalendar.get(Calendar.YEAR)+"";//获取年份
	        String month = tempCalendar.get(Calendar.MONTH) + 1 +"";//获取月份
	        String day = tempCalendar.get(Calendar.DATE)+"";//获取日
			outWithDate.add(df.parse(yearStr+"-"+month+"-"+day));	//添加当前日期为被过滤日期
		}
		//增加排除节假日end
		for (String date : dateList) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(df.parse(date));// 初始化待检验日期
			Boolean holidayFlag = false;
			Boolean startFlag = false;
			Boolean endFlag = false;
			// 先确定给日期在不在法定假日内
			if (holidayList != null && holidayList.size() > 0) {
				for (Calendar holiday : holidayList) {
					if (holiday.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
							&& holiday.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
							&& holiday.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
						holidayFlag = true;// 说明该日期在法定节日以内
						// 如果开始时间在法定假日内
						if (startCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
								&& startCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
								&& startCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
							startFlag = true;
						}
						// 如果结束时间在法定假日内
						if (endCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
								&& endCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
								&& endCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
							endFlag = true;
						}
						break;
					}
				}
			}
			// 该天不在法定节日以内 判断是不是在双休日内
			if (!holidayFlag) {
				startFlag = false;// 开始时间不在双休日内
				endFlag = false;// 结束时间
				days++;// 不在法定假日 又不在双休日 不加等什么？
//				outWithDate.add(df.parse(date));	//添加当前日期为被过滤日期
				if (!startFlag && startTime.equals(df.format(calendar.getTime()))) {
					if ("12:00:00".equals(startSelect)) {
						days = days - 0.5;
					}
				}
				if (!endFlag && endTime.equals(df.format(calendar.getTime()))) {
					if ("8:00:00".equals(endSelect) || "08:00:00".equals(endSelect)) {
						days = days - 0.5;
					}
				}

			}
			// 循环结束
		}
		//减去周末天数
		days -= CheckHolidayUtil.calWeekDays(DateUtil.parseDate(startTime, DateUtil.DATE_FORMAT_YMD_),
				DateUtil.parseDate(endTime, DateUtil.DATE_FORMAT_YMD_), outWithDate);

		return days;
	}

	/**
	 * 计算日期间隔内存在周末天数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private static int calWeekDays(Date startTime, Date endTime) {
		Date flag = startTime;// 设置循环开始日期
		Calendar cal = Calendar.getInstance();
		int week;
		int days = 0;
		while (flag.compareTo(endTime) != 1) {
			cal.setTime(flag);
			// 判断是否为周六日
			week = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (week == 0 || week == 6) {// 0为周日，6为周六
				// 跳出循环进入下一个日期
				cal.add(Calendar.DAY_OF_MONTH, +1);
				flag = cal.getTime();
				days++;
				continue;
			}
			// 日期往后加一天
			cal.add(Calendar.DAY_OF_MONTH, +1);
			flag = cal.getTime();
		}
		return days;
	}
	

	/**
	 * 计算日期间隔内存在周末天数,并除去法定节假日和法定调休日
	 * 
	 * @param startTime
	 * @param endTime
	 * @param outWithDateList 需要除去的法定节假日和法定调休日
	 * @return
	 */
	private static int calWeekDays(Date startTime, Date endTime, List<Date> outWithDateList) {
		Date flag = startTime;// 设置循环开始日期
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		int week;
		int days = 0;
        Date tempDate = new Date();
        outer:
		while (flag.compareTo(endTime) != 1) {
			cal.setTime(flag);
			for (Date outWithDate : outWithDateList) {
				String yearStr = cal.get(Calendar.YEAR)+"";//获取年份
		        String month = cal.get(Calendar.MONTH) + 1 +"";//获取月份
		        String day = cal.get(Calendar.DATE)+"";//获取日
				try {
					tempDate = df.parse(yearStr+"-"+month+"-"+day);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(tempDate.equals(outWithDate)){
					cal.add(Calendar.DAY_OF_MONTH, +1);
					flag = cal.getTime();
					continue outer;
				}
			}
			// 判断是否为周六日
			week = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (week == 0 || week == 6) {// 0为周日，6为周六
				// 跳出循环进入下一个日期
				cal.add(Calendar.DAY_OF_MONTH, +1);
				flag = cal.getTime();
				days++;
				continue;
			}
			// 日期往后加一天
			cal.add(Calendar.DAY_OF_MONTH, +1);
			flag = cal.getTime();
		}
		return days;
	}
}
