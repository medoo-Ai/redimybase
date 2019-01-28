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
@TableName("t_holiday_record")
public @Data class HolidayRecordEntity extends IdEntity<String> {
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    //请假开始结束时间 yyyy-MM-dd  HH-mm-ss
    @TableField("start_time")
    private String startTime;
    @TableField("end_time")
    private String endTime;
    //remark
    @TableField("remark")
    private String remark;

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
