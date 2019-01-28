package com.redimybase.flowable.listener.task.contants;

/**
 * 流程固定变量
 * Created by Vim 2019/1/26 19:15
 *
 * @author Vim
 */
public class FlowVariableConstants {
    public static final String 审批状态 = "^status";
    public static final String 业务类型 = "^businessType";
    public static final String 业务单号 = "^businessId";

    public static class TaskStatus{
        public static final Integer 审批中 = 1;
        public static final Integer 已通过 = 2;
        public static final Integer 已拒绝 = 3;
    }
}
