package com.aispread.admin.controller.leave;

import com.aispread.manager.leave.entity.StatutoryHolidayEntity;
import com.aispread.manager.leave.mapper.StatutoryHolidayMapper;
import com.aispread.manager.leave.service.impl.StatutoryServiceImpl;
import com.baomidou.mybatisplus.extension.api.R;
import com.redimybase.framework.web.TableController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther SyntacticSugar
 * @data 2019/1/26 0026上午 10:30
 */
@RestController
@RequestMapping("leave/statutory")
@Api(tags = "1法定节假日操作")
public class StatutoryHolidayController extends TableController<String, StatutoryHolidayEntity, StatutoryHolidayMapper, StatutoryServiceImpl> {

    @Autowired
    private StatutoryServiceImpl statutoryService;
    @Override
    protected StatutoryServiceImpl getService() {
        return statutoryService;
    }

    /**
     * 获取国家法定节假日
     * @return
     */
    @PostMapping("getStutoryHolidays")
    @ApiOperation(value = "获取国家法定节假日,json串",notes = "0是工作日，1法定休息日")
    public String getStatutoryHolidays() {
        try {
            String statutoryHolidays = statutoryService.getStatutoryHolidays();
            return statutoryHolidays;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "获取法定节日失败";
    }

    /**
     * 保存国家法定节假日
     * 前端或者后端定时任务保存拉取
     */
    @GetMapping("saveStutoryHolidays")
    @ApiOperation(value = "保存国家法定节假日",notes = "0是工作日，1法定休息日")
    public R<?> saveStatutoryHolidays() {
        boolean b = statutoryService.saveStatutoryHolidays();
        if (b) {
            return R.ok(b);
        }
        return R.failed("保存失败");
    }

//    @ApiParam(value = "",name = "")
    /**
     * 更新国家法定节假日
     * @return
     */
    @GetMapping("updateStutoryHolidays")
    @ApiOperation(value = "更新国家法定节假日",notes = "0是工作日，1法定休息日")
    public R<?> updateStatutoryHolidays() {
        boolean b = statutoryService.updateStatutoryHolidays();
        if (b) {
            return R.ok(b);
        }
        return R.failed("更新失败");
    }

}
