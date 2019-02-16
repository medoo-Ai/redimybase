package com.aispread.admin.controller.me;

import com.aispread.manager.flowable.entity.ActRuTaskEntity;
import com.aispread.manager.flowable.service.ActHiProcinstService;
import com.aispread.manager.flowable.service.ActRuTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人中心Controller
 * Created by Vim 2019/2/15 10:22
 *
 * @author Vim
 */
@RestController
@RequestMapping("me")
@Api(tags = "个人中心接口")
public class MeController {


    @ApiOperation("个人中心主要数据")
    @PostMapping("main")
    public R<?> mainData() {
        Map<String, Object> data = new HashMap<>();

        String currentUserId = SecurityUtil.getCurrentUserId();

        //待办事项数量
        data.put("todoCount", actRuTaskService.count(new QueryWrapper<ActRuTaskEntity>().eq("ASSIGNEE_", currentUserId)));
        //已办事项数量
        data.put("doneCount", actHiProcinstService.getDoneTaskCount(currentUserId));
        //已申请事项数量
        data.put("applyCount", actHiProcinstService.getApplyTaskCount(currentUserId));

        return new R<>(data);
    }


    @Autowired
    private ActRuTaskService actRuTaskService;

    @Autowired
    private ActHiProcinstService actHiProcinstService;
}
