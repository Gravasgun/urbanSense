package com.cqupt.urbansense.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.urbansense.bean.Classification;
import com.cqupt.urbansense.mapper.ClassificationMapper;
import com.cqupt.urbansense.service.ClassificationService;
import com.cqupt.urbansense.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class ClassificationServiceImpl extends ServiceImpl<ClassificationMapper, Classification> implements ClassificationService {
    @Override
    public ResponseResult getAllClassification() {
        return ResponseResult.okResult(this.list());
    }
}
