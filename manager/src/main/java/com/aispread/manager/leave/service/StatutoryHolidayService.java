package com.aispread.manager.leave.service;

import com.aispread.manager.leave.entity.StatutoryHolidayEntity;
import com.aispread.manager.leave.entity.HolidayRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface StatutoryHolidayService extends IService<StatutoryHolidayEntity> {
    String getStatutoryHolidays();

    boolean saveStatutoryHolidays();

    boolean updateStatutoryHolidays();
}
