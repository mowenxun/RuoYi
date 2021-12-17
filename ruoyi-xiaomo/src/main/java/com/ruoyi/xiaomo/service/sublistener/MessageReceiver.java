package com.ruoyi.xiaomo.service.sublistener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.xiaomo.config.RedisSubListenerConfig;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订阅者消费频道业务类
 *
 * @Auther mowenxun
 * @Date 2021/12/16
 */
@Component
public class MessageReceiver {

    Logger logger = LoggerFactory.getLogger(MessageReceiver.class);

    /**
     * 接收消息的方法
     */
    public void oaUserLogin(String message) {
        List<SysUser> list = JSONArray.parseArray(message, SysUser.class);
        list.forEach(item -> {
            logger.info(item.getUserName());
        });
    }

    /**
     * 接收消息的方法
     */
    public void oaUserUpdate(String message) {
        List<SysUser> list = JSONArray.parseArray(message, SysUser.class);
        list.forEach(item -> {
            logger.info(item.getUserName());
        });
    }
}
