package com.aispread.manager.document.dto;

import com.aispread.manager.flowable.dto.FlowCategoryDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 文档目录树
 * Created by Vim 2019/1/25 16:46
 *
 * @author Vim
 */
@Data
public class DocumentFolderTree implements Serializable {
    private String id;

    private String parentId;

    private String name;

    private Integer sort;

    private String categoryKey;

    private List<DocumentFolderTree> items;
}
