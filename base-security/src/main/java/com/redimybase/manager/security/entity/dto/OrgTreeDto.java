package com.redimybase.manager.security.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;
import java.util.List;

/**
 * 组织架构树
 * Created by Vim 2018/12/27 10:49
 *
 * @author Vim
 */
@Data
@ApiModel("组织架构树model")
public class OrgTreeDto implements Serializable {


    private String id;
    /**
     * 父级ID
     */
    @ApiModelProperty("父级ID")
    private String pid;

    /**
     * 组织名称
     */
    @ApiModelProperty("组织名称")
    private String name;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") private Date createTime;
    /**
     * 最后修改时间
     */
    @ApiModelProperty("最后修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") private Date updateTime;

    /**
     * 状态(0:删除,1:启用,2:禁用)
     */
    @ApiModelProperty("状态(0:删除,1:启用,2:禁用)")
    private Integer status;

    /**
     * 父级名称
     */
    @ApiModelProperty("父级名称")
    private String parentName;

    /**
     * 组织类型(0:部门,1:公司)
     */
    @ApiModelProperty("组织类型(0:部门,1:公司)")
    private Integer type;

    /**
     * 子级
     */
    @ApiModelProperty("子级")
    private List<OrgTreeDto> items;
}
