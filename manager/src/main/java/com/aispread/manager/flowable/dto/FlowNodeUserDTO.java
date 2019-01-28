package com.aispread.manager.flowable.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Vim 2019/1/8 10:51
 *
 * @author Vim
 */
@Data
public class FlowNodeUserDTO implements Serializable {

    private String id;

    private String nodeId;

    private Integer type;

    private String typeName;

    private Integer sort;

    private String value;

    private String userName;
}
