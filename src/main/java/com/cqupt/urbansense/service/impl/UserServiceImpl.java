package com.cqupt.urbansense.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.urbansense.bean.User;
import com.cqupt.urbansense.mapper.UserMapper;
import com.cqupt.urbansense.service.UserService;
import com.cqupt.urbansense.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public ResponseResult saveUser(User user) {
        return null;
    }
}
