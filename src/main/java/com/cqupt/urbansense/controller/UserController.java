package com.cqupt.urbansense.controller;

import com.cqupt.urbansense.bean.User;
import com.cqupt.urbansense.constants.JwtConstant;
import com.cqupt.urbansense.dtos.UserLoginDto;
import com.cqupt.urbansense.dtos.UserLoginVO;
import com.cqupt.urbansense.service.UserService;
import com.cqupt.urbansense.utils.AppJwtUtil;
import com.cqupt.urbansense.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseResult saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserLoginDto userLoginDto) {
        log.info("微信用户登录：{}", userLoginDto.getCode());
        //微信登录
        User user = userService.wxLogin(userLoginDto);
        AppJwtUtil.getToken(Long.parseLong(String.valueOf(user.getId().longValue())));
        //为微信用户生成jwt令牌
        String token = AppJwtUtil.getToken(user.getId().longValue());
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId().longValue())
                .openid(user.getOpenId())
                .token(token)
                .build();
        return ResponseResult.okResult(userLoginVO);
    }
}
