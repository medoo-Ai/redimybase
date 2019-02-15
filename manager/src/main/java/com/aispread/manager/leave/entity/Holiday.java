package com.aispread.manager.leave.entity;

import com.aispread.manager.flowable.entity.FlowDefinitionEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Transient;

//请假表
@TableName("t_holiday")
public @Data class Holiday {

	@TableId(value = "holiday_id", type = IdType.AUTO)
	private Long holidayId;

	@TableField("type_id")
	private Long typeId;  //请假类型
	
	@TableField("leave_days")
	private Double leaveDays; //请假天数

	//主表的流程定义的id
	@TableField("flow_definition_id")
	private FlowDefinitionEntity flowDefinitionId;;
	
	@TableField("personnel_advice")
	private String personnelAdvice;//人事部意见及说明
	
	@TableField("manager_advice")
	private String managerAdvice;//经理意见及说明
	
	@Transient
	private String nameuser;

}
