package com.aispread.manager.document.mapper;

import com.aispread.manager.document.dto.DocumentListPage;
import com.aispread.manager.document.entity.DocumentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 文档表 Mapper 接口
 * </p>
 *
 * @author vim
 * @since 2018-12-26
 */
public interface DocumentMapper extends BaseMapper<DocumentEntity> {


    @Select("select name from t_document where ids in (#{ids})")
    public List<String> getNameListById(@Param("ids") String ids);

    public DocumentListPage<DocumentEntity> list(DocumentListPage page, @Param("orgId") String orgId, @Param("name") String name, @Param("labelId") String labelId, @Param("creator") String creator, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("userId") String userId, @Param("userOrgId") String userOrgId,@Param("all") boolean all);

    public DocumentListPage<DocumentEntity> listByFolderId(DocumentListPage page, @Param("orgId") String orgId, @Param("name") String name, @Param("labelId") String labelId, @Param("creator") String creator, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("userId") String userId, @Param("userOrgId") String userOrgId,@Param("all") boolean all,@Param("folderId") String folderId);

}
