package com.cqupt.urbansense.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.urbansense.bean.User;
import com.cqupt.urbansense.bean.WeChatProperties;
import com.cqupt.urbansense.dtos.UserLoginDto;
import com.cqupt.urbansense.dtos.UserLoginVO;
import com.cqupt.urbansense.mapper.UserMapper;
import com.cqupt.urbansense.service.UserService;
import com.cqupt.urbansense.utils.AppJwtUtil;
import com.cqupt.urbansense.utils.AppThreadLocalUtil;
import com.cqupt.urbansense.utils.ResponseResult;
import com.mysql.jdbc.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ResponseResult saveUser(User user) {
        return null;
    }

    @Override
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    public ResponseResult wxLogin(UserLoginDto userLoginDTO) {
        Map<String, String> map = getWechatInformation(userLoginDTO.getCode());
        //判断openid是否为空，如果为空表示登录失败，抛出异常
        String openId = map.get("openId");
        if (openId == null) {
            throw new RuntimeException("登录失败！");
        }
        //判断当前用户是否为新用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenId, openId);
        User user = userMapper.selectOne(queryWrapper);
        //如果是新用户，自动完成注册
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
            String unionId = map.get("unionId");
            if (StringUtils.isNotBlank(unionId)) {
                user.setUnionId(map.get("unionId"));
            }
            user.setCreatedTime(new Date());
            userMapper.insert(user);
        }
        //为微信用户生成jwt令牌
        String token = AppJwtUtil.getToken(user.getId().longValue());
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId().longValue())
                .openid(user.getOpenId())
                .token(token)
                .build();
        //缓存token
        redisTemplate.opsForValue().set(user.getId() + "token", map.get("sessionKey"), 2, TimeUnit.HOURS);
        //将用户存入threadLocal，以便后续获取unionId
        AppThreadLocalUtil.setUser(user);
        //返回结果
        return ResponseResult.okResult(userLoginVO);
    }

    @Override
    public String getUnionId(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, user.getId());
        User targetUser = userMapper.selectOne(queryWrapper);
        return targetUser.getUnionId();
    }

    /**
     * 调用微信接口服务，获取微信用户的openid
     *
     * @param code
     * @return
     */
    private Map getWechatInformation(String code) {
        //调用微信接口服务，获得当前微信用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        // 使用 UriComponentsBuilder 构建 URL 并附加查询参数
        String url = UriComponentsBuilder.fromHttpUrl(WX_LOGIN)
                .queryParam("appid", map.get("appid"))
                .queryParam("secret", map.get("secret"))
                .queryParam("js_code", map.get("js_code"))
                .queryParam("grant_type", map.get("grant_type"))
                .toUriString();
        //发送http请求
        String responseJson = restTemplate.getForObject(url, String.class);
        JSONObject response = JSON.parseObject(responseJson);
        log.info("微信登录接口请求结果：" + response);
        String openId = response.getString("openid");
        String unionId = response.getString("unionid");
        String sessionKey = response.getString("session_key");
        //返回请求体中的数据
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("openId", openId);
        resultMap.put("unionId", unionId);
        resultMap.put("sessionKey", sessionKey);
        return resultMap;
    }
}
