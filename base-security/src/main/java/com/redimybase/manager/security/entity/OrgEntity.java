package com.redimybase.manager.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;


import com.baomidou.mybatisplus.annotation.TableField;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

/**
 * <p>
 * 用户组织表
 * </p>
 *
 * @author vim
 * @since 2018-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_org")
public class OrgEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 组织名称
     */
    @TableField("name")
    private String name;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") private Date updateTime;

    /**
     * 父级ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 状态(0:删除,1:启用,2:禁用)
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建人ID
     */
    @TableField("create_id")
    private String createId;

    /**
     * 创建人
     */
    @TableField("creator")
    private String creator;

    /**
     * 修改人ID
     */
    @TableField("reviser_id")
    private String reviserId;

    /**
     * 修改人
     */
    @TableField("reviser")
    private String reviser;

    /**
     * 组织CODE
     */
    private String code;

    /**
     * 组织类型(0:部门,1:公司)
     */
    private Integer type;


    /**
     * 父级名称
     */
    @TableField(exist = false)
    private String parentName;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }


    public static class Status {
        public static final Integer 删除 = 0;
        public static final Integer 启用 = 1;
        public static final Integer 禁用 = 2;
    }

    public static class Type{
        public static final Integer 部门 = 0;
        public static final Integer 公司 = 1;
    }
}
