package com.aispread.manager.leave.service;

import com.aispread.manager.leave.entity.CustomHolidayEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CustomHolidayService extends IService<CustomHolidayEntity> {
    /**
     * 添加假期
     * @param newCustomHoliday
     */
    void addHoliday(String newCustomHoliday);

    /**
     * 删除假期
      * @param customHoliday
     */
    void removeHolidayByDay(String customHoliday);

    /**
     * 获取自定义休息的列表
     * @return
     */
    List<CustomHolidayEntity> getHolidays();

    /**
     * 获取自定义工作日的列表
     * @return
     */
    List<CustomHolidayEntity> getWorkdays();

    /**
     * 更新自定义的节假日
     * @param customHoliday
     * @param newCustomHoliday
     * @param newStatus
     */
    void updateCustomHolidayByday(String customHoliday, String newCustomHoliday, int newStatus);

    /**
     * 获取所有的添加的列表
     * @return
     */
    List<CustomHolidayEntity> getAllCustomdays();

}
