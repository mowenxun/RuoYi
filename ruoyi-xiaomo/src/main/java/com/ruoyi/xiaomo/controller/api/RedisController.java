package com.ruoyi.xiaomo.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.Status;
import com.ruoyi.common.utils.redis.RedisUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.xiaomo.config.RedisSubListenerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther mowenxun
 * @Date 2021/12/15
 */
@RestController
@RequestMapping("/api/redis")
public class RedisController {


    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/add")
    @ResponseBody
    public AjaxResult add() {
        redisUtil.set("test", "520yaner");
        String orderCode = redisUtil.getSeqNoByRedis("test", 5);
        return AjaxResult.success(orderCode);
    }

    @GetMapping("/distributedLock")
    @ResponseBody
    public AjaxResult distributedLock() {
        Boolean f = redisUtil.tryGetDistributedLock("yaner520", UUID.fastUUID().toString(), 50000);
        return AjaxResult.success(f);
    }

    /**
     * 测试消息队列
     *
     * @return
     */
    @GetMapping("/messageChanel")
    @ResponseBody
    public AjaxResult messageChanel() {
        List<SysUser> list = new ArrayList<>();
        SysUser user = new SysUser();
        user.setUserId(1000L);
        user.setUserName("yaner");
        user.setPassword("距离100000码");
        SysUser user1 = new SysUser();
        user1.setUserId(1001L);
        user1.setUserName("xiaoting");
        user1.setPassword("距离100000码");
        list.add(user);
        list.add(user1);
        redisUtil.convertAndSend(RedisSubListenerConfig.OA_USER_LOGIN, JSONArray.toJSONString(list));
        return AjaxResult.success(JSONArray.toJSONString(list));
    }

    @GetMapping("/addList")
    public AjaxResult addListLeft() {
        List<SysUser> list = new ArrayList<>();
        SysUser user = new SysUser();
        user.setUserId(1000L);
        user.setUserName("yaner");
        user.setPassword("距离100000码");
        SysUser user1 = new SysUser();
        user1.setUserId(1001L);
        user1.setUserName("xiaoting");
        user1.setPassword("距离100000码");
        list.add(user);
        list.add(user1);
        String str = JSON.toJSONString(list);
        System.out.println("str====" + str);//没有反斜杠，前后无双引号，但是存入redis中就会有反斜杠和前后双引号，
        // 这导致读取再转化的时候要先处理才能转成对应的实体，redis反序列化没设置
        redisUtil.addToListLeft("love_key", Status.ExpireEnum.UNREAD_MSG, str);
        return AjaxResult.success();
    }
}
