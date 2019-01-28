package com.aispread.manager.flowable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程运行用户表
 * </p>
 *
 * @author vim
 * @since 2019-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_ru_user")
public class FlowRuUserEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 用户类型(0:发起人,1:用户组,2:其他节点,3:来自表单,4:上级领导,5:普通用户)
     */
    @TableField("type")
    private Integer type;

    /**
     * 值
     */
    @TableField("value")
    private String value;

    /**
     * 业务ID
     */
    @TableField("business_id")
    private String businessId;

    /**
     * 流程节点CODE
     */
    @TableField("task_code")
    private String taskCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
