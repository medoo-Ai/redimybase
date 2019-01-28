package com.aispread.manager.leave.service.impl;

import com.aispread.manager.leave.entity.CustomHolidayEntity;
import com.aispread.manager.leave.mapper.CustomHolidayMapper;
import com.aispread.manager.leave.service.CustomHolidayService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @auther SyntacticSugar
 * @data 2019/1/25 0025下午 8:53
 */
@Service
public class CustomHolidayImpl extends ServiceImpl<CustomHolidayMapper, CustomHolidayEntity> implements CustomHolidayService {
    /**
     * 添加自定义假期
     *
     * @param newCustomHoliday
     */
    @Override
    public void addHoliday(String newCustomHoliday) {
        CustomHolidayEntity customHolidayEntity = new CustomHolidayEntity();
        customHolidayEntity.setCusHoliday(newCustomHoliday);
        customHolidayEntity.setStatus(1);
        baseMapper.insert(customHolidayEntity);
    }

    /**
     * 删除自定义假期
     * @param customHoliday
     */
    @Override
    public void removeHolidayByDay(String customHoliday) {
        QueryWrapper<CustomHolidayEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cus_holiday",customHoliday);
        baseMapper.delete(queryWrapper);
    }

    /**
     * 获取自定义假期列表
     */
    @Override
    public List<CustomHolidayEntity> getHolidays() {
        QueryWrapper<CustomHolidayEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 获取自定义加班列表
     */
    @Override
    public List<CustomHolidayEntity> getWorkdays() {
        QueryWrapper<CustomHolidayEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",0);
        return   baseMapper.selectList(queryWrapper);
    }

    /**
     * 获取自定义加班列表
     */
    @Override
    public List<CustomHolidayEntity> getAllCustomdays() {
        QueryWrapper<CustomHolidayEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",0).or().eq("status", 1);
        return   baseMapper.selectList(queryWrapper);
    }




    /**
     * 更新自定义的节假日
     * @param customHoliday  要更新的自定义节假日
     * @param newCustomHoliday
     * @param newStatus
     */
    @Override
    public void updateCustomHolidayByday(String customHoliday,String newCustomHoliday,int newStatus ) {
        QueryWrapper<CustomHolidayEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cus_holiday",customHoliday);
        CustomHolidayEntity customHolidayEntity = baseMapper.selectOne(queryWrapper);
        customHolidayEntity.setCusHoliday(newCustomHoliday);
        customHolidayEntity.setStatus(newStatus);
        baseMapper.update(customHolidayEntity,queryWrapper);
    }


}
