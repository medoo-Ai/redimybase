package com.aispread.manager.leave.service.impl;

import com.aispread.manager.leave.entity.StatutoryHolidayEntity;
import com.aispread.manager.leave.mapper.StatutoryHolidayMapper;
import com.aispread.manager.leave.service.StatutoryHolidayService;
import com.aispread.manager.leave.utils.LeaveUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.common.util.DateUtil;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * @auther SyntacticSugar
 * @data 2019/1/28 0028下午 2:56
 */
@Service
public class StatutoryServiceImpl extends ServiceImpl<StatutoryHolidayMapper, StatutoryHolidayEntity> implements StatutoryHolidayService {

    /**
     * 获取法定节假日列表
     * @return
     */
    @Override
    public String getStatutoryHolidays() {
        QueryWrapper<StatutoryHolidayEntity> statutoryHolidayEntityQueryWrapper = new QueryWrapper<>();
        statutoryHolidayEntityQueryWrapper.eq("year", DateUtil.getYear(new Date()));
        return baseMapper.selectOne(statutoryHolidayEntityQueryWrapper).getHolidayDetails();
    }

    /**
     * 保存法定节假日
     * @return
     */
    @Override
    public boolean saveStatutoryHolidays() {
        StatutoryHolidayEntity statutoryHolidayEntity = new StatutoryHolidayEntity();
        statutoryHolidayEntity.setYear(DateUtil.getYear(new Date()));
        statutoryHolidayEntity.setHolidayDetails(LeaveUtils.getResult());
        int insert = 0;
        try {
            insert = baseMapper.insert(statutoryHolidayEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (insert==1){
            return true;
        }
        return false;
    }

    /**
     * 更新法定节假日
     * @return
     */
    @Override
    public boolean updateStatutoryHolidays() {
        StatutoryHolidayEntity statutoryHolidayEntity = new StatutoryHolidayEntity();
        QueryWrapper<StatutoryHolidayEntity> statutoryHolidayEntityQueryWrapper = new QueryWrapper<>();

        int update = 0;
        try {
            update = baseMapper.update(statutoryHolidayEntity, statutoryHolidayEntityQueryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (update==1){
            return true;
        }
        return false;
    }
}
