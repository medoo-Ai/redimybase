package com.aispread.admin.controller.system;

import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.entity.PositionEntity;
import com.redimybase.manager.security.mapper.PositionMapper;
import com.redimybase.manager.security.service.impl.PositionServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门职位Controller
 * Created by Vim 2019/1/6 11:59
 *
 * @author Vim
 */
@RestController
@RequestMapping("system/position")
@Api(tags = "部门职位接口")
public class PositionController extends TableController<String, PositionEntity, PositionMapper, PositionServiceImpl> {



    @Autowired
    private PositionServiceImpl service;
    @Override
    protected PositionServiceImpl getService() {
        return service;
    }
}
