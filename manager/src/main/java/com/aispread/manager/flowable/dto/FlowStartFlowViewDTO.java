package com.aispread.manager.flowable.dto;

import com.aispread.manager.flowable.entity.FlowButtonEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 发起流程视图DTO
 * Created by Vim 2019/1/9 15:51
 *
 * @author Vim
 */
@Data
public class FlowStartFlowViewDTO implements Serializable {


    /**
     * 表单JSON
     */
    private String formJson;

    /**
     * 操作按钮
     */
    private List<FlowButtonEntity> buttons;


    /**
     * 发起人
     */
    private String startUser;

    /**
     * 审批人
     */
    private String assignee;

    /**
     * 下一节点审批人
     */
    private String nextNodeAssignee;

    /**
     * 下一节点名称
     */
    private String nextNodeName;

    /**
     * 表单ID
     */
    private String formId;
}
