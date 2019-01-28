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
 * 流程任务留言表
 * </p>
 *
 * @author vim
 * @since 2019-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_ru_comment")
public class FlowRuCommentEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 留言内容
     */
    @TableField("content")
    private String content;

    /**
     * 任务节点CODE
     */
    @TableField("task_code")
    private String taskCode;

    /**
     * 业务ID
     */
    @TableField("business_id")
    private String businessId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
