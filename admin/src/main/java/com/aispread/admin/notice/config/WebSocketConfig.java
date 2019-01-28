package com.aispread.admin.notice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import redis.clients.jedis.JedisPoolConfig;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by Vim 2018/12/28 10:57
 *
 * @author Vim
 */
@Configuration
@Slf4j
public class WebSocketConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }




    /**
     * @Bean 和 @ConfigurationProperties
     * 该功能在官方文档是没有提到的，我们可以把@ConfigurationProperties和@Bean和在一起使用。
     * 举个例子，我们需要用@Bean配置一个Config对象，Config对象有a，b，c成员变量需要配置，
     * 那么我们只要在yml或properties中定义了a=1,b=2,c=3，
     * 然后通过@ConfigurationProperties就能把值注入进Config对象中
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.pool")
    public JedisPoolConfig getRedisConfig() {
        return new JedisPoolConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public JedisConnectionFactory getConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        /*factory.setHostName(this.host);
        factory.setPassword(this.password);*/
        factory.setUsePool(true);
        factory.setPoolConfig(getRedisConfig());
        log.info("Jedis连接初始化成功.");
        return factory;
    }


    @Bean
    public RedisTemplate<?, ?> getRedisTemplate() {
        JedisConnectionFactory factory = getConnectionFactory();
        log.info(this.host+","+factory.getHostName()+","+factory.getDatabase());
        log.info(this.password+","+factory.getPassword());
//        factory.setHostName(this.host);
//        factory.setPassword(this.password);
        return new StringRedisTemplate(factory);
    }
    /*@Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(){
        log.info("--port:"+port+"--password:"+password+"--timeout+"+timeout);
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        return redisTemplate;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWait);
        config.setTestOnBorrow(true);
        return config;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig){
        JedisConnectionFactory jedisConnectionFactory =new JedisConnectionFactory();
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.setHostName(hostName);
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setPassword(password);
        jedisConnectionFactory.setTimeout(timeout);
        jedisConnectionFactory.setUsePool(true);
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }*/

}
