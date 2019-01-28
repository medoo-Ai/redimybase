package com.aispread.manager.leave.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redimybase.common.util.DateUtil;
import com.redimybase.common.util.StrKit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @auther SyntacticSugar
 * @data 2019/1/26 0026下午 8:05
 */
public class LeaveUtils {

    public static final String goWeekURL = "http://tool.bitefu.net/jiari/vip.php?d=";
    public static final String format = "yyyy-MM-dd HH-mm-ss";
    public static List<Date> holidays = null;

    public static void setHolidays(List<Date> holidays) {
        LeaveUtils.holidays = holidays;
    }

    /**
     * 获取holidays  集合
     * @return
     */
    public static List<Date> getHolidayList() {
        //处理json  1节日休0班
        String result = "";
        BufferedReader in = null;
        result = getResult();
        // 动态的获取国家法定节假日  解析json串
        try {
            String concurrentYear = DateUtil.getYear(new Date());
            System.out.println(concurrentYear);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> objectMap = mapper.readValue(result, new TypeReference<HashMap<String, Object>>() {
            });
            Object data = objectMap.get("data");
            String s = mapper.writeValueAsString(data);
            Map<String, Integer> holidayData = mapper.readValue(s, new TypeReference<HashMap<String, Integer>>() {
            });
            // 取出1001拼接year 遍历切割添加到list
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            holidays = new ArrayList<Date>();
            //
            holidayData.keySet().forEach((key) -> {
                if (holidayData.get(key) == 1) {
                    // key添加到集合中
                    String holidayDate = key;
                    String subPre = StrKit.subPre(holidayDate, 2);
                    String subSuf = StrKit.subSuf(holidayDate, 2);
                    String date = concurrentYear + "-" + subPre + "-" + subSuf;
                    try {
                        holidays.add(sdf.parse(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
            return holidays;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 result 结果 ，数据库中的json 串
     * @return
     */
    public static String getResult() {
        String result = "";
        BufferedReader in = null;
        // 动态的获取国家法定节假日  解析json串
        String concurrentYear = DateUtil.getYear(new Date());
        System.out.println(concurrentYear);
        try {
            URL url;
            url = new URL(goWeekURL + concurrentYear + "&info=0&type=8&apikey=123456");
            URLConnection con;
            // 连接
            con = url.openConnection();
            con.setRequestProperty("accept", "*/*");
            con.setRequestProperty("connection", "Keep-Alive");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            con.connect();
            //
            Map<String, List<String>> map = con.getHeaderFields();
            map.keySet().forEach(key -> System.out.println(map.get(key)));
            in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    /**
     * 获取两个时间之内的工作日时间毫秒值（只去掉两个日期之间的周末时间，法定节假日未去掉）
     * @param start -起始时间
     * @param end   -结束时间
     * @return       Long型时间差对象
     */
    public static Long getWorkdayTimeInMillis(long start, long end) {

        // 如果起始时间大于结束时间，将二者交换
        if (start > end) {
            long temp = start;
            start = end;
            end = temp;
        }
        // 根据参数获取起始时间与结束时间的日历类型对象
        Calendar sdate = Calendar.getInstance();
        Calendar edate = Calendar.getInstance();

        sdate.setTimeInMillis(start);
        edate.setTimeInMillis(end);

        // 计算指定时间段内法定节假日天数的毫秒数
        long holidayss = 0;
        if (holidays != null) {
            holidayss = getHolidaysInMillis(start, end);
            holidays.clear();
        }

        // 如果两个时间在同一周并且都不是周末日期，则直接返回时间差，增加执行效率
        if ((sdate.get(Calendar.YEAR) == edate.get(Calendar.YEAR))
                && (sdate.get(Calendar.WEEK_OF_YEAR) == edate
                .get(Calendar.WEEK_OF_YEAR))
                && (sdate.get(Calendar.DAY_OF_WEEK) != 1 && sdate
                .get(Calendar.DAY_OF_WEEK) != 7)
                && (edate.get(Calendar.DAY_OF_WEEK) != 1 && edate
                .get(Calendar.DAY_OF_WEEK) != 7)) {
            return new Long(end - start - holidayss);
        }
        // 如果两个时间在同一周并且都是周末日期，则直接返回0
        if ((sdate.get(Calendar.YEAR) == edate.get(Calendar.YEAR))
                && (sdate.get(Calendar.WEEK_OF_YEAR) == edate
                .get(Calendar.WEEK_OF_YEAR) - 1)
                && (sdate.get(Calendar.DAY_OF_WEEK) == 1
                || sdate.get(Calendar.DAY_OF_WEEK) == 7)
                &&
                (edate.get(Calendar.DAY_OF_WEEK) == 1
                        || edate.get(Calendar.DAY_OF_WEEK) == 7)) {
            start = validateStartTime(sdate);
            end = validateEndTime(edate);
            long result = end - start - holidayss;
            return new Long(result > 0 ? result : 0);
        }

        start = validateStartTime(sdate);
        end = validateEndTime(edate);

        // 首先取得起始日期与结束日期的下个周一的日期
        Calendar snextM = getNextMonday(sdate);
        Calendar enextM = getNextMonday(edate);

        // 获取这两个周一之间的实际天数
        int days = getDaysBetween(snextM, enextM);

        // 获取这两个周一之间的工作日数(两个周一之间的天数肯定能被7整除，并且工作日数量占其中的5/7)
        int workdays = days / 7 * 5;

        // 计算最终结果，具体为：workdays加上开始时间的时间偏移量，减去结束时间的时间偏移量
        long result = (workdays * 24 * 3600000
                + calcWorkdayTimeInMillis(sdate, edate, start, end) - holidayss);
        return result > 0 ? result : 0;
    }

    public  static long getHolidaysInMillis(long start, long end) {
        Calendar scalendar = Calendar.getInstance();
        Calendar ecalendar = Calendar.getInstance();
        int daysofH = 0;
        try {
            scalendar.setTimeInMillis(start);
            ecalendar.setTimeInMillis(end);
            if ( holidays == null) {
                return new Long(0);
            }
            Iterator<Date> iterator =  holidays.iterator();
            // TODO
            while (iterator.hasNext()) {
                Calendar ca = Calendar.getInstance();
                Date hdate = iterator.next();
                ca.setTime(hdate);
                if (ca.after(scalendar) && ca.before(ecalendar)) {
                    daysofH = daysofH + 1;
                } else if (ca.getTimeInMillis() == scalendar.getTimeInMillis()) {
                    daysofH = daysofH + 1;
                } else if (ca.getTimeInMillis() == ecalendar.getTimeInMillis()) {
                    daysofH = daysofH + 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Long(0);
        }
        return daysofH * 24 * 3600000;
    }

    /**
     * 校验开始时间  结束时间
     *
     */
    private static long validateStartTime(Calendar sdate) {
        if (sdate.get(Calendar.DAY_OF_WEEK) == 1)//开始日期从周日开始,如果开始时间为周末，自动修复为下周的9：00开始
        {
            sdate.add(Calendar.DATE, 1);
            sdate.setTimeInMillis(
                    sdate.getTime().getTime() - //从9点开始
                            (
                                    ((sdate.get(Calendar.HOUR_OF_DAY) - 9) * 3600000)
                                            + (sdate.get(Calendar.MINUTE) * 60000)
                                            + (sdate.get(Calendar.SECOND) * 1000)
                            )
            );
        } else if (sdate.get(Calendar.DAY_OF_WEEK) == 7)//开始日期从周六开始
        {
            sdate.add(Calendar.DATE, 2);
            sdate.setTimeInMillis(
                    sdate.getTime().getTime() - //从9点开始,如果开始时间为周末，自动修复为下周的9：00开始
                            (
                                    ((sdate.get(Calendar.HOUR_OF_DAY) - 9) * 3600000)
                                            + (sdate.get(Calendar.MINUTE) * 60000)
                                            + (sdate.get(Calendar.SECOND) * 1000)
                            )
            );
        }
        return sdate.getTimeInMillis();
    }


    public static long validateEndTime(Calendar edate) {
        if (edate.get(Calendar.DAY_OF_WEEK) == 1)//结束日期是周日,如果结束日期是周六、周末自动修复为这周五18:00
        {
            edate.add(Calendar.DATE, -2);
            edate.setTimeInMillis(
                    edate.getTime().getTime() +
                            (
                                    18 * 3600000 - (
                                            (edate.get(Calendar.HOUR_OF_DAY) * 3600000)
                                                    + (edate.get(Calendar.MINUTE) * 60000)
                                                    + (edate.get(Calendar.SECOND) * 1000)
                                    )
                            )
            );
            //结束日期是周六,如果结束日期是周六、周末自动修复为这周五18:00
        } else if (edate.get(Calendar.DAY_OF_WEEK) == 7) {
            edate.add(Calendar.DATE, -1);
            edate.setTimeInMillis(
                    edate.getTime().getTime() +
                            (
                                    18 * 3600000 - (
                                            (edate.get(Calendar.HOUR_OF_DAY) * 3600000)
                                                    + (edate.get(Calendar.MINUTE) * 60000)
                                                    + (edate.get(Calendar.SECOND) * 1000)
                                    )
                            )
            );
        }
        return edate.getTimeInMillis();
    }

    public   static long calcWorkdayTimeInMillis(Calendar sdate, Calendar edate,
                                         long start, long end) {
        // 获取开始时间的偏移量
        long scharge = 0;
        if (sdate.get(Calendar.DAY_OF_WEEK) != 1
                && sdate.get(Calendar.DAY_OF_WEEK) != 7) {
            // 只有在开始时间为非周末的时候才计算偏移量
            scharge += (sdate.get(Calendar.HOUR_OF_DAY) * 3600000);
            scharge += (sdate.get(Calendar.MINUTE) * 60000);
            scharge += (sdate.get(Calendar.SECOND) * 1000);
            scharge = ((24 * 3600000) - scharge);

            scharge += ((sdate.getTime().getTime() - start) - (3 * 24 * 3600000));
        }
        // (24*3600000=86400000)-((9*3600000+1800000)=34200000)+(3*24*3600000=259200000)-(2*24*3600000)=
        // 86400000-34200000=52200000
        // 获取结束时间的偏移量
        long echarge = 0;
        if (edate.get(Calendar.DAY_OF_WEEK) != 1
                && edate.get(Calendar.DAY_OF_WEEK) != (7)) {
            // 只有在结束时间为非周末的时候才计算偏移量
            echarge += (edate.get(Calendar.HOUR_OF_DAY) * 3600000);
            echarge += (edate.get(Calendar.MINUTE) * 60000);
            echarge += (edate.get(Calendar.SECOND) * 1000);
            echarge = ((24 * 3600000) - echarge);
            echarge += (edate.getTime().getTime() - end) - (24 * 3600000);
            echarge -= (2 * 24 * 3600000);
        }
        // (24*3600000=86400000)-(18*3600000=64800000)+(24*3=259200000)
        if (scharge < 0 || echarge < 0) {
            scharge = echarge = 0;
        }
        return scharge - echarge;
    }

    /**
     * 获取两个时间之内的工作日时间（只去掉两个日期之间的周末时间，法定节假日未去掉）
     *
     * @param start -起始时间，共有3个重载方法，可以传入long型，Long型，与Date型
     * @param end   -结束时间，共有3个重载方法，可以传入long型，Long型，与Date型
     * @return Long型时间差对象
     */
    public Long getWorkdayTimeInMillisExcWeekend(long start, long end) {
        return getWorkdayTimeInMillis(start, end);
    }

    /***
     * 获取两个时间之内的工作日时间（去掉两个日期之间的周末时间，法定节假日时间）
     *
     * @param start
     * @param end
     * @return
     */
    public static Long getWorkdayTimeInMillisExcWeekendHolidays(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date sdate;
        Date edate;
        try {
            sdate = sdf.parse(start);
            edate = sdf.parse(end);
            return getWorkdayTimeInMillis(sdate.getTime(), edate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return new Long(0);
        }
    }

    public static Long getWorkdayTimeInMillis(Long start, Long end) {
        return getWorkdayTimeInMillis(start.longValue(), end.longValue());
    }

    public static Long getWorkdayTimeInMillis(Date start, Date end) {
        return getWorkdayTimeInMillis(start.getTime(), end.getTime());
    }

    public static Long getWorkdayTimeInMillis(String start, String end, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date sdate;
        Date edate;
        try {
            sdate = sdf.parse(start);
            edate = sdf.parse(end);
            return getWorkdayTimeInMillis(sdate, edate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Long(0);
        }
    }



    public  static Calendar getNextMonday(Calendar cal) {
        int addnum = 9 - cal.get(Calendar.DAY_OF_WEEK);
        if (addnum == 8) {
            addnum = 1;// 周日的情况
        }
        cal.add(Calendar.DATE, addnum);
        return cal;
    }

    /**
     * @param
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " days " + hours + " hours " + minutes + " minutes "
                + seconds + " seconds ";
    }

    /**
     * 获取两个日期之间的实际天数，支持跨年
     */
    public  static int getDaysBetween(Calendar start, Calendar end) {
        if (start.after(end)) {
            Calendar swap = start;
            start = end;
            end = swap;
        }

        int days = end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
        int y2 = end.get(Calendar.YEAR);
        if (start.get(Calendar.YEAR) != y2) {
            start = (Calendar) start.clone();
            do {
                days += start.getActualMaximum(Calendar.DAY_OF_YEAR);
                start.add(Calendar.YEAR, 1);
            } while (start.get(Calendar.YEAR) != y2);
        }
        return days;
    }


}
