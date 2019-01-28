package com.aispread.manager.leave.service.impl;

import com.aispread.manager.leave.entity.CustomHolidayEntity;
import com.aispread.manager.leave.entity.HolidayRecordEntity;
import com.aispread.manager.leave.entity.StatutoryHolidayEntity;
import com.aispread.manager.leave.mapper.CustomHolidayMapper;
import com.aispread.manager.leave.mapper.HolidayRecordMapper;
import com.aispread.manager.leave.mapper.StatutoryHolidayMapper;
import com.aispread.manager.leave.service.CustomHolidayService;
import com.aispread.manager.leave.service.HolidayRecordService;
import com.aispread.manager.leave.utils.JsonUtils;
import com.aispread.manager.leave.utils.LeaveUtils;
import com.aispread.manager.leave.utils.WorkDayUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redimybase.common.util.DateUtil;
import com.redimybase.common.util.StrKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @auther SyntacticSugar
 * @data 2019/1/26 0026下午 2:35
 */
@Service
public class HolidayRecordServiceImpl extends ServiceImpl<HolidayRecordMapper, HolidayRecordEntity>  implements HolidayRecordService {

    @Autowired
    private StatutoryHolidayMapper statutoryHolidayMapper;
    @Autowired
    private CustomHolidayService customHolidayService;


    private SimpleDateFormat sdf=null;

    static List<Date> holidayList = new CopyOnWriteArrayList<>();
    static List<String> list = new CopyOnWriteArrayList<String>();
    static List<CustomHolidayEntity> allCustomdays = new CopyOnWriteArrayList<>();
    /**
     * 获取请假的时间天数
     * @param holidayRecordEntity
     * @return
     */
    @Override
    public String getWorkdayTimeInMillisExcWeekendHolidays(HolidayRecordEntity holidayRecordEntity) {

//        //从数据库查询result
//        QueryWrapper<StatutoryHolidayEntity> sheQueryWrapper = new QueryWrapper<>();
//        sheQueryWrapper.eq("year", DateUtil.getYear(new Date()));
//        StatutoryHolidayEntity statutoryHolidayEntity = statutoryHolidayMapper.selectOne(sheQueryWrapper);
//        String holidayDetails = statutoryHolidayEntity.getHolidayDetails();
//
//         allCustomdays = customHolidayService.getAllCustomdays();
//        try {
//            String concurrentYear = DateUtil.getYear(new Date());
//            System.out.println(concurrentYear);
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, Object> objectMap = mapper.readValue(holidayDetails, new TypeReference<HashMap<String, Object>>() {
//            });
//            Object data = objectMap.get("data");
//            // 转换java对象   json
//            HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
//            allCustomdays.forEach(allCustomday->{
//                String cusHoliday = allCustomday.getCusHoliday();
//                int status = allCustomday.getStatus();
//                //
//                String[] split = cusHoliday.split("-");
//                String key=split[1]+split[2];
//                Integer value=status;
//                stringIntegerHashMap.put(key, value);
//            });
//            String allCustomdaysJson="";
//            allCustomdaysJson = mapper.writeValueAsString(stringIntegerHashMap);
//            System.out.println(allCustomdaysJson);
//            String s = mapper.writeValueAsString(data);
//            System.out.println(s);
//            //合并json
//            s = JsonUtils.mergeJson(s, allCustomdaysJson).toString();
//            System.out.println(s);
//            Map<String, Integer> holidayData = mapper.readValue(s, new TypeReference<HashMap<String, Integer>>() {
//            });
//            // 取出1001拼接year 遍历切割添加到list
//             sdf = new SimpleDateFormat("yyyy-MM-dd");
//             holidayList  = new ArrayList<Date>();
//            //
//            holidayData.keySet().forEach((key) -> {
//                if (holidayData.get(key) == 1) {
//                    // key添加到集合中
//                    String holidayDate = key;
//                    String subPre = StrKit.subPre(holidayDate, 2);
//                    String subSuf = StrKit.subSuf(holidayDate, 2);
//                    String date = concurrentYear + "-" + subPre + "-" + subSuf;
//                    try {
//                        holidayList.add(sdf.parse(date));
//                        System.out.print(date+" ");
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
////            System.out.println(holidayList);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        LeaveUtils.setHolidays(holidayList);
//        //获取的时间
//        Long daysCounts = LeaveUtils.getWorkdayTimeInMillisExcWeekendHolidays(holidayRecordEntity.getStartTime(), holidayRecordEntity.getEndTime());
//        return LeaveUtils.formatDuring(daysCounts);

        WorkDayUtils workDayUtils = new WorkDayUtils();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return  workDayUtils.getWorkDays(sdf.parse(holidayRecordEntity.getStartTime()), sdf.parse(holidayRecordEntity.getEndTime()))+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "失败";
    }

}
