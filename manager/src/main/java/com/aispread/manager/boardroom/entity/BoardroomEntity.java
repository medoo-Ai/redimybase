package com.aispread.manager.boardroom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会议室表
 * </p>
 *
 * @author vim
 * @since 2018-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_boardroom")
public class BoardroomEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 会议室名称
     */
    @TableField("name")
    private String name;

    /**
     * 所属组织ID
     */
    @TableField("org_id")
    private String orgId;

    /**
     * 会议室地点
     */
    @TableField("address")
    private String address;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建人ID
     */
    @TableField("creator_id")
    private String creatorId;

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
     * 创建人
     */
    @TableField("creator")
    private String creator;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态(0:正常,1:删除)
     */
    @TableField("status")
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Status {
        public static final Integer 正常 = 0;
        public static final Integer 删除 = 1;
    }
}
