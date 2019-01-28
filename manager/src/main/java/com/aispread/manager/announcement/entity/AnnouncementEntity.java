package com.aispread.manager.announcement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.redimybase.framework.mybatis.id.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 通知公告
 * </p>
 *
 * @author Mr.D
 * @since 2019-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_announcement")
public class AnnouncementEntity extends IdEntity<String> {
  private static final long serialVersionUID = 1L;

  public static class Status{
    public static final Integer 已删除 = 0;
    public static final Integer 审核中 = 1;
    public static final Integer 已发布 = 2;
    public static final Integer 已驳回 = 3;
    public static final Integer 已下架 = 4;
  }
  public static class Model{
    public static final Integer 公司公告 = 0;
    public static final Integer 图片信息 = 1;
  }


  @TableId(value = "id", type = IdType.ID_WORKER_STR)
  private String id;


  /**
   * 公告标题
   */
  @ApiModelProperty(value = "公告标题")
  @TableField("title")
  private String title;

  /**
   * 发布模块(0:公司公告/1:首页图片)
   */
  @TableField("model")
  @ApiModelProperty(value = "发布模块")
  private Integer model;

  /**
   * 富文本内容
   */
  @TableField("content")
  @ApiModelProperty(value = "富文本内容")
  private String content;


  /**
   * 轮播图片附件ID
   */
  @TableField("attachment_id")
  @ApiModelProperty(value = "轮播图片附件ID")
  private String attachmentId;


  /**
   * 发布人
   */
  @TableField("release_user")
  @ApiModelProperty(value = "创建人ID")
  private String releaseUser;

  /**
   * 发布时间
   */
  @TableField("release_time")
  @ApiModelProperty(value = "发布时间")
  private Date releaseTime;


  /**
   * 状态(0:删除,1:草稿-默认,2:已发布)
   */
  @TableField("status")
  @ApiModelProperty(value = "公告状态(0:已删除,1:审核中(默认),2:已发布,3:已驳回,4:已下架)")
  private Integer status;

  /**
   * 创建时间
   */
  @TableField("create_time")
  @ApiModelProperty(value = "创建时间")
  private Date createTime;

  /**
   * 创建人ID
   */
  @TableField("creator_id")
  @ApiModelProperty(value = "创建人ID")
  private String creatorId;

  /**
   * 最后更新时间
   */
  @TableField("update_time")
  @ApiModelProperty(value = "更新时间")
  private Date updateTime;

  /**
   * 最后修改人ID
   */
  @TableField("updater_id")
  @ApiModelProperty(value = "更新人ID")
  private String updaterId;

  /**
   * app轮播ID
   */
  @TableField("app_banner_id")
  @ApiModelProperty(value = "app轮播ID")
  private String appBannerId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
