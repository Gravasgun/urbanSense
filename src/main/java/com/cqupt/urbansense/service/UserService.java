package com.cqupt.urbansense.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.urbansense.bean.User;
import com.cqupt.urbansense.dtos.UserLoginDto;
import com.cqupt.urbansense.utils.ResponseResult;

import java.util.Map;

public interface UserService extends IService<User> {
    ResponseResult saveUser(User user);

    ResponseResult wxLogin(UserLoginDto userLoginDto);
    String getUnionId(User user);
}
