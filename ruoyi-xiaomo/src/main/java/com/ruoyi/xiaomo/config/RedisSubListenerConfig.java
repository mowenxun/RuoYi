package com.ruoyi.xiaomo.config;

import com.ruoyi.xiaomo.service.sublistener.MessageReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * 订阅者
 *
 * @Auther mowenxun
 * @Date 2021/12/16
 */
@Configuration
public class RedisSubListenerConfig {

    //不同的频道名
    public static final String OA_USER_LOGIN = "oa_user_login";
    public static final String OA_USER_UPDATE = "oa_user_update";

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     *
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter, MessageListenerAdapter listenerAdapter2) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //订阅了一个叫testchannel 的通道
        container.addMessageListener(listenerAdapter, new PatternTopic(RedisSubListenerConfig.OA_USER_LOGIN));
        //订阅了一个叫chat的频道
        container.addMessageListener(listenerAdapter2, new PatternTopic(RedisSubListenerConfig.OA_USER_UPDATE));
        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     *
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "oaUserLogin");
    }

    @Bean
    MessageListenerAdapter listenerAdapter2(MessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "oaUserUpdate");
    }

    /**
     * redis 读取内容的template
     */
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }


}
