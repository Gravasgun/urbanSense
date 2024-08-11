package com.cqupt.urbansense.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.urbansense.bean.Classification;
import com.cqupt.urbansense.utils.ResponseResult;

public interface ClassificationService extends IService<Classification> {
    ResponseResult getAllClassification();
}
