package com.redimybase.manager.security.entity;

import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
@Getter
@Setter
@TableName("t_role")
public class RoleEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 角色代码
     */
    @TableField("code")
    private String code;
    /**
     * 角色名曾
     */
    @TableField("name")
    private String name;
    /**
     * 创建人ID
     */
    @TableField("creator_id")
    private String creatorId;
    /**
     * 创建人
     */
    @TableField("creator")
    private String creator;
    /**
     * 最后修改人ID
     */
    @TableField("reviser_id")
    private String reviserId;
    /**
     * 最后修改人
     */
    @TableField("reviser")
    private String reviser;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") private Date createTime;
    /**
     * 最后更新时间
     */
    @TableField("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") private Date updateTime;

    /**
     * 资源ID
     */
    @TableField(exist = false)
    private String resourceId;


    @Override
    public String getId() {
        return this.id;
    }
}
