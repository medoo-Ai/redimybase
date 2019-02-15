package com.aispread.manager.system.mapper;

import com.aispread.manager.system.entity.SystemTypeList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @auther SyntacticSugar
 * @data 2019/2/15 0015上午 11:03
 */
public interface SystemTypeListMapper extends BaseMapper<SystemTypeList> {

    //根据模块名查找到类型集合
    List<SystemTypeList> findByTypeModel(String typeModel);


    //根据模块名字查找  具体的唯一的类型
    SystemTypeList findByTypeModelAndTypeName(String typeModel,String typeName);

}
