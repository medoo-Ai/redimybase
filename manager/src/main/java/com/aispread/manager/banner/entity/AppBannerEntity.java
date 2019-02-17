package com.aispread.manager.banner.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * APP轮播图表
 * </p>
 *
 * @author vim
 * @since 2019-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_app_banner")
public class AppBannerEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    public static class Status{
        public static final Integer 已删除 = 0;
        public static final Integer 审核中 = 1;
        public static final Integer 已发布 = 2;
        public static final Integer 已驳回 = 3;
        public static final Integer 已下架 = 4;
    }

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 图片链接
     */
    @TableField("url")
    private String url;

    /**
     * 公告ID
     */
    @TableField("announcement_id")
    private String announcementId;

    /**
     * 简称
     */
    @TableField("short_name")
    private String shortName;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 排序
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
