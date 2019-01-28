package com.aispread.manager.flowable.service.impl;

import com.aispread.manager.flowable.entity.FlowUserEntity;
import com.aispread.manager.flowable.dto.FlowNodeUserDTO;
import com.aispread.manager.flowable.mapper.FlowUserMapper;
import com.aispread.manager.flowable.service.FlowUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
@Service
public class FlowUserServiceImpl extends ServiceImpl<FlowUserMapper, FlowUserEntity> implements FlowUserService {

    @Override
    public List<FlowNodeUserDTO> getFlowUserByNodeId(String nodeId) {
        List<FlowNodeUserDTO> userDTOList = baseMapper.getFlowUserByNodeId(nodeId);
        setFlowNodeUserType(userDTOList);
        return userDTOList;
    }


    /**
     * 给用户类型赋值
     */
    private void setFlowNodeUserType(List<FlowNodeUserDTO> dtos) {
        for (FlowNodeUserDTO flowNodeUserDTO : dtos) {
            switch (flowNodeUserDTO.getType()) {
                case FlowUserEntity.Type.USER_GROUP:
                    flowNodeUserDTO.setTypeName("用户组");
                    break;

                case FlowUserEntity.Type.ORTHER_NODE:
                    flowNodeUserDTO.setTypeName("其他节点");
                    break;

                case FlowUserEntity.Type.FROM_FORM:
                    flowNodeUserDTO.setTypeName("来自表单");
                    break;

                case FlowUserEntity.Type.LEADERSHIP:
                    flowNodeUserDTO.setTypeName("上级领导");
                    break;

                case FlowUserEntity.Type.INITIATOR:
                    flowNodeUserDTO.setTypeName("发起人");
                    break;

                case FlowUserEntity.Type.USER:
                    flowNodeUserDTO.setTypeName("普通用户");
                    break;

                default:
                        break;
            }
        }
    }
}
