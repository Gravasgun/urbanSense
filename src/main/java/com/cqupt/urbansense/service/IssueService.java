package com.cqupt.urbansense.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.urbansense.bean.Issue;
import com.cqupt.urbansense.dtos.IssueDto;
import com.cqupt.urbansense.utils.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface IssueService extends IService<Issue> {
    ResponseResult saveIssue(IssueDto issueDto);
}
