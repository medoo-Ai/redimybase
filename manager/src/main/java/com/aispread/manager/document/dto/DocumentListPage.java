package com.aispread.manager.document.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redimybase.framework.mybatis.bean.PlusPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文档列表查询条件
 * Created by Vim 2019/1/25 18:00
 *
 * @author Vim
 */
@Data
public class DocumentListPage<T> extends PlusPage<T> {

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty("文档名称")
    private String name;

    @ApiModelProperty("标签ID")
    private String labelId;

    @ApiModelProperty("上传人")
    private String creator;

    @ApiModelProperty("上传开始时间")
    private String startTime;

    @ApiModelProperty("上传结束时间")
    private String endTime;

    @ApiModelProperty(value = "用户ID", required = true)
    private String userId;

    @ApiModelProperty("是否查询全部")
    private boolean all;

    @ApiModelProperty("文件夹ID")
    private String folderId;

    @ApiModelProperty(value = "查询类型(1:按组织架构查询,2:按文件夹查询)",required = true)
    private Integer queryType;

    public static class QueryType{
        public static final Integer 组织架构 = 1;
        public static final Integer 文件夹 = 2;
    }
}
