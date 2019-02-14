package com.aispread.admin.controller.notice;

import com.aispread.manager.notice.dto.NoticeDTO;
import com.aispread.manager.notice.entity.Notice;
import com.aispread.manager.notice.service.impl.NoticeServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.common.util.ToolUtil;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 通知Controller
 * Created by Vim 2018/12/27 15:31
 *
 * @author Vim
 */
@RestController
@RequestMapping("notice")
@Api(tags = "通知接口")
public class NoticeController {

//    @Autowired
//    private JavaMailSender mailSender;
    private final static Logger log = LoggerFactory.getLogger(NoticeController.class);
    @Autowired
    private NoticeServiceImpl service;

    @GetMapping("email")
    @ApiOperation("发送邮件")
    public String send(String sender, String receiver, String title, String text) {
//        //建立邮件消息
//        SimpleMailMessage mainMessage = new SimpleMailMessage();
//        //发送者
//        System.out.println("发送者 ------------------");
//        mainMessage.setFrom("irany@163.com");
//        System.out.println("接收者 ------------------");
//        //接收者
//        mainMessage.setTo("543991220@qq.com");
//
//        //发送的标题
//        mainMessage.setSubject("testTitle");
//        //发送的内容
//        mainMessage.setText("testText");
//        mailSender.send(mainMessage);
        return "1";
    }

    /**
     * 消息列表
     * @param notice 消息对象
     *               接受userID 和 readFlag
     * @return
     */
    @PostMapping("getNoticeList")
    @ApiOperation("获取消息列表")
    public R<?> getNoticeList(NoticeDTO notice) {
        Page<NoticeDTO> page = new Page<>();
        page.setCurrent(notice.getPage());
        page.setSize(notice.getPageSize());
        return new R(service.getNoticeList(page, notice));
        //return null;
    }

    /**
     * 消息详情
     * @param ID 消息ID
     * @return
     */
    @PostMapping("getNoticeByID")
    @ApiOperation("获取单个消息详情")
    public R<?> getNoticeByID(String ID){
        Notice notice = service.getById(ID);
        //消息状态改为已读
        Notice tempNotice = new Notice();
        tempNotice.setId(ID);
        tempNotice.setReadFlag(1);
        boolean readFlag = service.updateById(tempNotice);
        if(readFlag){
            return new R(notice);
        } else{
            return R.fail();
        }
    }

    /**
     * 新增消息
     * @param notice 消息对象
     * @return
     */
    @PostMapping("addNotice")
    @ApiOperation("新建消息")
    public R<?> addNotice(Notice notice){
        if(service.save(notice)){
            return R.ok();
        } else {
            return R.fail();
        }
    }

    /**
     * 删除消息
     * @param ID 消息ID
     * @return
     */
    @PostMapping("delNotice")
    @ApiOperation("删除消息")
    public R<?> delNotice(String ID){
        if(service.removeById(ID)){
            return R.ok();
        } else {
            return R.fail();
        }
    }

    /**
     * 检查某个用户是否有未读消息
     * @param userID 用户ID
     * @return 返回true：有未读消息，返回false：没有未读消息
     */
    @PostMapping("isHaveNotReadByUserID")
    @ApiOperation("检查某个用户是否有未读消息")
    public R<?> isHaveNotReadByUserID(String userID){
        QueryWrapper<Notice> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userID);
        queryWrapper.eq("read_flag", 0);
        int num = service.count(queryWrapper);
        if(num == 0){
          return new R<>(false);
        } else {
          return new R<>(true);
        }
    }

    /**
     * 批量删除消息
     *
     * @param ids 消息ids
     * @return
     */
    @PostMapping("delNoticeByIds")
    @ApiOperation("批量删除消息")
    public R<?> delNotice(@ApiParam(required = true, name = "ids", value = "消息id值") @NotNull(message = "messageIds不能为null") @NotEmpty(message = "messageIds不能为空") List<String> ids) {
        if (service.removeByIds(ids)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    /**
     * 修改通知
     *
     * @author SyntacticSugar
     * @Date 2019/2/13 6:06 PM
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("修改通知")
    public R<?> update(@ApiParam(name = "Notice",value = "要修改的notice") Notice notice) {
        if (ToolUtil.isOneEmpty(notice, notice.getId(), notice.getMessageTitle(), notice.getMessageContent())) {
            throw new BusinessException(400,"请求有错误,notice中存在空值");
        }
        try {
            Notice old = this.service.getById(notice.getId());
            old.setMessageTitle(notice.getMessageTitle());
            old.setMessageContent(notice.getMessageContent());
            this.service.updateById(old);
            return R.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("修改失败");
        }
    }

}
