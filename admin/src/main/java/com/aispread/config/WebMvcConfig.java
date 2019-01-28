package com.aispread.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.redimybase.framework.converter.FastJsonHttpDateConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;


/**
 * mvc配置
 * Created by Vim 2019/1/24 12:51
 *
 * @author Vim
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //swagger文档路径映射
        registry.addResourceHandler("/docs/**").addResourceLocations("classpath:/static/docs/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/static/docs/webjars/");
        super.addResourceHandlers(registry);
    }
/*
    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        //加入时间格式转换器
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter =
                new FastJsonHttpDateConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat
        );
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastJsonHttpMessageConverter);
    }*/
}
