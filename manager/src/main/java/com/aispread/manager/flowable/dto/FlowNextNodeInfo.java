package com.aispread.manager.flowable.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 流程下一节点信息
 * Created by Vim 2019/1/10 12:09
 *
 * @author Vim
 */
@Data
public class FlowNextNodeInfo implements Serializable {

    /**
     * 下一节点审批人
     */
    private String nextAssignee;

    /**
     * 下一节点名称
     */
    private String nextNodeName;
}
