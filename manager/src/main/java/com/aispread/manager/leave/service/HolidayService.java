package com.aispread.manager.leave.service;

import com.aispread.manager.leave.entity.CustomHolidayEntity;
import com.aispread.manager.leave.entity.Holiday;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface HolidayService extends IService<Holiday> {
    /**
     * 添加假期
     * @param newCustomHoliday
     */
    void addHoliday(String newCustomHoliday);


}
