package com.aispread.manager.flowable.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程节点表
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_node")
public class FlowNodeEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private String id;

    /**
     * 节点名称
     */
    @TableField("name")
    private String name;

    /**
     * 节点类型
     */
    @TableField("type")
    private String type;

    /**
     * 流程定义ID
     */
    @TableField("definition_id")
    private String definitionId;

    /**
     * 父级ID(子流程ID)
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 节点CODE
     */
    private String taskCode;

    /**
     * 发起人是否可见(0:否,1:是)
     */
    @TableField("initiator_view")
    private Integer initiatorView;


    /**
     * 审批人是否可见(0:否,1:是)
     */
    @TableField("assignee_view")
    private Integer assigneeView;

    /**
     * 工作流流程定义ID
     */
    private String flowDefinitionId;


    public FlowNodeEntity() {
        this.id = IdWorker.getIdStr();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public class Type{
        public static final String USER_TASK = "userTask";
        public static final String START_EVENT = "start";
        public static final String END_EVENT = "end";
        public static final String INCLUSIVE_GATEWAY = "inclusiveGateway";
        public static final String PARALLEL_GATEWAY = "parallelGateway";
        public static final String SUBPROCESS = "subProcess";
        //排他网关
        public static final String EXCLUSIVE_GATEWAY = "exclusiveGateway";

        //流程线
        public static final String SEQUENCE_FLOW = "sequenceFlow";
    }
}
