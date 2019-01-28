package com.aispread.manager.flowable.service;

import com.aispread.manager.flowable.entity.FlowUserEntity;
import com.aispread.manager.flowable.dto.FlowNodeUserDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
public interface FlowUserService extends IService<FlowUserEntity> {

    public List<FlowNodeUserDTO> getFlowUserByNodeId(String nodeId);

}
