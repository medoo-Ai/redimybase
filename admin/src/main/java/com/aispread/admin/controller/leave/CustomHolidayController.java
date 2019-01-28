package com.aispread.admin.controller.leave;

import com.aispread.manager.leave.entity.CustomHolidayEntity;
import com.aispread.manager.leave.mapper.CustomHolidayMapper;
import com.aispread.manager.leave.service.impl.CustomHolidayImpl;
import com.baomidou.mybatisplus.extension.api.R;
import com.redimybase.framework.web.TableController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther SyntacticSugar
 * @data 2019/1/26 0026上午 10:30
 */
@RestController
@RequestMapping("leave/custom")
@Api(tags = "1自定义节假日操作")
public class CustomHolidayController extends TableController<String, CustomHolidayEntity, CustomHolidayMapper, CustomHolidayImpl> {

    @Autowired
    private CustomHolidayImpl customHolidayService;
    @Override
    protected CustomHolidayImpl getService() {
        return customHolidayService;
    }

    /**
     * 添加自定义的假期
     * @param newCustomHoliday
     * @return
     */
    @PostMapping("addCustomHoliday")
    @ApiOperation(value = "添加自定义的假期,默认status为1 格式yyyy-MM-dd ",notes = "状态status休息日1，工作日0")
    public R<?> addHoliday(@ApiParam(name = "newCustomHoliday",value = "要添加自定义的假期") String newCustomHoliday) {
        try {
            customHolidayService.addHoliday(newCustomHoliday);
            return R.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("已存在当前自定义的假期");
        }

    }

    /**
     * 删除自定义的假期
     * @param customHoliday
     */
    @DeleteMapping("remove")
    @ApiOperation(value = "删除自定义的假期,status为1的是假期",notes = "状态status休息日1，工作日0")
    public R<?> removeHolidayByDay(@ApiParam(name = "customHoliday",value = "要删除自定义的假期")String customHoliday){
        try {
            customHolidayService.removeHolidayByDay(customHoliday);
            return R.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("要删除当前自定义的假期不存在");
        }
    }

    /**
     * 获取自定义休息的列表
     * @return
     */
    @PostMapping("getholidays")
    @ApiOperation(value = "获取自定义假期的列表,status为1的是假期",notes = "状态status休息日1，工作日0")
    public ResponseEntity<List<CustomHolidayEntity>> getHolidays(){
        List<CustomHolidayEntity> holidays = customHolidayService.getHolidays();
        if (holidays.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(holidays);
    }

    /**
     * 获取自定义工作日的列表
     * @return
     */
    @PostMapping("getworkdays")
    @ApiOperation(value = "获取自定义工作日的列表,status为0的是工作日",notes = "状态status休息日1，工作日0")
    public ResponseEntity<List<CustomHolidayEntity>> getWorkdays(){
        List<CustomHolidayEntity> workdays = customHolidayService.getWorkdays();
        if (workdays.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(workdays);
    }

    /**
     * 更新自定义的节假日
     * @param customHoliday
     * @param newCustomHoliday
     * @param newStatus
     */
    @PostMapping("update")
    @ApiOperation(value = "更新自定义的节假日,status为1的是假期",notes = "状态status休息日1，工作日0")
    public R<?> updateCustomHolidayByday(@ApiParam(name = "customHoliday",value = "要更新的自定义假期")String customHoliday,@ApiParam(name = "newCustomHoliday",value = "更新后的值")String newCustomHoliday,@ApiParam(name = "newStatus",value = "更新后的状态码")int newStatus ){
        // 先查询status
        try {
            customHolidayService.updateCustomHolidayByday(customHoliday, newCustomHoliday, newStatus);
            return R.ok("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("更新失败");
        }
    }

    /**
     * 获取所有的自定义的列表
     * @return
     */
    @PostMapping("getAll")
    @ApiOperation(value = "获取所有的自定义的列表,status为1的是假期",notes = "状态status休息日1，工作日0")
    public R<?> getAllCustomdays(){
        // 先查询status
        try {
            List<CustomHolidayEntity> allCustomdays = customHolidayService.getAllCustomdays();
            return R.ok(allCustomdays);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("获取失败");
        }
    }



}
