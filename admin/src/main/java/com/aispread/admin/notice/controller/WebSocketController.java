package com.aispread.admin.notice.controller;

import com.aispread.admin.notice.WebSocketServer;
import com.aispread.admin.notice.dto.SendMessageRequest;
import com.redimybase.framework.bean.R;
import com.redimybase.manager.security.service.UserService;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vim 2018/12/28 10:59
 *
 * @author Vim
 */
@RestController
@RequestMapping("socket")
@Slf4j
@Api(tags = "socket推送接口")
public class WebSocketController {

   /* @RequestMapping(value = "/addUserConnection", method = {RequestMethod.POST, RequestMethod.GET})
    public R<?> addUserConnection() {
        try {
            String currentUserId = SecurityUtil.getCurrentUserId();
            if (currentUserId == null) {
                return R.fail("用户凭证过期,请重新登录");
            }
            webSocketServer.onOpen(currentUserId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return R.fail("连接服务器失败");
        }

        return R.ok();
    }*/

    @RequestMapping(value = "/push", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation("推送")
    public R<?> sendMessageForUserList(@RequestBody SendMessageRequest messageRequest) {
        try {
            if (messageRequest.getTargetList() == null) {
                return R.fail("无效用户组");
            }
            if (messageRequest.getOs() == null) {
                return R.fail("无效的平台");
            }
            webSocketServer.sendInfo(messageRequest, messageRequest.getOs(), messageRequest.getTargetList());
            log.info("通知群发成功");
        } catch (Exception e) {
            log.error("通知群发失败,{}", e.getMessage());
        }
        return R.ok();
    }

    @Autowired
    private WebSocketServer webSocketServer;


}
