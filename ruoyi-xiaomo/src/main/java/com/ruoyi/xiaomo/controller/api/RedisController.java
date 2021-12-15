package com.ruoyi.xiaomo.controller.api;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
}
