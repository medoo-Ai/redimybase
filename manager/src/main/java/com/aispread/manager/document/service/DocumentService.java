package com.aispread.manager.document.service;

import com.aispread.manager.document.dto.DocumentListPage;
import com.aispread.manager.document.entity.DocumentEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文档表 服务类
 * </p>
 *
 * @author vim
 * @since 2018-12-26
 */
public interface DocumentService extends IService<DocumentEntity> {

    public List<String> getNameListById(String ids);

    /**
     * 获取文档库列表
     */
    public DocumentListPage<DocumentEntity> list(DocumentListPage page);

}
