package com.aispread.admin.controller.document;

import com.aispread.manager.document.entity.DocumentLabelEntity;
import com.aispread.manager.document.mapper.DocumentLabelMapper;
import com.aispread.manager.document.service.impl.DocumentLabelServiceImpl;
import com.redimybase.framework.web.TableController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文档标签接口
 * Created by Vim 2019/1/25 15:35
 *
 * @author Vim
 */
@RestController
@RequestMapping("document/label")
public class DocumentLabelController extends TableController<String, DocumentLabelEntity, DocumentLabelMapper, DocumentLabelServiceImpl> {


    @Autowired
    private DocumentLabelServiceImpl service;

    @Override
    protected DocumentLabelServiceImpl getService() {
        return service;
    }
}
