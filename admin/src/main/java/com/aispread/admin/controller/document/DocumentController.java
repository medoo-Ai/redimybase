package com.aispread.admin.controller.document;

import com.aispread.manager.document.dto.DocumentListPage;
import com.aispread.manager.document.entity.DocumentEntity;
import com.aispread.manager.document.entity.DocumentLogEntity;
import com.aispread.manager.document.mapper.DocumentMapper;
import com.aispread.manager.document.service.DocumentLogService;
import com.aispread.manager.document.service.impl.DocumentServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;
import java.util.List;

/**
 * 文档Controller
 * Created by Vim 2018/12/26 16:21
 *
 * @author Vim
 */
@RestController
@RequestMapping("document")
@Api(tags = "知识库接口")
public class DocumentController extends TableController<String, DocumentEntity, DocumentMapper, DocumentServiceImpl> {

    @Override
    public Object query(HttpServletRequest request) {
        TableModel<DocumentEntity> model = new TableModel<>();
        Page<DocumentEntity> page = (Page<DocumentEntity>) buildPageRequest(request);
        if (page == null) {
            page = new Page<>(1, 8);
        }
        QueryWrapper<DocumentEntity> queryWrapper = buildWrapper(getQueryColumn(request), getQueryValue(request));
        if (null == queryWrapper) {
            queryWrapper = new QueryWrapper<>();
        }
        queryWrapper.and(i -> i.eq("status", DocumentEntity.Status.正常));

        model.setData(getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request))));
        return model;
    }

    @PostMapping("list")
    @ApiOperation(value = "获取知识库列表",notes = "文件下载的话调用文件下载的接口传attachmentId附件ID进去进行下载")
    public R<?> list(DocumentListPage page) {
        if (page.getQueryType() == null) {
            return R.fail("缺少参数[queryType],请尝试刷新页面再试");
        }
        return new R<>(service.list(page));
    }

    @Override
    public R<?> detail(String id) {
        return new R<>(service.getOne(new QueryWrapper<DocumentEntity>().eq("id", id).eq("status", DocumentEntity.Status.正常)));
    }

    @Override
    public R<?> save(DocumentEntity entity) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();

        if (null == currentUser) {
            return R.fail("用户凭证过期,请尝试重新登录");
        }


        DocumentLogEntity logEntity = new DocumentLogEntity();

        if (entity.getViewRange() == null) {
            entity.setViewRange(DocumentEntity.ViewRange.个人);
        }
        if (StringUtils.isBlank(entity.getId())) {
            entity.setStatus(DocumentEntity.Status.正常);
            entity.setCreateTime(new Date());
            entity.setCreator(currentUser.getUserName());
            entity.setCreatorId(currentUser.getId());
            service.save(entity);
            logEntity.setDocumentId(entity.getId());
            logEntity.setName("添加文档");
            logEntity.setContent(String.format("%s 添加了文档[%s]", currentUser.getUserName(), entity.getName()));
        } else {
            DocumentEntity documentEntity = service.getById(entity.getId());
            service.updateById(documentEntity);
            logEntity.setDocumentId(documentEntity.getId());
            logEntity.setName("修改文档");
            logEntity.setContent(String.format("%s 修改了文档[%s]", currentUser.getUserName(), entity.getName()));
        }

        logEntity.setCreator(currentUser.getUserName());
        logEntity.setCreatorId(currentUser.getId());
        logEntity.setDocumentName(entity.getName());
        logEntity.setCreateTime(new Date());

        logService.save(logEntity);
        return R.ok();
    }

    @Override
    public R<?> delete(String id) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();

        if (null == currentUser) {
            return R.fail("用户凭证过期,请尝试重新登录");
        }
        DocumentEntity documentEntity = service.getOne(new QueryWrapper<DocumentEntity>().eq("id", id).select("id,status,name"));
        documentEntity.setStatus(DocumentEntity.Status.删除);
        service.updateById(documentEntity);

        DocumentLogEntity logEntity = new DocumentLogEntity();

        logEntity.setCreator(currentUser.getUserName());
        logEntity.setCreatorId(currentUser.getId());
        logEntity.setDocumentName(documentEntity.getName());
        logEntity.setContent(String.format("%s 删除了文档[%s]", currentUser.getUserName(), documentEntity.getName()));
        logEntity.setName("删除文档");
        logEntity.setCreateTime(new Date());
        logEntity.setDocumentId(id);

        logService.save(logEntity);
        return R.ok();
    }

    @Override
    public R<?> deleteBatchIds(String ids) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();

        if (null == currentUser) {
            return R.fail("用户凭证过期,请尝试重新登录");
        }

        String[] splitStr = StringUtils.split(ids, ",");

        List<String> nameList = new ArrayList<>();
        for (String id : splitStr) {
            DocumentEntity documentEntity = service.getOne(new QueryWrapper<DocumentEntity>().eq("id", id).select("id,status,name"));
            documentEntity.setStatus(DocumentEntity.Status.删除);
            service.updateById(documentEntity);
            nameList.add(documentEntity.getName());
        }

        DocumentLogEntity logEntity = new DocumentLogEntity();

        String nameListStr = StringUtils.join(nameList);

        logEntity.setCreator(currentUser.getUserName());
        logEntity.setCreatorId(currentUser.getId());
        logEntity.setDocumentName(nameListStr);
        logEntity.setContent(String.format("%s 删除了文档[%s]", currentUser.getUserName(), nameListStr));
        logEntity.setName("删除文档");
        logEntity.setCreateTime(new Date());
        logEntity.setDocumentId(ids);

        logService.save(logEntity);

        return R.ok();
    }

    @Autowired
    private DocumentLogService logService;

    @Autowired
    private DocumentServiceImpl service;

    @Override
    protected DocumentServiceImpl getService() {
        return service;
    }
}
