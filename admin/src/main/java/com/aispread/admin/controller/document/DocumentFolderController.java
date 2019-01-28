package com.aispread.admin.controller.document;

import com.aispread.manager.document.entity.DocumentFolderEntity;
import com.aispread.manager.document.mapper.DocumentFolderMapper;
import com.aispread.manager.document.service.impl.DocumentFolderServiceImpl;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.web.TableController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文档目录Controller
 * Created by Vim 2018/12/26 20:26
 *
 * @author Vim
 */
@RestController
@RequestMapping("document/folder")
@Api(tags = "知识库-目录接口")
public class DocumentFolderController extends TableController<String, DocumentFolderEntity, DocumentFolderMapper, DocumentFolderServiceImpl> {

    @ApiOperation("获取文档目录树")
    @PostMapping("tree")
    public R<?> folderTree(@ApiParam("目录名称(用于模糊搜索)") String name) {
        return service.getFolderTree(name);
    }
    @Autowired
    private DocumentFolderServiceImpl service;

    @Override
    protected DocumentFolderServiceImpl getService() {
        return service;
    }
}
