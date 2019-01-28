package com.aispread.admin.controller.system;

import com.aispread.manager.dict.entity.DictEntity;
import com.aispread.manager.dict.mapper.DictMapper;
import com.aispread.manager.dict.service.impl.DictServiceImpl;
import com.redimybase.framework.web.TableController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据字典Controller
 * Created by Vim 2018/12/27 14:15
 *
 * @author Vim
 */
@RestController
@RequestMapping("dict")
@Api(tags = "数据字典接口")
public class DictController extends TableController<String, DictEntity, DictMapper, DictServiceImpl> {


    @Autowired
    private DictServiceImpl service;

    @Override
    protected DictServiceImpl getService() {
        return service;
    }
}
