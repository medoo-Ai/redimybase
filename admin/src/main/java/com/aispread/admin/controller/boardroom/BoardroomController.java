package com.aispread.admin.controller.boardroom;

import com.aispread.manager.boardroom.entity.BoardroomEntity;
import com.aispread.manager.boardroom.mapper.BoardroomMapper;
import com.aispread.manager.boardroom.service.impl.BoardroomServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
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
import java.util.Date;

/**
 * 会议室Controller
 * Created by Vim 2018/12/28 13:16
 *
 * @author Vim
 */
@RestController
@RequestMapping("boardroom")
@Api(tags = "会议室管理接口")
public class BoardroomController extends TableController<String, BoardroomEntity, BoardroomMapper, BoardroomServiceImpl> {


    @Override
    @ApiOperation("获取会议室列表")
    public Object query(HttpServletRequest request) {
        TableModel<BoardroomEntity> model = new TableModel<>();
        Page<BoardroomEntity> page = (Page<BoardroomEntity>) buildPageRequest(request);
        if (page == null) {
            page = new Page<>(1, 10);
        }
        QueryWrapper<BoardroomEntity> queryWrapper = buildWrapper(getQueryColumn(request), getQueryValue(request));
        if (null == queryWrapper) {
            queryWrapper = new QueryWrapper<>();
        }

        queryWrapper.and(i -> i.eq("status", BoardroomEntity.Status.正常));

        model.setData(getService().page(page, buildPageWrapper(queryWrapper, getQueryKey(request), getQuerySearch(request))));
        return model;
    }

    @Override
    public void beforeSave(BoardroomEntity entity) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();

        if (null == currentUser) {
            throw new BusinessException(R.失败, "用户凭证过期,请尝试重新登录");
        }
        if (StringUtils.isBlank(entity.getId())) {
            //新增
            entity.setCreateTime(new Date());
            entity.setCreator(currentUser.getUserName());
            entity.setCreatorId(currentUser.getId());
            entity.setStatus(BoardroomEntity.Status.正常);
        } else {
            //修改
            entity.setUpdateTime(new Date());
            entity.setReviser(currentUser.getUserName());
            entity.setReviserId(currentUser.getId());
        }
    }

    @Override
    @ApiOperation("新增会议室")
    public R<?> save(BoardroomEntity entity) {
        this.beforeSave(entity);
        return super.save(entity);
    }

    @Override
    @ApiOperation("删除会议室")
    public R<?> delete(String id) {
        BoardroomEntity entity = new BoardroomEntity();
        entity.setId(id);
//        entity.setStatus(BoardroomEntity.Status.删除);
        if(service.removeById(entity)){
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Override
    @ApiOperation("批量删除会议室")
    public R<?> deleteBatchIds(String ids) {
        String[] idArray = StringUtils.split(ids, ",");
        for (String id : idArray) {
            this.delete(id);
        }
        return R.ok();
    }

    /**
     * 修改
     * @param entity 会议室对象
     * @return
     */
    @PostMapping(value = "update")
    @ApiOperation("修改会议室")
    public R<?> update(BoardroomEntity entity) {
        this.beforeSave(entity);
        if(service.updateById(entity)){
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Autowired
    private BoardroomServiceImpl service;

    @Override
    protected BoardroomServiceImpl getService() {
        return service;
    }
}
