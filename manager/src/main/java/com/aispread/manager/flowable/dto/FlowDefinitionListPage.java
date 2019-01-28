package com.aispread.manager.flowable.dto;

import com.redimybase.framework.mybatis.bean.PlusPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 流程模板查询条件
 * Created by Vim 2019/1/25 13:45
 *
 * @author Vim
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("流程模板查询条件")
public class FlowDefinitionListPage<T> extends PlusPage<T> {

    @ApiModelProperty("组织ID")
    private String ordId;

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("模板状态(0:未发布,1:已发布)")
    private Integer status;

    @ApiModelProperty("创建开始时间")
    private String startTime;

    @ApiModelProperty("创建结束时间")
    private String endTime;

}
