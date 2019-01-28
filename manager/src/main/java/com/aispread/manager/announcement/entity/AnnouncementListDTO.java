package com.aispread.manager.announcement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class AnnouncementListDTO {
  /**
   * 公告ID
   */
  @ApiModelProperty(value = "公告ID")
  private String id;

  /**
   * 公告标题
   */
  @ApiModelProperty(value = "公告标题")
  private String title;

  /**
   * 发布模块(0:公司公告/1:首页图片)
   */
  @ApiModelProperty(value = "发布模块")
  private Integer model;

  /**
   * 富文本内容
   */
  @ApiModelProperty(value = "富文本内容")
  private String content;

  /**
   * 轮播图片附件ID
   */
  @ApiModelProperty(value = "轮播图片附件ID")
  private String attachmentId;

  /**
   * 发布人
   */
  @ApiModelProperty(value = "发布人ID")
  private String releaseUser;

  /**
   * 发布人名称
   */
  @ApiModelProperty(value = "发布人名称")
  private String releaseUserName;

  /**
   * 发布时间
   */
  @ApiModelProperty(value = "发布时间")
  private Date releaseTime;


  /**
   * 状态(0:删除,1:草稿-默认,2:已发布)
   */
  @ApiModelProperty(value = "公告状态(1:审核中(默认),2:已发布,3:已驳回,4:已下架)")
  private Integer status;

  /**
   * 创建时间
   */
  @ApiModelProperty(value = "创建时间")
  private Date createTime;

  /**
   * 创建人ID
   */
  @ApiModelProperty(value = "创建人ID")
  private String creatorId;

  /**
   * 创建人名称
   */
  @ApiModelProperty(value = "创建人名称")
  private String creatorName;

  /**
   * 最后更新时间
   */
  @ApiModelProperty(value = "更新时间")
  private Date updateTime;

  /**
   * 最后修改人ID
   */
  @ApiModelProperty(value = "更新人ID")
  private String updaterId;

  /**
   * 最后修改人名称
   */
  @ApiModelProperty(value = "最后修改人名称")
  private String updateName;

  /**
   * 轮播图片排序
   */
  @ApiModelProperty(value = "轮播图片排序")
  private Integer imgSort;

}
