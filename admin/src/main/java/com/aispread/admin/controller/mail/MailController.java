package com.aispread.admin.controller.mail;

import com.aispread.manager.mail.service.MailService;
import com.redimybase.framework.bean.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther SyntacticSugar
 * @data 2019/2/1 0001下午 2:07
 */
@RestController
@RequestMapping("/")
@Api("2邮件发送系统")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("mail")
    @ApiOperation( "发送邮件")
    public R<?> sendMail(@ApiParam(value = "接收人") String to, @ApiParam(value = "主题")String subject , @ApiParam(value = "内容")String content) {
        try {
            mailService.sendSimpleMail(to,subject,content);
            return R.ok("发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("发送失败");
        }

    }
}
