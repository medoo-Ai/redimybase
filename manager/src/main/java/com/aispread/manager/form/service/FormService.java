package com.aispread.manager.form.service;

import com.aispread.manager.form.entity.FormEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.redimybase.framework.bean.R;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vim
 * @since 2019-01-02
 */
public interface FormService extends IService<FormEntity> {


    /**
     * 保存表单
     */
    public R<?> saveForm(FormEntity entity);

    public FormEntity getFormByNodeId(@Param("nodeId") String nodeId);
}
