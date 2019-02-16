package com.aispread.admin.controller.main;

import com.aispread.manager.announcement.entity.AnnouncementEntity;
import com.aispread.manager.announcement.service.AnnouncementService;
import com.aispread.manager.banner.entity.AppBannerEntity;
import com.aispread.manager.banner.service.AppBannerService;
import com.aispread.manager.dict.entity.DictEntity;
import com.aispread.manager.dict.service.DictService;
import com.aispread.manager.flowable.dto.*;
import com.aispread.manager.flowable.entity.ActRuTaskEntity;
import com.aispread.manager.flowable.service.ActHiProcinstService;
import com.aispread.manager.flowable.service.ActHiTaskinstService;
import com.aispread.manager.flowable.service.ActRuTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * 首页数据
 * Created by Vim 2019/1/13 15:33
 *
 * @author Vim
 */
@RestController
@RequestMapping("main")
@Api(tags = "首页数据接口")
public class MainController {

    /**
     * 获取pc端首页数据
     */
    @PostMapping("pc")
    @ApiOperation(value = "获取pc端首页数据")
    public R<?> pc() {
        Map<String, Object> data = new HashMap<>();

        DictEntity bannerSizeDict = dictService.getOne(new QueryWrapper<DictEntity>().eq("dict_key", "app_main_banner_size").select("dict_value"));

        Page<AppBannerEntity> page = new Page<>();
        page.setCurrent(1);
        page.setSize(Long.parseLong(bannerSizeDict.getDictValue()));
        page.setDesc("sort");

        //首页banner
        data.put("banners", appBannerService.page(page, new QueryWrapper<>()));

        String userId = SecurityUtil.getCurrentUserId();

        //已申请任务
        TaskPage applyTaskPage = new TaskPage();
        applyTaskPage.setCurrent(1);
        applyTaskPage.setSize(5);

        FlowApplyTaskQuery applyTaskQuery = new FlowApplyTaskQuery();
        applyTaskQuery.setUserId(userId);
        applyTaskPage.setDesc("create_time");
        TaskPage<FlowApplyTask> applyTaskMainTaskPage =  actHiTaskinstService.applyTaskList(applyTaskPage, applyTaskQuery);
        applyTaskMainTaskPage.setTaskCount(actHiProcinstService.getApplyTaskCount(userId));
        data.put("applyTaskList", applyTaskMainTaskPage);

        //待办任务
        TaskPage todoTaskPage = new TaskPage();
        todoTaskPage.setCurrent(1);
        todoTaskPage.setSize(5);

        FlowTodoTaskQuery todoTaskQuery = new FlowTodoTaskQuery();
        todoTaskQuery.setUserId(userId);
        todoTaskPage.setDesc("create_time");
        TaskPage<FlowTodoTask> todoTaskMainTaskPage =  actRuTaskService.taskList(todoTaskPage, todoTaskQuery);
        todoTaskMainTaskPage.setTaskCount(actRuTaskService.count(new QueryWrapper<ActRuTaskEntity>().eq("ASSIGNEE_", userId)));
        data.put("todoTaskList", todoTaskMainTaskPage);

        //已办任务
        TaskPage doneTaskPage = new TaskPage();
        doneTaskPage.setCurrent(1);
        doneTaskPage.setSize(5);

        FlowDoneTaskQuery donTaskQuery = new FlowDoneTaskQuery();
        donTaskQuery.setUserId(userId);
        doneTaskPage.setDesc("apply_start_time");
        TaskPage<FlowDoneTask> doneTaskMainTaskPage = actHiTaskinstService.taskList(doneTaskPage, donTaskQuery);
        doneTaskMainTaskPage.setTaskCount(actHiProcinstService.getDoneTaskCount(userId));
        data.put("doneTaskList", doneTaskMainTaskPage);

        Page<AnnouncementEntity> announcementPage = new Page<>();
        announcementPage.setCurrent(1);
        announcementPage.setSize(8);
        announcementPage.setDesc("create_time");

        data.put("announcements", announcementService.page(announcementPage, new QueryWrapper<AnnouncementEntity>().eq("status", AnnouncementEntity.Status.已发布)).getRecords());

        return new R<>(data);
    }

    /**
     * 获取APP端首页数据
     */
    @PostMapping("app")
    @ApiOperation(value = "获取APP端首页数据")
    public R<?> app() {
        Map<String, Object> data = new HashMap<>();

        DictEntity bannerSizeDict = dictService.getOne(new QueryWrapper<DictEntity>().eq("dict_key", "app_main_banner_size").select("dict_value"));

        Page<AppBannerEntity> page = new Page<>();
        page.setCurrent(1);
        page.setSize(Long.parseLong(bannerSizeDict.getDictValue()));
        page.setDesc("sort");

        //首页banner
        data.put("banners", appBannerService.page(page, new QueryWrapper<>()));

        String userId = SecurityUtil.getCurrentUserId();

        //已申请任务数量
        data.put("applyCount",actHiProcinstService.getApplyTaskCount(userId));
        //待办任务数量
        data.put("todoCount", actRuTaskService.count(new QueryWrapper<ActRuTaskEntity>().eq("ASSIGNEE_", userId)));
        //已办任务数量
        data.put("doneCount", actHiProcinstService.getDoneTaskCount(userId));

        return new R<>(data);
    }

    @Autowired
    private ActHiTaskinstService actHiTaskinstService;

    @Autowired
    private DictService dictService;

    @Autowired
    private AppBannerService appBannerService;

    @Autowired
    private ActRuTaskService actRuTaskService;

    @Autowired
    private ActHiProcinstService actHiProcinstService;

    @Autowired
    private AnnouncementService announcementService;
}
