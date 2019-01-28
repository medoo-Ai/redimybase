package com.redimybase.flowable.config;

import com.redimybase.flowable.listener.autocomplete.AutoCompleteFirstTaskListener;
import com.redimybase.flowable.listener.end.CustomEndListener;
import com.redimybase.flowable.parser.factory.DefaultActivityBehaviorFactoryExt;
import org.flowable.app.properties.FlowableModelerAppProperties;
import org.flowable.engine.common.api.delegate.event.FlowableEventListener;
import org.flowable.job.service.impl.asyncexecutor.AsyncExecutor;
import org.flowable.spring.ProcessEngineFactoryBean;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.FlowableMailProperties;
import org.flowable.spring.boot.FlowableProperties;
import org.flowable.spring.boot.ProcessEngineAutoConfiguration;
import org.flowable.spring.boot.idm.FlowableIdmProperties;
import org.flowable.spring.boot.process.FlowableProcessProperties;
import org.flowable.spring.boot.process.Process;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * flowable配置
 * Created by Vim 2018/10/8 15:32
 *
 * @author Vim
 */
@Configuration
@EnableConfigurationProperties(FlowableProperties.class)
public class FlowableConfig extends ProcessEngineAutoConfiguration {


    public FlowableConfig(FlowableProperties flowableProperties,
                          FlowableProcessProperties processProperties, FlowableIdmProperties idmProperties,
                          FlowableMailProperties mailProperties) {
        super(flowableProperties, processProperties, idmProperties, mailProperties);
    }

    @Override
    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource, PlatformTransactionManager platformTransactionManager,
                                                                             @Process ObjectProvider<AsyncExecutor> asyncExecutorProvider) throws IOException {

        return super.springProcessEngineConfiguration(dataSource, platformTransactionManager, asyncExecutorProvider);
    }

    @Bean
    @Override
    public ProcessEngineFactoryBean processEngine(SpringProcessEngineConfiguration configuration) throws Exception {
        //注入配置用户任务行为工厂配置
        configuration.setActivityBehaviorFactory(configTaskUserActivityBehaviorFactory());

        //注入全局监听
        List<FlowableEventListener> eventListeners = new ArrayList<>();
        //自动完成首个任务监听器
        eventListeners.add(new AutoCompleteFirstTaskListener());
        //添加业务流程监听器
        //eventListeners.add(new CustomEndListener());
        configuration.setEventListeners(eventListeners);
        return super.processEngine(configuration);
    }

    @Bean
    public FlowableModelerAppProperties flowableModelerAppProperties() {
        return new FlowableModelerAppProperties();
    }


    public DefaultActivityBehaviorFactoryExt configTaskUserActivityBehaviorFactory() {
        return new DefaultActivityBehaviorFactoryExt();
    }
}
