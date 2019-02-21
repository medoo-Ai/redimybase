package com.aispread.manager.leave.service.impl;
//
//import com.redimybase.manager.security.entity.UserEntity;
//import com.redimybase.security.utils.SecurityUtil;
//import com.unimlink.cuteoa.dto.UserLoginDto;
//import com.unimlink.cuteoa.entity.*;
//import com.unimlink.cuteoa.util.CreateBeanUtil;
//import com.unimlink.cuteoa.util.ProcessDeploymentUtil;
//import com.unimlink.cuteoa.util.ReplaceStrUtil;
//import com.unimlink.cuteoa.util.UserIdsUtil;
//import com.unimlink.tools.CheckHolidayUtil;
//import com.unimlink.tools.DateUtil;
//import com.unimlink.tools.IdStringUtil;
//import org.activiti.engine.HistoryService;
//import org.activiti.engine.ProcessEngine;
//import org.activiti.engine.ProcessEngines;
//import org.activiti.engine.TaskService;
//import org.activiti.engine.history.HistoricTaskInstance;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.Task;
//import org.apache.commons.lang3.StringUtils;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
public class OaleaveApplyService {
//
//    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//    @Resource
//    OaorganizationService oaorganizationService;
//
////    @Resource
////    OauserService oauserService;
//
//    @Resource
//    OaleavecalendarService oaleavecalendarService;
//
//    @Resource
//    OaleaverecordService oaleaverecordService;
//
//    @Resource
//    OastaffholidaymanagerService oastaffholidaymanagerService;
//
//    @Resource
//    UploadattachmentService uploadattachmentService;
//
//    @Resource
//    FormNoSeqService formNoSeqService;
//
//    @Resource
//    ActRuTaskService actRuTaskService;
//
//    @Resource
//    OaattachmentService oaattachmentService;
//
//    @Resource
//    OaattachmentdetailService oaattachmentdetailService;
//
//    @Resource
//    OaopinionService oaopinionService;
//
//    @Resource
//    FormStateService formStateService;
//
//    @Resource
//    ResumeleaverecordService resumeleaverecordService;
//
//    /**
//     * TODO 发起页面信息
//     *
//     * @param userid
//     * @return
//     */
//    public Map<String, Object> applyIndexInfo(String userid) {
//        UserEntity currentUser = SecurityUtil.getCurrentUser();
//
////        UserLoginDto userLoginDto = oauserService.findUserLoginByUserId(userid);
//        List<Oaleavecalendar> oaleavecalendars = oaleavecalendarService.leavecalendarList();
//        Calendar calendar = Calendar.getInstance();
//        List<Oastaffholidaymanager> oastaffholidaymanagers = oastaffholidaymanagerService
//                .findByIdAndYear(userLoginDto.getId(), String.valueOf(calendar.get(Calendar.YEAR)));
//
//        Map<String, Object> applyIndexInfo = new HashMap<>();
//
//        applyIndexInfo.put("userLoginDto", userLoginDto);
//        applyIndexInfo.put("today", new Date());
//        applyIndexInfo.put("oaleavecalendars", oaleavecalendars);
//        applyIndexInfo.put("oastaffholidaymanager", oastaffholidaymanagers.get(0));
//        return applyIndexInfo;
//    }
//
//    /**
//     * TODO
//     *
//     * @param typeflag
//     * @param applydate
//     * @param leavestardate
//     * @param leaveenddate
//     * @param reasons
//     * @param attachmentdetails
//     * @param userid
//     * @param leavcedays
//     * @return
//     */
//    public Boolean applyConfirm(String typeflag, String applydate, String leavestardate, String leaveenddate,
//                                String reasons, List<String> attachmentdetails, String userid, Double leavcedays) {
//        boolean flag = true;
//        try {
//            // 部署流程
//            String processDefinitionKey = "leaveApply";
//            String processDefinitionName = "请假申请";
//            ProcessDeploymentUtil.deployProcess(processEngine, processDefinitionKey, processDefinitionName);
//
//            UserLoginDto userLoginDto = oauserService.findUserLoginByUserId(userid);
//            Oaleaverecord oaleaveRecord = new Oaleaverecord();
//
//            //fb1a355c1c22431184427fd955cda7c1 司假类型
//            //0986d59269f1408d81c3f671ec31782b 年假类型
//            // 只有休假类型,司假 请假发起成功 扣除天数
//            if ("0986d59269f1408d81c3f671ec31782b".equals(typeflag)) {
//
//                Calendar now = Calendar.getInstance();
//                String year = String.valueOf(now.get(Calendar.YEAR));
//                List<Oastaffholidaymanager> oastaffholidaymanagers = oastaffholidaymanagerService
//                        .findByIdAndYear(userLoginDto.getId(), year);
//                if (0D == oastaffholidaymanagers.get(0).getTakeoffleaveremain()) {
//                    if (0D == oastaffholidaymanagers.get(0).getAnnualleaveremain()) {
//                        return false;
//                    }
//                }
//                if (oastaffholidaymanagers.get(0).surplus >= leavcedays) {
//                    if (0D == oastaffholidaymanagers.get(0).getAnnualleaveremain()) {
//                        return false;
//                    }
//                    // 更新个人剩余假期数
//                    List<Double> remainDays = oastaffholidaymanagerService.updateLeaveDayNew_1(oastaffholidaymanagers.get(0), leavcedays, typeflag);
//                    oaleaveRecord.setSurplus(remainDays.get(0));
//                    oaleaveRecord.setCompanyleaveremain(remainDays.get(1));
//                } else {
//                    flag = false;
//                    return flag;
//                }
//
//            }
//            if ("fb1a355c1c22431184427fd955cda7c1".equals(typeflag)) {
//                Calendar now = Calendar.getInstance();
//                String year = String.valueOf(now.get(Calendar.YEAR));
//                List<Oastaffholidaymanager> oastaffholidaymanagers = oastaffholidaymanagerService
//                        .findByIdAndYear(userLoginDto.getId(), year);
//                if (0D == oastaffholidaymanagers.get(0).getCompanyleaveremain()) {
//                    return false;
//                }
//                if (oastaffholidaymanagers.get(0).getCompanyleaveremain() >= leavcedays) {
//                    // 更新个人剩余假期数
//                    List<Double> remainDays = oastaffholidaymanagerService.updateLeaveDayNew_1(oastaffholidaymanagers.get(0), leavcedays, typeflag);
//                    oaleaveRecord.setSurplus(remainDays.get(0));
//                    oaleaveRecord.setCompanyleaveremain(remainDays.get(1));
//                } else {
//                    flag = false;
//                    return flag;
//                }
//            }
//            if (typeflag != null && leavestardate != null && leaveenddate != null && reasons != null
//                    && !"".equals(leavestardate) && !"".equals(leaveenddate) && !"".equals(typeflag)
//                    && !"".equals(reasons)) {
//                // 新建附件表
//                Oaattachment oaattachment = new Oaattachment();
//                oaattachment.setId(IdStringUtil.produceId());
//                oaattachment.setAttachmenttype("1");// 申请页面的附件
//                oaattachment.setAttachmentname("申请附件");
//                oaattachment.setCreateuser(userLoginDto.getId());
//                oaattachment.setCreatetime(new Timestamp(System.currentTimeMillis()));
//                oaattachment.setUpdateuser(userLoginDto.getUserId());
//                oaattachment.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                oaattachment.setProcesstype(processDefinitionKey);
//                oaattachment.setDeletedflag("0");
//                oaattachmentService.insert(oaattachment);
//                // 新建附件表详情
//                if (attachmentdetails == null) {
//                    attachmentdetails = new ArrayList<>();
//                }
//                if (attachmentdetails.size() > 0) {
//                    for (String attachmentdetail : attachmentdetails) {
//                        Oaattachmentdetail oaattachmentdetail = new Oaattachmentdetail();
//                        oaattachmentdetail.setId(IdStringUtil.produceId());
//                        oaattachmentdetail.setMastertableid(oaattachment.getId());
//                        oaattachmentdetail.setUploadattachment(attachmentdetail);
//                        oaattachmentdetail.setCreateuser(userLoginDto.getId());
//                        oaattachmentdetail.setCreatetime(new Timestamp(System.currentTimeMillis()));
//                        oaattachmentdetail.setUpdateuser(userLoginDto.getUserId());
//                        oaattachmentdetail.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                        oaattachmentdetail.setDeletedflag("0");
//                        oaattachmentdetailService.insert(oaattachmentdetail);
//                    }
//                }
//                // 新建表单号
//                FormNoSeq formNoSeq = new FormNoSeq();
//                formNoSeqService.insert(formNoSeq);
//                // 新建业务表
//                //oaleaveRecord = new Oaleaverecord();
//                oaleaveRecord.setId(UUID.randomUUID().toString().replace("-", ""));
//                oaleaveRecord.setUserid(userLoginDto.getId());
//                oaleaveRecord.setReasons(reasons);
//                oaleaveRecord.setApplydate(DateUtil.getDateStr(applydate));
//                oaleaveRecord.setLeavestardate(Timestamp.valueOf(leavestardate));
//                oaleaveRecord.setLeaveenddate(Timestamp.valueOf(leaveenddate));
//                oaleaveRecord.setTypeflag(typeflag);
//                oaleaveRecord.setLeavedays(new BigDecimal(leavcedays));
//                oaleaveRecord.setEnclosure(oaattachment.getId());
//                oaleaveRecord.setDeleteflag("0");
//                oaleaveRecord.setCreateuser(userLoginDto.getUserId());
//                oaleaveRecord.setCreatetime(new Timestamp(System.currentTimeMillis()));
//                oaleaveRecord.setUpdateuser(userLoginDto.getUserId());
//                oaleaveRecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                oaleaveRecord.setLeavestateflag("0");
//                oaleaveRecord.setFormno(formNoSeq.getId());
//                oaleaveRecord.setCompanyid(userLoginDto.getId());
//                oaleaveRecord.setCompanyname(userLoginDto.getCompanyName());
//                oaleaveRecord.setDepartmentid(userLoginDto.getDepartment());
//                oaleaveRecord.setDepartmentname(userLoginDto.getDepartmentName());
//                oaleaverecordService.insertOneRecord(oaleaveRecord);
//            }
//
//            // 设定流程任务办理人
//            Map<String, Object> variables = new HashMap<String, Object>();
//            // 公司id
//            String company = userLoginDto.getCompany();
//            // 部门id
//            String department = userLoginDto.getDepartment();
//            // 查询获得行政部门负责人 只有一人
//            Oauser minister = oauserService.oauserByRole(company, department, "17");
//            // 查询获得行总经理 只有一人
//            Oauser generalManager = oauserService.oauserByRole(company, "", "13");
//            // 人事管理员
//
//            /*
//             * List<Oauser> personnels = oauserService.oausersByRole(company,
//             * "", "35"); String ids = ""; if (personnels.size() > 0 &&
//             * personnels != null) { for (int i = 0; i < personnels.size(); i++)
//             * { if (i == 0) { ids = personnels.get(i).getId(); } else if (i >
//             * 0) { ids = ids + "," + personnels.get(i).getId(); } } }
//             */
//            variables.put("userIn", userLoginDto.getId());// 申请人
//            if (userLoginDto.getRole().contains("13")) {
//                variables.put("minister", generalManager.getId());
//            } else {
//                variables.put("minister", minister.getId());// 部门负责人
//            }
//            // variables.put("personnel", ids);// 人事
//            variables.put("generalManager", generalManager.getId());// 经理
//
//            // 启动工作签报流程并制定bussinesskey
//            String businessKey = "leaveApply." + oaleaveRecord.getId();
//            // 启动流程获得 流程实例
//            ProcessInstance processInstance = processEngine.getRuntimeService()
//                    .startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
//            // 申请人完成任务
//            TaskService taskService = processEngine.getTaskService();
//            if (userLoginDto.getRole().contains("13")) {// 申请人是不是总经理
//                variables.put("isLeader", "yes");
//                oaleaveRecord.setLeavestateflag("3");
//                Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
//                taskService.complete(task.getId(), variables);
//            } else {
//                variables.put("isLeader", "no");
//                Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
//                taskService.complete(task.getId(), variables);
//
//                if (userLoginDto.getRole().contains("17")) {// 部门负责人再进一步
//                    Task task2 = taskService.createTaskQuery().processInstanceId(processInstance.getId())
//                            .singleResult();
//                    variables.put("intendedFlag", "10");// 负责人请假需 经过总经理批示
//                    oaleaveRecord.setUpdateuser(userLoginDto.getUserId());
//                    oaleaveRecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                    int updateFlag = oaleaverecordService.update(oaleaveRecord);
//                    if (updateFlag > 0) {
//                        taskService.complete(task2.getId(), variables);// 与正在执行的任务管理相关的Service
//                    }
//                }
//            }
//
//            oaleaveRecord.setProcessinstanceid(processInstance.getId());
//            oaleaverecordService.updateOneRecord(oaleaveRecord);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            flag = false;
//        }
//        return flag;
//    }
//
//    /**
//     * TODO 跳转到任务办理页面
//     *
//     * @param userid
//     * @param seeFlag
//     * @param taskID
//     * @param formId
//     * @return
//     */
//    public Map<String, Object> toTaskHandleIndex(String userid, String seeFlag, String taskID, String formId) {
//        // 当前登录用户
//        UserLoginDto userLoginDto = oauserService.findUserLoginByUserId(userid);
//        String path ="taskHandle.jsp";
//        // 任务实例
//        Task task = null;
//        TaskService taskService = processEngine.getTaskService();
//        HistoryService historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
//        Map<String, Object> handlleMap = new HashMap<>();
//        if ("1".equals(seeFlag)) {// 表示待办和已申请 不可见 按钮
//            String ProcessInstanceId = historyService.createHistoricTaskInstanceQuery().taskId(taskID).singleResult()
//                    .getProcessInstanceId();
//            task = taskService.createTaskQuery().processInstanceId(ProcessInstanceId).singleResult();
//            if (task == null) {
//                List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
//                        .processInstanceId(ProcessInstanceId).list();
//                HistoricTaskInstance taskHis = tasks.get(tasks.size() - 1);
//                handlleMap.put("taskHis", taskHis);
//            }
//        } else if ("0".equals(seeFlag)) {
//            task = taskService.createTaskQuery().taskId(taskID).singleResult();// 正在执行的任务
//            if (task==null){
//                path="/webpage/loseEfficacy/?redirect=true";
//            }
//        }
//        handlleMap.put("path",path);
//        handlleMap.put("task", task);
//
//        // 查询业务表数据
//        Oaleaverecord oaleaverecord = oaleaverecordService.findbyID(formId);
//        oaleaverecord.setReasons(ReplaceStrUtil.FunStr(oaleaverecord.getReasons()));
//        oaleaverecord.setLeadercomments(ReplaceStrUtil.FunStr(oaleaverecord.getLeadercomments()));
//        oaleaverecord.setMakecomments(ReplaceStrUtil.FunStr(oaleaverecord.getMakecomments()));
//        oaleaverecord.setResumeleavereasons(ReplaceStrUtil.FunStr(oaleaverecord.getResumeleavereasons()));
//        oaleaverecord.setResumeleavecomments(ReplaceStrUtil.FunStr(oaleaverecord.getResumeleavecomments()));
//        //修改 2018-6-7 start
//        oaleaverecord.setCompanyleaveremain(oaleaverecord.getCompanyleaveremain() == null ? 0.0 : oaleaverecord.getCompanyleaveremain());
//        oaleaverecord.setSurplus(oaleaverecord.getSurplus() == null ? 0.0 : oaleaverecord.getSurplus());
//        //修改 2018-6-7 end
//        handlleMap.put("oaleaverecord", oaleaverecord);
//
//        // 根据申请时候的ID 查询用户
//        UserLoginDto oauser = oauserService.findOauserByID(oaleaverecord.getUserid());
//        handlleMap.put("oauser", oauser);
//        String thisUserId = oauserService.findUsersByMulti(oauser.getUserId()).getId();
//
//        // 查询剩余天数
//        List<Oastaffholidaymanager> oastaffholidaymanagers = oastaffholidaymanagerService.findbyID(thisUserId);
//        handlleMap.put("oastaffholidaymanager", new Oastaffholidaymanager());
//        int year = Calendar.getInstance().get(Calendar.YEAR);
//        for (int i = 0; i < oastaffholidaymanagers.size(); i++) {
//            if (StringUtils.equals(oastaffholidaymanagers.get(i).getYear(), String.valueOf(year))) {
//                handlleMap.put("oastaffholidaymanager", oastaffholidaymanagers.get(i));
//                break;
//            }
//        }
//
//        // 查询附件
//        List<Uploadattachment> uploadattachments = uploadattachmentService
//                .findAttachmentsByids(oaleaverecord.getEnclosure());
//        handlleMap.put("uploadattachment", uploadattachments);
//
//        // 查询意见表
//        List<Oaopinion> oaopinions = oaopinionService.findAllByTableid(oaleaverecord.getId(), "leaveApply");
//        handlleMap.put("oaopinions", oaopinions);
//
//        // 申请日期
//        handlleMap.put("applydate", oaleaverecord.getApplydate());
//
//        // 是否办理页面的 识别标记
//        handlleMap.put("seeFlag", seeFlag);
//
//        // 登录用户所在公司的部门情报
//        List<Oaorganization> organizationList = new ArrayList<Oaorganization>();
//        organizationList = oaorganizationService.findByUserId(userLoginDto.getUserId(), userLoginDto.getCompany());
//        handlleMap.put("organizationList", organizationList);
//
//        // 这是供员工选择的假期类型名称
//        List<Oaleavecalendar> oaleavecalendars = oaleavecalendarService.leavecalendarList();
//        handlleMap.put("oaleavecalendars", oaleavecalendars);
//
//        return handlleMap;
//    }
//
//    /**
//     * TODO 请假任务办理
//     *
//     * @param taskID
//     * @param formid
//     * @param userid
//     * @param intendedFlag
//     * @param opinion
//     * @return
//     */
//
//    public boolean taskHandle(String taskID, String formid, String userid, String intendedFlag, String opinion) {
//        Boolean handleflag = true;
//        try {
//            // 部署id
//            String processDefinitionKey = "leaveApply";
//
//            // 当前登录用户信息
//            UserLoginDto userLoginDto = oauserService.findUserLoginByUserId(userid);
//
//            // 请假记录表
//            Oaleaverecord oaleaverecord = oaleaverecordService.findbyID(formid);
//
//            // 查询当前任务
//            TaskService taskService = processEngine.getTaskService();
//            Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
//
//            // 查询申请人 假期
//            List<Oastaffholidaymanager> oastaffholidaymanagers = oastaffholidaymanagerService
//                    .findbyID(oaleaverecord.getUserid());
//            // 代理人信息
//            String userlogo = "";
//            List<UserLoginDto> users = oauserService.findUsersByUserIdAndMulti(userid);
////            Boolean flag = false;
////            for (UserLoginDto user : users) {
////                if (user.getId().equals(task.getAssignee())) {
////                    flag = true;
////                    break;
////                } else {
////                    flag = false;
////                }
////            }
////            if (!flag) {
////                userlogo = "(代)";
////            }
//            // 获取当前流程实例
//            ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery()
//                    .processInstanceId(task.getProcessInstanceId()).singleResult();
//            // 获取businesskey
//            String businesskey = processInstance.getBusinessKey();
//            // 获取委员参议表单ID
//            String formId = businesskey.split("\\.")[1];
//
//            // 任务办理
//            Map<String, Object> variables = new HashMap<String, Object>();
//            switch (task.getName()) {
//                case "填写拟办意见":
//                    if ("否".equals(intendedFlag)) {
//                        variables.put("intendedFlag", -1);
//
//                        taskService.complete(taskID, variables);// 与正在执行的任务管理相关的Service
//                        Formstate formstate = new Formstate();
//                        formstate.setId(formId);
//                        formstate.setFormtype("leaveApply");
//                        formstate.setFormstatecode(5);
//                        formStateService.insert(formstate);
//
//                        oaleaverecord.setLeavestateflag("5");// 流程结束 状态为已拒绝
//                        oaleaverecord.setUpdateuser(userLoginDto.getUserId());
//                        oaleaverecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                        oaleaverecordService.update(oaleaverecord);
//
//                        oastaffholidaymanagerService.operateLeaveDays(oastaffholidaymanagers.get(0), oaleaverecord.getLeavedays().doubleValue(), oaleaverecord.getTypeflag(),"1");
////                        if ("0986d59269f1408d81c3f671ec31782b".equals(oaleaverecord.getTypeflag())) {
////                            oastaffholidaymanagers.get(0)
////                                    .setSurplus(oastaffholidaymanagers.get(0).getSurplus().doubleValue()
////                                            + oaleaverecord.getLeavedays().doubleValue());
////                            oastaffholidaymanagers.get(0).setAnnualleaveremain(oastaffholidaymanagers.get(0).getAnnualleaveremain().doubleValue());
////                            oastaffholidaymanagerService.update(oastaffholidaymanagers.get(0));
////                        }
////
////                        if ("fb1a355c1c22431184427fd955cda7c1".equals(oaleaverecord.getTypeflag())) {
////                            oastaffholidaymanagers.get(0)
////                                    .setCompanyleaveremain(oastaffholidaymanagers.get(0).getCompanyleaveremain().doubleValue()
////                                            + oaleaverecord.getLeavedays().doubleValue());
////                            oastaffholidaymanagerService.update(oastaffholidaymanagers.get(0));
////                        }
//                        CreateBeanUtil.opinionCreate_new(oauserService, oaopinionService, oaleaverecord.getId(), processDefinitionKey, "1", opinion, userid, task.getAssignee());
////                        CreateBeanUtil.opinionCreate(oaleaverecord.getId(), processDefinitionKey, "1", userLoginDto,
////                                userlogo, opinion, oaopinionService);
//                    } else {
//                        Double leavedays = oaleaverecord.getLeavedays().doubleValue();// 员工申请的请假天数
//                        variables.put("intendedFlag", leavedays);
//                        // 查询申请人详细信息
//                        String createuser = oaleaverecord.getUserid();
//                        Oauser creatuser1 = oauserService.findById(createuser);
//                        // 部门id
//                        String department = creatuser1.getDepartment().toString();
//                        String company = creatuser1.getCompany().toString();
//                        // 查询获得行政部门负责人 只有一人
//                        Oauser minister = oauserService.oauserByRole(company, department, "17");
//                        // 查询获得行总经理 只有一人
//                        Oauser generalManager = oauserService.oauserByRole(company, "", "13");
//
//                        if (minister.getId().equals(generalManager.getId())) {
//
//                            variables.put("intendedFlag", -1);
//
//                        }
//                        taskService.complete(taskID, variables);// 与正在执行的任务管理相关的Service
//                        if (leavedays < 5) {
//                            oaleaverecord.setLeavestateflag("3");// 流程结束 状态为请假完成
//                            oaleaverecord.setUpdateuser(userLoginDto.getUserId());
//                            oaleaverecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                            oaleaverecordService.update(oaleaverecord);
//                            //审批通过 年假日期 更新
//                            oastaffholidaymanagerService.operateLeaveDays(oastaffholidaymanagers.get(0), oaleaverecord.getLeavedays().doubleValue(), oaleaverecord.getTypeflag(),"2");
////                            oastaffholidaymanagers.get(0).setAnnualleaveremain(oastaffholidaymanagers.get(0).getSurplus().doubleValue());
////                            oastaffholidaymanagerService.update(oastaffholidaymanagers.get(0));
//                        }
//
//                        if (StringUtils.isEmpty(opinion)) {
//                            opinion = "同意";
//                        }
//                        CreateBeanUtil.opinionCreate_new(oauserService, oaopinionService, oaleaverecord.getId(), processDefinitionKey, "1", opinion, userid, task.getAssignee());
////                        CreateBeanUtil.opinionCreate(oaleaverecord.getId(), processDefinitionKey, "1", userLoginDto,
////                                userlogo, opinion, oaopinionService);
//
//                    }
//                    break;
//                case "总经理审核":
//                    if ("否".equals(intendedFlag)) {
//                        variables.put("leaderFlag", "0");
//                        Formstate formstate = new Formstate();
//                        formstate.setId(formId);
//                        formstate.setFormtype("leaveApply");
//                        formstate.setFormstatecode(5);
//                        formStateService.insert(formstate);
//
//                        oaleaverecord.setLeavestateflag("5");// 流程结束 状态为已拒绝
//                        oaleaverecord.setUpdateuser(userLoginDto.getUserId());
//                        oaleaverecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                        oaleaverecordService.update(oaleaverecord);
//
//                        oastaffholidaymanagerService.operateLeaveDays(oastaffholidaymanagers.get(0), oaleaverecord.getLeavedays().doubleValue(), oaleaverecord.getTypeflag(),"1");
//                        CreateBeanUtil.opinionCreate_new(oauserService, oaopinionService, oaleaverecord.getId(), processDefinitionKey, "2", opinion, userid, task.getAssignee());
//                        taskService.complete(taskID, variables);// 与正在执行的任务管理相关的Service
////                        if ("0986d59269f1408d81c3f671ec31782b".equals(oaleaverecord.getTypeflag())) {
////
////                            oastaffholidaymanagers.get(0)
////                                    .setSurplus(oastaffholidaymanagers.get(0).getSurplus().doubleValue()
////                                            + oaleaverecord.getLeavedays().doubleValue());
////                            oastaffholidaymanagers.get(0).setAnnualleaveremain(oastaffholidaymanagers.get(0).getSurplus().doubleValue());
////                            oastaffholidaymanagerService.update(oastaffholidaymanagers.get(0));
////                        }
////
////                        if ("fb1a355c1c22431184427fd955cda7c1".equals(oaleaverecord.getTypeflag())) {
////                            oastaffholidaymanagers.get(0)
////                                    .setCompanyleaveremain(oastaffholidaymanagers.get(0).getCompanyleaveremain().doubleValue()
////                                            + oaleaverecord.getLeavedays().doubleValue());
////                            oastaffholidaymanagerService.update(oastaffholidaymanagers.get(0));
////                        }
////                        CreateBeanUtil.opinionCreate(oaleaverecord.getId(), processDefinitionKey, "2", userLoginDto,
////                                userlogo, opinion, oaopinionService);
//                    } else {
//
//                        if (StringUtils.isEmpty(opinion)) {
//                            opinion = "同意";
//                        }
//                        CreateBeanUtil.opinionCreate_new(oauserService, oaopinionService, oaleaverecord.getId(), processDefinitionKey, "2", opinion, userid, task.getAssignee());
////                        CreateBeanUtil.opinionCreate(oaleaverecord.getId(), processDefinitionKey, "2", userLoginDto,
////                                userlogo, opinion, oaopinionService);
//
//                        //审批通过 年假日期 更新
//                        oastaffholidaymanagerService.operateLeaveDays(oastaffholidaymanagers.get(0), oaleaverecord.getLeavedays().doubleValue(), oaleaverecord.getTypeflag(),"2");
////                        oastaffholidaymanagers.get(0).setAnnualleaveremain(oastaffholidaymanagers.get(0).getSurplus().doubleValue());
////                        oastaffholidaymanagerService.update(oastaffholidaymanagers.get(0));
//
//                        oaleaverecord.setLeavestateflag("3");// 流程结束 状态为请假完成
//                        oaleaverecord.setUpdateuser(userLoginDto.getUserId());
//                        oaleaverecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                        oaleaverecordService.update(oaleaverecord);
//
//                        variables.put("leaderFlag", "1");
//                        taskService.complete(taskID, variables);// 与正在执行的任务管理相关的Service
//                    }
//                    break;
//                case "办结存档":
//                    String thisUserId = UserIdsUtil
//                            .findIdsStr(oauserService.findUsersByUserIdAndMulti(userLoginDto.getUserId()));
//                    String claimid = "";
//                    long checkFlag = actRuTaskService.checkLoginDto(thisUserId, taskID);
//
//                    if (checkFlag > 0) {// 这说明登录用户 有执行该任务的权限。
//                        claimid = userLoginDto.getId();
//                    } else {// 代理别人 执行任务。
//                        claimid = actRuTaskService.finduserIDBytaskID(thisUserId, taskID,
//                                DateUtil.parseDateToStringYMD(new Date()));
//                    }
//                    taskService.claim(taskID, claimid);
//                    taskService.complete(taskID);
//
//                    oaleaverecord.setLeavestateflag("3");// 流程结束 状态为请假完成
//                    oaleaverecord.setUpdateuser(userLoginDto.getUserId());
//                    oaleaverecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                    oaleaverecordService.update(oaleaverecord);
//                    break;
//                default:
//                    break;
//            }
//        } catch (Exception e) {
//            handleflag = false;
//        }
//
//        return handleflag;
//
//    }
//
//    /**
//     * TODO 销假页面详情
//     *
//     * @param userid
//     * @return
//     */
//    public Map<String, Object> resumeApplyIndexInfo(String userid) {
//        Map<String, Object> resumeApplyIndexInfo = new HashMap<>();
//
//        // 获得登录用户
//        UserLoginDto userLoginDto = oauserService.findUserLoginByUserId(userid);
//        resumeApplyIndexInfo.put("userLoginDto", userLoginDto);
//
//        // 请假中为0 销假为处理中为1 销假完成为2 请假完成为3
//        // 用户只能看到请假完成的假单 销假被拒绝之后应该将该字段变为3
//        // String state = "0";
//        String state = "3";
//        List<OaleaverecordInfo> oaleaverecords = oaleaverecordService.leaverecordList_Update1(userLoginDto.getId(), state);
////        List<Oaleaverecord> oaleaverecords = oaleaverecordService.leaverecordList(userLoginDto.getId(), state);
//        resumeApplyIndexInfo.put("oaleaverecords", oaleaverecords);
//
//        // 当前日期
//        resumeApplyIndexInfo.put("today", new Date());
//        return resumeApplyIndexInfo;
//    }
//
//    /**
//     * TODO 提交销假申请
//     *
//     * @param formid
//     * @param userid
//     * @param resumeleavedays
//     * @param resumeleavereasons
//     * @param resumeleaveapplydate
//     * @param resumedatestr
//     * @return
//     */
//    public boolean addresumeLeaveApply(String formid, String userid, Double resumeleavedays, String resumeleavereasons,
//                                       String resumeleaveapplydate, String resumedatestr) {
//        boolean flag = true;
//        try {
//            // 部署流程
//            String processDefinitionKey = "resumeleaveApply";
//            String processDefinitionName = "销假申请";
//            ProcessDeploymentUtil.deployProcess(processEngine, processDefinitionKey, processDefinitionName);
//
//            // 获得登录用户
//            UserLoginDto userLoginDto = oauserService.findUserLoginByUserId(userid);
//
//            // 获得请假信息
//            Oaleaverecord oaleaveRecord = oaleaverecordService.findbyID(formid);
//            Calendar calendar = Calendar.getInstance();
//            // 查询申请人 假期
//            Oastaffholidaymanager oastaffholidaymanager = oastaffholidaymanagerService
//                    .findbyUserIdAndYear(oaleaveRecord.getUserid(), String.valueOf(calendar.get(Calendar.YEAR)));
//
//            // 将状态置为销假状态 销假中不能再次看到该假单
//            oaleaveRecord.setLeavestateflag("1");
//            oaleaverecordService.update(oaleaveRecord);
//
//            Resumeleaverecord resumeleaverecord = new Resumeleaverecord();
//
//            // 新增销假详情
//            if (resumeleaveapplydate != null && resumeleavedays != null && resumeleavereasons != null
//                    && !"".equals(resumeleaveapplydate) && !"".equals(resumeleavedays)
//                    && !"".equals(resumeleavereasons)) {
//                FormNoSeq formNoSeq = new FormNoSeq();
//                formNoSeqService.insert(formNoSeq);
//
//                // oaleaveRecord.setResumeleavedays(resumeleavedays);
//                // oaleaveRecord.setResumeleavereasons(resumeleavereasons);
//                // oaleaveRecord.setResumeleaveapplydate(Timestamp.valueOf(resumeleaveapplydate));
//                // oaleaveRecord.setResumedatestr(resumedatestr);
//                // oaleaveRecord.setUpdateuser(userLoginDto.getUserId());
//                // oaleaveRecord.setUpdatetime(new
//                // Timestamp(System.currentTimeMillis()));
//                // oaleaveRecord.setLeavestateflag("1");
//                // oaleaveRecord.setResumeformno(formNoSeq.getId());
//
//                resumeleaverecord.setId(UUID.randomUUID().toString().replace("-", ""));
//                resumeleaverecord.setOaleaverecordid(formid);
//                resumeleaverecord.setResumeleaveday(resumeleavedays);
//                resumeleaverecord.setResumeleavereasons(resumeleavereasons);
//                resumeleaverecord.setResumeleaveapplydate(Timestamp.valueOf(resumeleaveapplydate));
//                resumeleaverecord.setResumedatestr(resumedatestr);
//                resumeleaverecord.setUpdateuser(userLoginDto.getUserId());
//                resumeleaverecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                resumeleaverecord.setResumeformno(formNoSeq.getId().toString());
//                resumeleaverecordService.insert(resumeleaverecord);
//
//            }
//
//            // 设定流程任务办理人
//            Map<String, Object> variables = new HashMap<String, Object>();
//            // 公司id
//            String company = userLoginDto.getCompany();
//            // 部门id
//            String department = userLoginDto.getDepartment();
//            // 查询获得行政部门负责人 只有一人
//            Oauser minister = oauserService.oauserByRole(company, department, "17");
//            // 查询获得行总经理 只有一人
//            Oauser generalManager = oauserService.oauserByRole(company, "", "13");
//            // 人事管理员
//            List<Oauser> personnels = oauserService.oausersByRole(company, "", "35");
//            String ids = "";
//            if (personnels.size() > 0 && personnels != null) {
//                for (int i = 0; i < personnels.size(); i++) {
//                    if (i == 0) {
//                        ids = personnels.get(i).getId();
//                    } else if (i > 0) {
//                        ids = ids + "," + personnels.get(i).getId();
//                    }
//                }
//            }
//
//            variables.put("userIn", userLoginDto.getId());// 申请人
//            variables.put("minister", minister.getId());// 部门负责人
//            variables.put("personnel", ids);// 人事
//            variables.put("generalManager", generalManager.getId());// 经理
//
//            String businessKey = "resumeleaveApply." + resumeleaverecord.getId();
//            // 启动流程获得 流程实例
//            ProcessInstance processInstance = processEngine.getRuntimeService()
//                    .startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
//
//            // 申请人完成任务
//            TaskService taskService = processEngine.getTaskService();
//            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
//            taskService.complete(task.getId());
//            if (userLoginDto.getRole().contains("13")) {// 总经理 流程直接结束
//                // 部门负责人再进一步
//                Task task2 = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
//                variables.put("re", "总经理");
//                taskService.complete(task2.getId(), variables);// 与正在执行的任务管理相关的Service
//
//                resumeleaverecord.setUpdateuser(userLoginDto.getUserId());
//                resumeleaverecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                resumeleaverecordService.update(resumeleaverecord);
//
//                Task task3 = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
//
//                // variables.put("leaderFlag", "1");
//                taskService.complete(task3.getId());
//                // if (StringUtils.isEmpty(opinion)) {
//                // opinion = "同意";
//                // }
//                oaleaveRecord.setLeavestateflag("2");// 销假完成
//                oaleaverecordService.update(oaleaveRecord);
//                resumeleaverecord.setUpdateuser(userLoginDto.getUserId());
//                resumeleaverecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                resumeleaverecordService.update(resumeleaverecord);
//                String opinion = "同意";
//                CreateBeanUtil.opinionCreate_new(oauserService, oaopinionService, resumeleaverecord.getId(), processDefinitionKey, "4", opinion, userid, task.getAssignee());
////                CreateBeanUtil.opinionCreate(resumeleaverecord.getId(), processDefinitionKey, "4", userLoginDto, "",
////                        "同意", oaopinionService);
//
//                // 还时间
//                oastaffholidaymanagerService.operateLeaveDays(oastaffholidaymanager, resumeleaverecord.getResumeleaveday().doubleValue(), oaleaveRecord.getTypeflag(),"1");
////                if ("0986d59269f1408d81c3f671ec31782b".equals(oaleaveRecord.getTypeflag())) {
////                    oastaffholidaymanager.setAnnualleaveremain(oastaffholidaymanager.getSurplus() + resumeleaverecord.getResumeleaveday());
////                    oastaffholidaymanager.setSurplus(oastaffholidaymanager.getSurplus() + resumeleaverecord.getResumeleaveday());
////                    oastaffholidaymanagerService.update(oastaffholidaymanager);
////                }
//
//            } else if (userLoginDto.getRole().contains("17")) {// 部门负责人再进一步
//                Task task2 = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
//                variables.put("re", "总经理");
//                taskService.complete(task2.getId(), variables);// 与正在执行的任务管理相关的Service
//
//                resumeleaverecord.setUpdateuser(userLoginDto.getUserId());
//                resumeleaverecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                resumeleaverecordService.update(resumeleaverecord);
//            }
//            resumeleaverecord.setProcessinstanceid(processInstance.getId());
//            resumeleaverecordService.update(resumeleaverecord);
//        } catch (Exception e) {
//            e.printStackTrace();
//            flag = false;
//        }
//        return flag;
//    }
//
//    /**
//     * TODO toResumeTaskHandleIndex 跳转到销假任务办理页面
//     *
//     * @param seeFlag
//     * @param taskID
//     * @param formId
//     * @return
//     */
//    public Map<String, Object> toResumeTaskHandleIndex(String seeFlag, String taskID, String formId) {
//        // 任务实例
//        Task task = null;
//        TaskService taskService = processEngine.getTaskService();
//        HistoryService historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
//        Map<String, Object> handlleMap = new HashMap<>();
//        String path = "resumetaskHandle.jsp";
//        if ("1".equals(seeFlag)) {// 表示待办和已申请 不可见 按钮
//            String ProcessInstanceId = historyService.createHistoricTaskInstanceQuery().taskId(taskID).singleResult()
//                    .getProcessInstanceId();
//            task = taskService.createTaskQuery().processInstanceId(ProcessInstanceId).singleResult();
//            if (task == null) {
//                List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
//                        .processInstanceId(ProcessInstanceId).list();
//                HistoricTaskInstance taskHis = tasks.get(tasks.size() - 1);
//                handlleMap.put("taskHis", taskHis);
//            }
//        } else if ("0".equals(seeFlag)) {
//            task = taskService.createTaskQuery().taskId(taskID).singleResult();// 正在执行的任务
//            if (task==null){
//                path="/webpage/loseEfficacy/?redirect=true";
//            }
//
//        }
//        handlleMap.put("path",path);
//        handlleMap.put("task", task);
//
//        // 查询业务表数据
//        Resumeleaverecord oaleaverecord = resumeleaverecordService.findById(formId);
//        Oaleaverecord oaleaverecordinfo = oaleaverecordService.findbyID(oaleaverecord.getOaleaverecordid());
//        oaleaverecord.setResumeleavereasons(ReplaceStrUtil.FunStr(oaleaverecord.getResumeleavereasons()));
//        handlleMap.put("oaleaverecord", oaleaverecord);
//        handlleMap.put("oaleaverecordinfo", oaleaverecordinfo);
//
//        // 查询意见表
//        List<Oaopinion> oaopinions = oaopinionService.findAllByTableid(oaleaverecord.getId(), "resumeleaveApply");
//        handlleMap.put("oaopinions", oaopinions);
//
//        // 根据申请时候的ID 查询用户
//        UserLoginDto oauser = oauserService.findOauserByID(oaleaverecordinfo.getUserid());
//        handlleMap.put("oauser", oauser);
//
//        handlleMap.put("maplist", MapListReader(formId));
//        handlleMap.put("seeFlag", seeFlag);
//        handlleMap.put("processDefinitionKey", "resumeleaveApply");
//
//        // 当前登录用户
//        UserLoginDto userLoginDto = oauserService.findUserLoginByUserId(oauser.getUserId());
//        handlleMap.put("userLoginDto", userLoginDto);
//        return handlleMap;
//    }
//
//    /**
//     * TODO resumeTaskHandle 销假任务办理
//     *
//     * @param taskID
//     * @param formid
//     * @param userid
//     * @param intendedFlag
//     * @param opinion
//     * @param formStateService TODO
//     * @return
//     */
//    public boolean resumeTaskHandle(String taskID, String formid, String userid, String intendedFlag, String opinion,
//                                    FormStateService formStateService) {
//        boolean handleflag = true;
//        try {
//            // 部署id
//            String processDefinitionKey = "resumeleaveApply";
//
//            // 当前登录用户信息
//            UserLoginDto userLoginDto = oauserService.findUserLoginByUserId(userid);
//
//            // 请假记录表
//            Resumeleaverecord resumeleaverecord = resumeleaverecordService.findById(formid);
//            Oaleaverecord oaleaveRecord = oaleaverecordService.findbyID(resumeleaverecord.getOaleaverecordid());
//
//            // 查询当前任务
//            TaskService taskService = processEngine.getTaskService();
//            Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
//
//            Calendar calendar = Calendar.getInstance();
//            // 查询申请人 假期
//            Oastaffholidaymanager oastaffholidaymanager = oastaffholidaymanagerService
//                    .findbyUserIdAndYear(oaleaveRecord.getUserid(), String.valueOf(calendar.get(Calendar.YEAR)));
////            // 代理人信息
////            String userlogo = "";
////            List<UserLoginDto> users = oauserService.findUsersByUserIdAndMulti(userid);
////            Boolean flag = false;
////            for (UserLoginDto user : users) {
////                if (user.getId().equals(task.getAssignee())) {
////                    flag = true;
////                    break;
////                } else {
////                    flag = false;
////                }
////            }
////            if (!flag) {
////                userlogo = "(代)";
////            }
//
//            // 任务办理
//            Map<String, Object> variables = new HashMap<String, Object>();
//            switch (task.getName()) {
//                case "审批":
//                    if ("拒绝".equals(intendedFlag)) {
//                        // variables.put("re", intendedFlag);
//                        variables.put("re", "结束");
//                        taskService.complete(taskID, variables);// 与正在执行的任务管理相关的Service
//
//                        oaleaveRecord.setLeavestateflag("3");// 流程结束 变为请假完成，重新可被处理
//                        oaleaverecordService.update(oaleaveRecord);
//                        resumeleaverecord.setUpdateuser(userLoginDto.getUserId());
//                        resumeleaverecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                        resumeleaverecordService.update(resumeleaverecord);
//
//                        CreateBeanUtil.opinionCreate_new(oauserService, oaopinionService, resumeleaverecord.getId(), processDefinitionKey, "3", opinion, userid, task.getAssignee());
//
////                        CreateBeanUtil.opinionCreate(resumeleaverecord.getId(), processDefinitionKey, "3", userLoginDto,
////                                userlogo, opinion, oaopinionService);
//                        // 拒绝
//                        Formstate formstate = new Formstate();
//                        formstate.setFormtype("resumeleaveApply");
//                        formstate.setId(resumeleaverecord.getId());
//                        formstate.setFormstatecode(5);
//                        formStateService.insert(formstate);
//                    } else {
//                        variables.put("re", "结束");
//                        taskService.complete(taskID, variables);// 与正在执行的任务管理相关的Service
//                        oaleaveRecord.setLeavestateflag("2");// 销假完成
//                        oaleaverecordService.update(oaleaveRecord);
//                        resumeleaverecord.setUpdateuser(userLoginDto.getUserId());
//                        resumeleaverecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                        resumeleaverecordService.update(resumeleaverecord);
//                        if (StringUtils.isEmpty(opinion)) {
//                            opinion = "同意";
//                        }
//                        CreateBeanUtil.opinionCreate_new(oauserService, oaopinionService, resumeleaverecord.getId(), processDefinitionKey, "3", opinion, userid, task.getAssignee());
////                        CreateBeanUtil.opinionCreate(resumeleaverecord.getId(), processDefinitionKey, "3", userLoginDto,
////                                userlogo, opinion, oaopinionService);
//                        // 还时间
//                        oastaffholidaymanagerService.operateLeaveDays(oastaffholidaymanager, resumeleaverecord.getResumeleaveday().doubleValue(), oaleaveRecord.getTypeflag(),"1");
////                        if ("0986d59269f1408d81c3f671ec31782b".equals(oaleaveRecord.getTypeflag())) {
////                            oastaffholidaymanager.setTakeoffleaveremain(
////                                    oastaffholidaymanager.getTakeoffleaveremain() + resumeleaverecord.getResumeleaveday());
////                            oastaffholidaymanager
////                                    .setSurplus(oastaffholidaymanager.getSurplus() + resumeleaverecord.getResumeleaveday());
////                            oastaffholidaymanagerService.update(oastaffholidaymanager);
////                        }
////                        if ("fb1a355c1c22431184427fd955cda7c1".equals(oaleaveRecord.getTypeflag())) {
//////                            oastaffholidaymanager.setTakeoffleaveremain(
//////                                    oastaffholidaymanager.getCompanyleaveremain() + resumeleaverecord.getResumeleaveday());
////                            oastaffholidaymanager.setCompanyleaveremain(oastaffholidaymanager.getCompanyleaveremain().doubleValue()
////                                    + resumeleaverecord.getResumeleaveday().doubleValue());
////                            oastaffholidaymanagerService.update(oastaffholidaymanager);
////                        }
//
//                    }
//                    break;
//                case "总经理批示":
//                    if ("否".equals(intendedFlag)) {
//                        // variables.put("leaderFlag", "0");
//                        taskService.complete(taskID);// 与正在执行的任务管理相关的Service
//
//                        oaleaveRecord.setLeavestateflag("3");// 流程结束 重新可被处理
//                        oaleaverecordService.update(oaleaveRecord);
//                        resumeleaverecord.setUpdateuser(userLoginDto.getUserId());
//                        resumeleaverecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                        resumeleaverecordService.update(resumeleaverecord);
//
//                        CreateBeanUtil.opinionCreate_new(oauserService, oaopinionService, resumeleaverecord.getId(), processDefinitionKey, "4", opinion, userid, task.getAssignee());
////                        CreateBeanUtil.opinionCreate(resumeleaverecord.getId(), processDefinitionKey, "4", userLoginDto,
////                                userlogo, opinion, oaopinionService);
//                        // 拒绝
//                        Formstate formstate = new Formstate();
//                        formstate.setFormtype("resumeleaveApply");
//                        formstate.setId(resumeleaverecord.getId());
//                        formstate.setFormstatecode(5);
//                        formStateService.insert(formstate);
//                    } else {
//                        // variables.put("leaderFlag", "1");
//                        taskService.complete(taskID);
//                        if (StringUtils.isEmpty(opinion)) {
//                            opinion = "同意";
//                        }
//                        oaleaveRecord.setLeavestateflag("2");// 销假完成
//                        oaleaverecordService.update(oaleaveRecord);
//                        resumeleaverecord.setUpdateuser(userLoginDto.getUserId());
//                        resumeleaverecord.setUpdatetime(new Timestamp(System.currentTimeMillis()));
//                        resumeleaverecordService.update(resumeleaverecord);
//
//                        CreateBeanUtil.opinionCreate_new(oauserService, oaopinionService, resumeleaverecord.getId(), processDefinitionKey, "4", opinion, userid, task.getAssignee());
////                        CreateBeanUtil.opinionCreate(resumeleaverecord.getId(), processDefinitionKey, "4", userLoginDto,
////                                userlogo, opinion, oaopinionService);
//
//                        // 还时间
//                        oastaffholidaymanagerService.operateLeaveDays(oastaffholidaymanager, resumeleaverecord.getResumeleaveday().doubleValue(), oaleaveRecord.getTypeflag(),"1");
//
//                        // 还时间
////                        if ("0986d59269f1408d81c3f671ec31782b".equals(oaleaveRecord.getTypeflag())) {
////                            oastaffholidaymanager.setTakeoffleaveremain(
////                                    oastaffholidaymanager.getTakeoffleaveremain() + resumeleaverecord.getResumeleaveday());
////                            oastaffholidaymanager
////                                    .setSurplus(oastaffholidaymanager.getSurplus() + resumeleaverecord.getResumeleaveday());
////                            oastaffholidaymanagerService.update(oastaffholidaymanager);
////                        }
////                        if ("fb1a355c1c22431184427fd955cda7c1".equals(oaleaveRecord.getTypeflag())) {
//////                            oastaffholidaymanager.setTakeoffleaveremain(
//////                                    oastaffholidaymanager.getCompanyleaveremain() + resumeleaverecord.getResumeleaveday());
////                            oastaffholidaymanager.setCompanyleaveremain(oastaffholidaymanager.getCompanyleaveremain().doubleValue()
////                                    + resumeleaverecord.getResumeleaveday().doubleValue());
////                            oastaffholidaymanagerService.update(oastaffholidaymanager);
////                        }
//                    }
//                    break;
//                // case "归档":
//                // String thisUserId = UserIdsUtil
//                // .findIdsStr(oauserService.findUsersByUserIdAndMulti(userLoginDto.getUserId()));
//                // String claimid = "";
//                // long checkFlag = actRuTaskService.checkLoginDto(thisUserId,
//                // taskID);
//                //
//                // if (checkFlag > 0) {// 这说明登录用户 有执行该任务的权限。
//                // claimid = userLoginDto.getId();
//                // } else {// 代理别人 执行任务。
//                // claimid = actRuTaskService.finduserIDBytaskID(thisUserId, taskID,
//                // DateUtil.parseDateToStringYMD(new Date()));
//                // }
//                // taskService.claim(taskID, claimid);
//                // taskService.complete(taskID);
//                //
//                // resumeleaverecord.setUpdateuser(userLoginDto.getUserId());
//                // resumeleaverecord.setUpdatetime(new
//                // Timestamp(System.currentTimeMillis()));
//                // resumeleaverecordService.update(resumeleaverecord);
//                //
//                // // 归档
//                // Formstate formstate = new Formstate();
//                // formstate.setFormtype("resumeleaveApply");
//                // formstate.setId(resumeleaverecord.getId());
//                // formstate.setFormstatecode(2);
//                // formStateService.insert(formstate);
//                //
//                //// if
//                // ("0986d59269f1408d81c3f671ec31782b".equals(oaleaveRecord.getTypeflag()))
//                // {
//                //// oastaffholidaymanager
//                //// .setSurplus(oastaffholidaymanager.getSurplus() +
//                // oaleaveRecord.getResumeleavedays());
//                //// oastaffholidaymanagerService.update(oastaffholidaymanager);
//                //// }
//                // break;
//
//                default:
//                    break;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            handleflag = false;
//        }
//        return handleflag;
//    }
//
//    /**
//     * TODO 获得销假日期的str
//     *
//     * @param id
//     * @return
//     */
//    public List<Map<String, Object>> MapListReader(String id) {
//        Oaleaverecord oaleaverecord = oaleaverecordService.findbyID(id);
//        if (oaleaverecord == null) {
//            Resumeleaverecord resumeleaverecord = resumeleaverecordService.findById(id);
//            oaleaverecord = oaleaverecordService.findbyID(resumeleaverecord.getOaleaverecordid());
//        }
//        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
//        DateFormat dFormat2 = new SimpleDateFormat("HH:mm:ss");
//        String startdate = dFormat.format(oaleaverecord.getLeavestardate());
//        String enddate = dFormat.format(oaleaverecord.getLeaveenddate());
//        List<String> dateList = CheckHolidayUtil.getDateList(startdate, enddate);
//
//        List<Map<String, Object>> list = new ArrayList<>();
//        if (startdate.equals(enddate)) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("day", startdate);
//            if (StringUtils.equals("08:00:00", dFormat2.format(oaleaverecord.getLeavestardate()))) {
//                map.put("am", startdate + " 08:00:00");
//            } else {
//                map.put("am", startdate + " 12:00:00");
//            }
//            if (StringUtils.equals("08:00:00", dFormat2.format(oaleaverecord.getLeaveenddate()))) {
//                map.put("pm", startdate + " 12:00:00");
//            } else {
//                map.put("pm", startdate + " 08:00:00");
//            }
//			/*if ("08:00:00".equals(dFormat2.format(oaleaverecord.getLeavestardate()))
//					&& "12:00:00".equals(dFormat2.format(oaleaverecord.getLeaveenddate()))) {
//				map.put("am", startdate + " 08:00:00");
//				map.put("pm", "null");
//			} else if ("12:00:00".equals(dFormat2.format(oaleaverecord.getLeavestardate()))
//					&& "18:00:00".equals(dFormat2.format(oaleaverecord.getLeaveenddate()))) {
//				map.put("am", "null");
//				map.put("pm", startdate + " 12:00:00");
//			} else {
//				map.put("am", startdate + " 08:00:00");
//				map.put("pm", startdate + " 12:00:00");
//			}*/
//            list.add(map);
//        } else {
//            for (int i = 0; i < dateList.size(); i++) {
//                Map<String, Object> map = new HashMap<>();
//                if (i == 0) {// 考虑 开始日期 是否是 从下午开始的
//                    map.put("day", dateList.get(i));
//                    if ("08:00:00".equals(dFormat2.format(oaleaverecord.getLeavestardate()))) {
//                        map.put("am", dateList.get(i) + " 08:00:00");
//                        map.put("pm", dateList.get(i) + " 12:00:00");
//                    } else {
//                        map.put("am", "null");
//                        map.put("pm", dateList.get(i) + " 12:00:00");
//                    }
//                } else if (i == dateList.size() - 1) {// 结束日期是否是从上午结束的
//                    map.put("day", dateList.get(i));
//                    if ("08:00:00".equals(dFormat2.format(oaleaverecord.getLeaveenddate()))) {
//                        map.put("am", dateList.get(i) + " 08:00:00");
//                        map.put("pm", "null");
//                    } else {
//                        map.put("am", dateList.get(i) + " 08:00:00");
//                        map.put("pm", dateList.get(i) + " 12:00:00");
//                    }
//                } else {
//                    map.put("day", dateList.get(i));
//                    map.put("am", dateList.get(i) + " 08:00:00");
//                    map.put("pm", dateList.get(i) + " 12:00:00");
//                }
//                list.add(map);
//            }
//        }
//
//        // start g
//        // 节假日减除
//        List<String> listAll = new ArrayList<String>();
//		/*List<Oaleavecalendar> oaleavecalendars = oaleavecalendarService.leavecalendarList("1");
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		for (Oaleavecalendar oaleavecalendar : oaleavecalendars) {
//			String holidayStart = dateFormat.format(oaleavecalendar.getStarttime());
//			String holidayEnd = dateFormat.format(oaleavecalendar.getEndtime());
//			List<String> listh = CheckHolidayUtil.getDateList(holidayStart, holidayEnd);
//			listAll.addAll(listh);
//		}*/
//
//        for (int i = 0; i < list.size(); i++) {
//            for (int j = 0; j < listAll.size(); j++) {
//                if (list.get(i).get("day").equals(listAll.get(j))) {
//                    list.remove(i);
//                    i--;
//                }
//            }
//        }
//
//        // 周六周天减除
//		/*for (int i = 0; i < list.size(); i++) {
//			Calendar calendar = Calendar.getInstance();
//			try {
//				calendar.setTime(dateFormat.parse((String) list.get(i).get("day")));
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} // 初始化待检验日期
//			int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
//			if (week == 6 || week == 0) {
//				list.remove(i);
//				i--;
//			}
//		}*/
//        // end g
//
//        return list;
//    }
//
//    public double findLeaveDays(String startTime, String endTime, String startSelect, String endSelect) {
//        double leavedays = 0;
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        // 查数据库中 录入的 法定节假日的信息。typeFlag = 1;
//        List<Oaleavecalendar> oaleavecalendars = oaleavecalendarService.leavecalendarList("1");
//        // 查数据库中 录入的 法定节假日的信息。typeFlag = 2;
//        List<Oaleavecalendar> oaleavecalendars2 = oaleavecalendarService.leavecalendarList("2");
//
//        List<String> dateList = new ArrayList<>();
//        List<Calendar> holidayList = new ArrayList<>();
//        List<Calendar> weekendLists = new ArrayList<>();
//
//        // 都不能为空
//        if ((startTime != null && endTime != null)) {
//            if (!StringUtils.equals(startTime, "null") && !StringUtils.equals(endTime, "null")) {
//                dateList = CheckHolidayUtil.getDateList(startTime, endTime);
//            }
//        }
//        if (oaleavecalendars != null && oaleavecalendars.size() > 0) {
//            for (Oaleavecalendar oaleavecalendar : oaleavecalendars) {
//                String holidayStart = dateFormat.format(oaleavecalendar.getStarttime());
//                String holidayEnd = dateFormat.format(oaleavecalendar.getEndtime());
//
//                // 加载所有假日信息
//                holidayList.addAll(
//                        CheckHolidayUtil.initHolidayList(CheckHolidayUtil.getDateList(holidayStart, holidayEnd)));
//            }
//        }
//        if (oaleavecalendars2 != null && oaleavecalendars2.size() > 0) {
//            for (Oaleavecalendar oaleavecalendar : oaleavecalendars2) {
//                String holidayStart = dateFormat.format(oaleavecalendar.getStarttime());
//                String holidayEnd = dateFormat.format(oaleavecalendar.getEndtime());
//                // 加载所有加班日的信息
//                weekendLists.addAll(
//                        CheckHolidayUtil.initWeekendList(CheckHolidayUtil.getDateList(holidayStart, holidayEnd)));
//            }
//        }
//        // 如果
//        try {
//            if ((startTime != null && endTime != null)) {
//                if (!StringUtils.equals(startTime, "null") && !StringUtils.equals(endTime, "null")) {
//                    leavedays = CheckHolidayUtil.checkHoliday(dateList, holidayList, weekendLists, startTime, endTime,
//                            startSelect, endSelect);// 获得天数;
//                    if (leavedays < 0){
//                        leavedays = 0;
//                    }
//                }
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return leavedays;
//    }
}
