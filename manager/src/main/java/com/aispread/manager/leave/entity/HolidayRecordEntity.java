package com.aispread.manager.leave.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;

import java.util.Date;

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
    /** 工作流名字*/
    @TableField("name")
    private String name;
    @TableField("create_time")
    private Date createTime;
    @TableField("category_id")
    private String categoryId;
    @TableField("user_id")
    private String userId;
    /** 1084735479095889921 工作流的id */

    /** 审批状态 */
    @TableField("status")
    private String status;
    /** 类型 */
    @TableField("type")
    private String type;
    /** 客户id 租客id  */
    @TableField("tenant_id")
    private String tenantId;

    @Override
    public void setId(String id) {
        this.id = id;
    }
//    public static class Status {
//        public static final Integer 事假 = 0;
//        public static final Integer 病假 = 1;
//        public static final Integer 年假 = 2;
//        public static final Integer 丧假 = 3;
//    }

}
