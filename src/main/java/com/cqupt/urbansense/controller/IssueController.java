package com.cqupt.urbansense.controller;

import com.cqupt.urbansense.bean.Issue;
import com.cqupt.urbansense.dtos.IssueDto;
import com.cqupt.urbansense.service.IssueService;
import com.cqupt.urbansense.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/issue")
public class IssueController {
    @Autowired
    private IssueService issueService;

    @PostMapping("/save")
    public ResponseResult saveIssue(@RequestBody IssueDto issueDto, MultipartFile multipartFile) {
        return issueService.saveIssue(issueDto, multipartFile);
    }
}
