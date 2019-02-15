package com.aispread.manager.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 类型列表
 * @auther SyntacticSugar
 * @data 2019/2/13 0013下午 6:21
 */
@TableName("t_type_list")
public @Data class SystemTypeList {
    @TableField("type_id")
    @TableId(type = IdType.AUTO)
    private Long typeId;			//类型id

    @TableField("type_name")
    @NotEmpty(message="类型名称不能为空")
    private String typeName;		//类型名字

    @TableField("sort_value")
    private Integer typeSortValue;	//排序值

    @TableField("type_model")
    private String typeModel;		//所属模块

    @TableField("type_color")
    private String typeColor;		//类型颜色
}
