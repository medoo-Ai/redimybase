package com.aispread.manager.flowable.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vim 2019/1/7 14:14
 *
 * @author Vim
 */
@Data
public class FlowNodeTreeDTO implements Serializable {


    private String id;

    private String parentId;

    private String name;

    private String type;

    private String definitionId;

    private String taskCode;

    private List<FlowNodeTreeDTO> items;
}
