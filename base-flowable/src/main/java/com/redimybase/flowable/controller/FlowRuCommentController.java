package com.redimybase.flowable.controller;

import com.aispread.manager.flowable.entity.FlowRuCommentEntity;
import com.aispread.manager.flowable.mapper.FlowRuCommentMapper;
import com.aispread.manager.flowable.service.impl.FlowRuCommentServiceImpl;
import com.redimybase.framework.web.TableController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *流程运行留言Controller
 * Created by Vim 2019/1/11 18:02
 *
 * @author Vim
 */
@RestController
@RequestMapping("flowable/comment")
@Api(tags = "流程运行留言接口")
public class FlowRuCommentController extends TableController<String, FlowRuCommentEntity, FlowRuCommentMapper, FlowRuCommentServiceImpl> {

    @Autowired
    private FlowRuCommentServiceImpl service;


    @Override
    protected FlowRuCommentServiceImpl getService() {
        return service;
    }
}
