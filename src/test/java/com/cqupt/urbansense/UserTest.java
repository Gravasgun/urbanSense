package com.cqupt.urbansense;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqupt.urbansense.bean.User;
import com.cqupt.urbansense.mapper.UserMapper;
import com.cqupt.urbansense.service.ClassificationService;
import com.cqupt.urbansense.utils.ResponseResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ClassificationService classificationService;

    @Test
    public void testUser() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, 1);
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }

    @Test
    public void testClassification() {
        ResponseResult result = classificationService.getAllClassification();
        List<String> data = (List<String>)result.getData();
        System.out.println(data);
    }
}
