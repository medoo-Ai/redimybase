package com.aispread.manager.announcement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnnouncementSaveDTO {

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
   * 轮播图片排序
   */
  @ApiModelProperty(value = "轮播图片排序")
  private Integer imgSort;

  /**
   * 是否置顶
   */
  @ApiModelProperty(value = "是否置顶")
  private String istop;

  /**
   * 轮播图片ID
   */
  @ApiModelProperty(value = "轮播图片ID")
  private String appBannerId;

  /**
   * 公告状态(0:草稿,1:发布)
   */
  @ApiModelProperty(value = "公告状态(0:草稿,1:发布)")
  private Integer status;


}
