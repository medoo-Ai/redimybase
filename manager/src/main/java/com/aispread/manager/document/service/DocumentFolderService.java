package com.aispread.manager.document.service;

import com.aispread.manager.document.entity.DocumentFolderEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.redimybase.framework.bean.R;

/**
 * <p>
 * 文档目录表 服务类
 * </p>
 *
 * @author vim
 * @since 2018-12-26
 */
public interface DocumentFolderService extends IService<DocumentFolderEntity> {

    /**
     * 获取类别树
     */
    public R<?> getFolderTree(String name);
}
