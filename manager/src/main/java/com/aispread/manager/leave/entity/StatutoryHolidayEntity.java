package com.aispread.manager.leave.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;

/**
 * 法定节假日
 * @auther SyntacticSugar
 * @data 2019/1/25 0025下午 2:44
 */
@TableName("t_holiday_model")
public @Data class StatutoryHolidayEntity extends IdEntity<String> {
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    //法定节假日json串
    @TableField("holiday_details")
    private String holidayDetails;
    @TableField("year")
    private String year;

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
