package com.aispread.manager.flowable.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vim 2019/1/5 11:49
 *
 * @author Vim
 */
@Data
public class FlowCategoryDTO implements Serializable {


    private String id;

    private String parentId;

    private String name;

    private Integer sort;

    private String description;

    private String categoryKey;

    private List<FlowCategoryDTO> items;

}
