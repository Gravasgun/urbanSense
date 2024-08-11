package com.cqupt.urbansense.controller;

import com.cqupt.urbansense.bean.User;
import com.cqupt.urbansense.service.UserService;
import com.cqupt.urbansense.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseResult saveUser(@RequestBody User user){
        return userService.saveUser(user);
    }
}
