package com.aispread.manager.document.service.impl;

import com.aispread.manager.document.dto.DocumentListPage;
import com.aispread.manager.document.entity.DocumentEntity;
import com.aispread.manager.document.mapper.DocumentMapper;
import com.aispread.manager.document.service.DocumentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.manager.security.entity.UserOrgEntity;
import com.redimybase.manager.security.service.UserOrgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 文档表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2018-12-26
 */
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, DocumentEntity> implements DocumentService {

    @Override
    public List<String> getNameListById(String ids) {
        return baseMapper.getNameListById(ids);
    }

    @Override
    public DocumentListPage<DocumentEntity> list(DocumentListPage page) {
        UserOrgEntity userOrgEntity = userOrgService.getOne(new QueryWrapper<UserOrgEntity>().eq("user_id", page.getUserId()).select("org_id"));
        String orgId = null;
        if (userOrgEntity != null) {
            orgId = userOrgEntity.getOrgId();
        }

        if (StringUtils.isBlank(page.getLabelId())) {
            page.setLabelId(null);
        }

        if (StringUtils.isBlank(page.getOrgId())) {
            page.setOrgId(null);
        }

        if (StringUtils.isBlank(page.getName())) {
            page.setName(null);
        }

        if (StringUtils.isBlank(page.getCreator())) {
            page.setCreator(null);
        }

        if (StringUtils.isBlank(page.getStartTime())) {
            page.setStartTime(null);
        }

        if (StringUtils.isBlank(page.getEndTime())) {
            page.setEndTime(null);
        }


        if (DocumentListPage.QueryType.组织架构.equals(page.getQueryType())) {
            return baseMapper.list(page, page.getOrgId(), page.getName(), page.getLabelId(), page.getCreator(), page.getStartTime(), page.getEndTime(), page.getUserId(), orgId, page.isAll());
        } else if (DocumentListPage.QueryType.文件夹.equals(page.getQueryType())) {
            return baseMapper.listByFolderId(page, page.getOrgId(), page.getName(), page.getLabelId(), page.getCreator(), page.getStartTime(), page.getEndTime(), page.getUserId(), orgId, page.isAll(),page.getFolderId());
        }
        return null;
    }

    @Autowired
    private UserOrgService userOrgService;
}
