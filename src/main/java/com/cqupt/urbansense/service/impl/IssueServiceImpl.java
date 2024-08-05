package com.cqupt.urbansense.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.urbansense.bean.Issue;
import com.cqupt.urbansense.enums.AppHttpCodeEnum;
import com.cqupt.urbansense.mapper.IssueMapper;
import com.cqupt.urbansense.service.IssueService;
import com.cqupt.urbansense.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Slf4j
public class IssueServiceImpl extends ServiceImpl<IssueMapper, Issue> implements IssueService {
    @Override
    public ResponseResult saveIssue(Issue issue, MultipartFile multipartFile) {
        //1.参数校验
        if (issue==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        return null;
    }
}
