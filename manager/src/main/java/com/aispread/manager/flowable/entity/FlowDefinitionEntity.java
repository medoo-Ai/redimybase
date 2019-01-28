package com.aispread.manager.flowable.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableField;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程定义表
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
@ApiModel(value = "流程定义实体类")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_definition")
public class FlowDefinitionEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private String id;

    /**
     * 定义名称
     */
    @ApiModelProperty(value = "定义名称")
    @TableField("name")
    private String name;

    /**
     * 定义key
     */
    @ApiModelProperty(value = "定义key")
    @TableField("definition_key")
    private String definitionKey;

    /**
     * 所属种类
     */
    @ApiModelProperty(value = "所属种类")
    @TableField("category_id")
    private String categoryId;

    /**
     * 状态(0:未发布,1:已发布)
     */
    @ApiModelProperty(value = "状态(0:未发布,1:已发布,2:已删除)")
    @TableField("status")
    private Integer status;

    /**
     * 定义描述
     */
    @ApiModelProperty(value = "定义描述")
    @TableField("description")
    private String description;

    /**
     * 工作流流程定义key
     */
    @ApiModelProperty(value = "工作流流程定义key")
    @TableField("flow_definition_key")
    private String flowDefinitionKey;

    /**
     * 工作流流程定义ID
     */
    @ApiModelProperty(value = "工作流流程定义ID")
    @TableField("flow_definition_id")
    private String flowDefinitionId;

    /**
     * 工作流流程定义版本
     */
    @ApiModelProperty(value = "工作流流程定义版本")
    @TableField("flow_definition_version")
    private String flowDefinitionVersion;

    /**
     * 是否完成第一个任务(0:否,1:是)
     */
    @ApiModelProperty(value = "是否完成第一个任务(0:否,1:是)")
    private Boolean completeFirstTask;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField("creator")
    private String creator;

    /**
     * 图标链接
     */
    @ApiModelProperty(value = "图标链接")
    private String ico;

    /**
     * 流程模型ID
     */
    @ApiModelProperty(value = "流程模型ID")
    private String modelId;
    /**
     * 是否更新流程配置
     */
    @TableField(exist = false)
    private boolean syncFlowConfig;

    /**
     * 是否为内置流程(0:否,1:是)
     */
    private boolean internal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public static class Status {
        public static final Integer 未发布 = 0;
        public static final Integer 已发布 = 1;
        public static final Integer 已删除 = 2;
    }
}
