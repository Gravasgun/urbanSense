package com.cqupt.urbansense.controller;

import com.cqupt.urbansense.bean.Issue;
import com.cqupt.urbansense.dtos.IssueDto;
import com.cqupt.urbansense.service.IssueService;
import com.cqupt.urbansense.utils.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/issue")
public class IssueController {
    @Autowired
    private IssueService issueService;

    @PostMapping("/save")
    public ResponseResult saveIssue(@RequestBody IssueDto issueDto) {
        return issueService.saveIssue(issueDto);
    }
}
