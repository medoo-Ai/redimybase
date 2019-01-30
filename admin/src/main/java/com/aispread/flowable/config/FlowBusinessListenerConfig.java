package com.aispread.flowable.config;

import com.aispread.admin.controller.document.listener.FlowDocumentListener;
import com.redimybase.flowable.listener.business.FlowBusinessListener;
import com.aispread.admin.controller.leave.listener.FlowLeaveListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程业务监听器配置
 * Created by Vim 2019/1/26 21:22
 *
 * @author Vim
 */
@Configuration
public class FlowBusinessListenerConfig {

    @Bean
    public List<FlowBusinessListener> flowBusinessListeners() {
        List<FlowBusinessListener> businessListeners = new ArrayList<>();

        //(案例)文档业务流程监听器
        businessListeners.add(new FlowDocumentListener());
        //请假流程监听器
        businessListeners.add(new FlowLeaveListener());
        return businessListeners;
    }
}
