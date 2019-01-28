package com.aispread.manager.leave.service;

import com.aispread.manager.leave.entity.HolidayRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface HolidayRecordService extends IService<HolidayRecordEntity> {
    String getWorkdayTimeInMillisExcWeekendHolidays(HolidayRecordEntity holidayRecordEntity);
}
