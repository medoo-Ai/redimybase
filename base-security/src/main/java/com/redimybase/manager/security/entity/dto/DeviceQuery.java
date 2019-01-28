package com.redimybase.manager.security.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;
import lombok.Data;

/**
 * 设备列表查询条件
 * Created by Vim 2019/1/11 19:39
 *
 * @author Mr.D
 */
@Data
public class DeviceQuery {


    /**
     * 设备编码
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备类型ID
     */
    private String type;

    /**
     * 设备类型ID
     */
    private String typeName;

    /**
     * 型号
     */
    private String model;

    /**
     * 单位
     */
    private String unit;

    /**
     * 备注
     */
    private String remark;

    /**
     * 数据状态
     */
    private Integer status;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 当前页数据大小
     */
    private Integer pageSize;

    /**
     * 根据哪个字段排序
     */
    private String orderBy;

    /**
     * 是否顺序排序(0:否-倒序,1:是-顺序)
     */
    private boolean asc;
}
