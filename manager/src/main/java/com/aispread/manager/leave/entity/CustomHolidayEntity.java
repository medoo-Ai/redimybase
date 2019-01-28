package com.aispread.manager.leave.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;

/**
 * 自定义假期
 * @auther SyntacticSugar
 * @data 2019/1/25 0025下午 3:45
 */

@TableName("t_holiday_custom")
public @Data class CustomHolidayEntity extends IdEntity<String> {
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    @TableField("cus_holiday")
    private String cusHoliday;
    //状态 1节日休息 0上班
    //status
    @TableField(value = "status")
    private int status;
    //remark
    @TableField("remark")
    private String remark;

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
