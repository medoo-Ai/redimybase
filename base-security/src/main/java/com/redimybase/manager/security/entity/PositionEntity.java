package com.redimybase.manager.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 岗位表
 * </p>
 *
 * @author vim
 * @since 2019-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_position")
public class PositionEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 职位名称
     */
    @TableField("name")
    private String name;

    /**
     * 所属部门ID
     */
    @TableField("org_id")
    private String orgId;

    /**
     * 所属部门名称
     */
    @TableField("org_name")
    private String orgName;

    /**
     * 父级ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 岗位级别
     */
    @TableField("level_id")
    private String levelId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Level{
        public static final Integer 部门负责人 = 1;
        public static final Integer 员工 = 2;
        public static final Integer 总经理 = 3;
    }

}
