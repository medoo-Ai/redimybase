package com.aispread.manager.form.mapper;

import com.aispread.manager.flowable.entity.FlowFormEntity;
import com.aispread.manager.form.entity.FormEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2019-01-02
 */
public interface FormMapper extends BaseMapper<FormEntity> {
    /**
     * 根据节点ID获取对应节点表单
     */
    @Select("select t1.name,t1.id,t1.json,t1.form_key from t_form t1 left join t_flow_form t2 on t1.form_key=t2.form_key left join t_flow_node t3 on t2.node_id=t3.id where node_id=#{nodeId}")
    public FormEntity getFormByNodeId(@Param("nodeId") String nodeId);
}
