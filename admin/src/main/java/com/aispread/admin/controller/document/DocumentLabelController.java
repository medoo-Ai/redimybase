package com.aispread.admin.controller.document;

import com.aispread.manager.document.entity.DocumentLabelEntity;
import com.aispread.manager.document.mapper.DocumentLabelMapper;
import com.aispread.manager.document.service.impl.DocumentLabelServiceImpl;
import com.redimybase.framework.web.TableController;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 文档标签接口
 * Created by Vim 2019/1/25 15:35
 *
 * @author Vim
 */
@RestController
@RequestMapping("document/label")
@Api(tags = "知识库-文档标签接口")
public class DocumentLabelController extends TableController<String, DocumentLabelEntity, DocumentLabelMapper, DocumentLabelServiceImpl> {


    @Override
    public void beforeSave(DocumentLabelEntity entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setCreateId(SecurityUtil.getCurrentUserId());
            entity.setCreateTime(new Date());
        }
    }

    @Autowired
    private DocumentLabelServiceImpl service;

    @Override
    protected DocumentLabelServiceImpl getService() {
        return service;
    }
}
