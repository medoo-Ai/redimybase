package com.redimybase.flowable.listener.business;

import com.redimybase.flowable.bean.CustomFlow;

/**
 * 业务流程监听器
 * Created by Vim 2019/1/26 21:13
 *
 * @author Vim
 */
public interface FlowBusinessListener {

    /**
     * 流程通过监听
     * @param customFlow 相关流程信息
     */
    public void onPass(CustomFlow customFlow);

    /**
     * 流程驳回监听
     * @param customFlow 相关流程信息
     */
    public void onReject(CustomFlow customFlow);
}
