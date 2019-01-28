package com.aispread.manager.flowable.service;

import com.aispread.manager.flowable.entity.FlowRuFormEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 流程运行表单表 服务类
 * </p>
 *
 * @author vim
 * @since 2019-01-11
 */
public interface FlowRuFormService extends IService<FlowRuFormEntity> {

    /**
     * 保存运行表单
     */
    public void saveRuForm(String businessId,String runFormId, Map<String, Object> varFields);
}
