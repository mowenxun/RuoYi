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
        //"[{\"admin\":false,\"dept\":{\"params\":{}},\"params\":{},\"password\":\"距离100000码\",\"userId\":1000,\"userName\":\"yaner\"},{\"admin\":false,\"dept\":{\"params\":{}},\"params\":{},
        // \"password\":\"距离100000码\",\"userId\":1001,\"userName\":\"xiaoting\"}]"
        logger.info("channel={};message={}", RedisSubListenerConfig.OA_USER_LOGIN, message);
        //切掉前后的双引号
        message = message.substring(message.indexOf("\"") + 1, message.lastIndexOf("\""));
        logger.info("message11=={}", message);
        //[{\"admin\":false,\"dept\":{\"params\":{}},\"params\":{},\"password\":\"距离100000码\",\"userId\":1000,\"userName\":\"yaner\"},{\"admin\":false,\"dept\":{\"params\":{}},\"params\":{},
        // \"password\":\"距离100000码\",\"userId\":1001,\"userName\":\"xiaoting\"}]
        //再替换掉反斜杠\,这样才是真正的字符串json格式
        message = message.replaceAll("\\\\", "");
        logger.info("message22=={}", message);
        //[{"admin":false,"dept":{"params":{}},"params":{},"password":"距离100000码","userId":1000,"userName":"yaner"},{"admin":false,"dept":{"params":{}},"params":{},"password":"距离100000码",
        // "userId":1001,"userName":"xiaoting"}]
        List<SysUser> list = JSONArray.parseArray(message, SysUser.class);
        list.forEach(item -> {
            logger.info(item.getUserName());
        });
    }

    /**
     * 接收消息的方法
     */
    public void oaUserUpdate(String message) {
        logger.info("channel={};message={}", RedisSubListenerConfig.OA_USER_UPDATE, message);
        message = message.substring(message.indexOf("\"") + 1, message.lastIndexOf("\""));
        message = message.replaceAll("\\\\", "");
        List<SysUser> list = JSONArray.parseArray(message, SysUser.class);
        list.forEach(item -> {
            logger.info(item.getUserName());
        });
    }
}
