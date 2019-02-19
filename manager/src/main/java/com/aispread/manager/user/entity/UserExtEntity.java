package com.aispread.manager.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户信息拓展表
 * </p>
 *
 * @author vim
 * @since 2019-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user_ext")
public class UserExtEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 所在办公室
     */
    @TableField("office")
    private String office;

    /**
     * 工位
     */
    @TableField("station")
    private String station;

    /**
     * 家庭住址
     */
    @TableField("address")
    private String address;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 入职时间
     */
    private Date hireDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
