package com.cqupt.urbansense.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.urbansense.bean.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
