package com.aispread.admin.controller.leave;

import com.aispread.manager.flowable.entity.FlowDefinitionEntity;
import com.aispread.manager.flowable.service.impl.FlowDefinitionServiceImpl;
import com.aispread.manager.leave.entity.HolidayRecordEntity;
import com.aispread.manager.leave.mapper.HolidayRecordMapper;
import com.aispread.manager.leave.service.impl.HolidayRecordServiceImpl;
import com.aispread.manager.system.entity.SystemTypeList;
import com.aispread.manager.system.mapper.SystemTypeListMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.redimybase.common.util.SequenceUtils;
import com.redimybase.flowable.service.ProcessHandleService;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @auther SyntacticSugar
 * @data 2019/1/26 0026上午 10:30
 */
@RestController
@RequestMapping("leave/days")
@Api(tags = "1请假时间操作")
public class LeaveController extends TableController<String, HolidayRecordEntity, HolidayRecordMapper, HolidayRecordServiceImpl> {

    @Autowired
    private HolidayRecordServiceImpl leaveSearvice;
    @Autowired
    private FlowDefinitionServiceImpl flowDefinitionService;


    @Autowired
    private SystemTypeListMapper  systemTypeListMapper;


    /**
     * 获取请假的时间天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @PostMapping("leaveCount")
    @ApiOperation(value = "获取请假的时间天数", notes = "传入字符串2013-10-09 15-00-00")
    public R<?> getHolidaycounts(@ApiParam(value = "请假开始时间", name = "startDate") String startDate, @ApiParam(value = "请假结束时间", name = "endDate") String endDate) {
        HolidayRecordEntity holidayRecordEntity = new HolidayRecordEntity();
        holidayRecordEntity.setStartTime(startDate);
        holidayRecordEntity.setEndTime(endDate);
        String holidays = leaveSearvice.getWorkdayTimeInMillisExcWeekendHolidays(holidayRecordEntity);
        if (StringUtils.isEmpty(holidays)) {
            return R.failed("请假天数不能为空");
        }
        return R.ok(holidays);
    }

    @Autowired
    ProcessHandleService processHandleService;

    /**
     * @param processDefinitionId
     * @param fieldValueJson
     * @param formId
     * @return
     */
    @PostMapping("startHoliday")
    @ApiOperation(value = "启动请假流程")
    public R<?> startHoliday(@ApiParam(value = "请假流程定义ID", required = true) String processDefinitionId, @ApiParam(value = "启动表单字段JSON") String fieldValueJson, @ApiParam(value = "启动表单ID", required = true) String formId) {
        //流程定义id
        processDefinitionId = "leave:1:10020";
        UserEntity currentUser = SecurityUtil.getCurrentUser();
        if (null == currentUser) {
            throw new BusinessException(com.redimybase.framework.bean.R.失败, "用户凭证过期,请尝试重新登录");
        }
        //业务实例，leaveInfo
        HolidayRecordEntity entity = new HolidayRecordEntity();
        //todo
        FlowDefinitionEntity definitionEntity = flowDefinitionService.getOne(
                new QueryWrapper<FlowDefinitionEntity>().eq("flow_definition_id", processDefinitionId).select("id,category_id"));
        entity.setId(SequenceUtils.getSequence());
        //实例 id
//        entity.setCategoryId(definitionEntity.getCategoryId());
        entity.setCategoryId(1+"");
        entity.setCreateTime(new Date());
        entity.setName("请假流程");
        leaveSearvice.save(entity);
        try {
            ProcessInstance startProcessInstance = processHandleService.startProcessInstance(currentUser, entity.getId(),
                    "qingjia:2:27504", fieldValueJson, "请假流程");
            return R.ok("流程启动成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("请假流程启动失败");
        }
    }


    /**
     * 请假申请的类型
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("holiday")
    @ApiOperation(value = "查看请假申请的类型列表")
    public R<?> holiday(@ApiParam(value = "请假人ID", required = true)@SessionAttribute("userId") Long userId,
                        @ApiParam(value = "页面的page默认0")@RequestParam(value = "page", defaultValue = "0") int page,
                        @ApiParam(value = "页面的size默认10")@RequestParam(value = "size", defaultValue = "10") int size){
        //查找请假的类型
        List<SystemTypeList> overtype=systemTypeListMapper.findByTypeModel("t_holiday");
        // 分页 ，待做
        return R.ok(overtype);
    }

    @Override
    protected HolidayRecordServiceImpl getService() {
        return leaveSearvice;
    }
}
