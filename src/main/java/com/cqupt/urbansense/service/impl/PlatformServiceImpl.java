package com.cqupt.urbansense.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.urbansense.bean.Platforms;
import com.cqupt.urbansense.mapper.PlatformMapper;
import com.cqupt.urbansense.service.PlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class PlatformServiceImpl extends ServiceImpl<PlatformMapper, Platforms> implements PlatformService {
}
