package com.aispread.admin.controller.leave;

import com.aispread.manager.leave.entity.HolidayRecordEntity;
import com.aispread.manager.leave.mapper.HolidayRecordMapper;
import com.aispread.manager.leave.service.impl.HolidayRecordServiceImpl;
import com.baomidou.mybatisplus.extension.api.R;
import com.redimybase.framework.web.TableController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    /**
     * 获取请假的时间天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @PostMapping("leaveCount")
    @ApiOperation(value = "获取请假的时间天数",notes = "传入字符串2013-10-09 15-00-00")
    public R<?> getHolidaycouts(@ApiParam(value = "请假开始时间",name ="startDate" ) String startDate, @ApiParam(value = "请假结束时间",name ="endDate" )String endDate) {
        HolidayRecordEntity holidayRecordEntity = new HolidayRecordEntity();
        holidayRecordEntity.setStartTime(startDate);
        holidayRecordEntity.setEndTime(endDate);
        String holidays = leaveSearvice.getWorkdayTimeInMillisExcWeekendHolidays(holidayRecordEntity);
        if (StringUtils.isEmpty(holidays)) {
            return R.failed("请假天数不能为空");
        }
        return R.ok(holidays);
    }


    @Override
    protected HolidayRecordServiceImpl getService() {
        return leaveSearvice;
    }
}
