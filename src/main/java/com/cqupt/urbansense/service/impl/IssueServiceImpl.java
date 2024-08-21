package com.cqupt.urbansense.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.urbansense.bean.Issue;
import com.cqupt.urbansense.bean.User;
import com.cqupt.urbansense.dtos.IssueDto;
import com.cqupt.urbansense.enums.AppHttpCodeEnum;
import com.cqupt.urbansense.mapper.IssueMapper;
import com.cqupt.urbansense.service.IssueService;
import com.cqupt.urbansense.utils.AppThreadLocalUtil;
import com.cqupt.urbansense.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cqupt.urbansense.constants.StatusConstant.NOT_DEAL;

@Service
@Transactional
@Slf4j
public class IssueServiceImpl extends ServiceImpl<IssueMapper, Issue> implements IssueService {
    @Autowired
    private MinIOFileStorageService fileStorageService;

    @Override
    public ResponseResult saveIssue(IssueDto issueDto, MultipartFile multipartFile) {
        //1.check param
        if (issueDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        User user = AppThreadLocalUtil.getUser();
        if (user != null && StringUtils.isNotBlank(user.getUnionId())) {
            issueDto.setUnionId(user.getUnionId());
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN, "请登录！");
        }
        if (issueDto.getPlatformId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PLATFORM_ID_DOT_EXIST, "平台ID不能为空！");
        }
        if (StringUtils.isBlank(issueDto.getClassification())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CLASSIFICATION_DOT_EXIST, "问题分类不能为空！");
        }
        if (StringUtils.isBlank(issueDto.getDescription())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DESCRIPTION_DOT_EXIST, "问题描述不能为空！");
        }
        Issue issue = new Issue();
        //2.copy properties
        BeanUtils.copyProperties(issueDto, issue);
        //2.1 additional properties
        issue.setStatus(NOT_DEAL);
        issue.setSubmittedTime(new Date());
        issue.setUpdatedTime(new Date());
        List<String> photoFileNames = issueDto.getPhotoFileNames();
        List<String> videoFileNames = issueDto.getVideoFileNames();
        //2.2 deal with files
        List<String> photoUrlList = uploadFiles(photoFileNames, true, multipartFile);
        List<String> videoUrlList = uploadFiles(videoFileNames, false, multipartFile);
        issue.setPhotoUrl(photoUrlList);
        issue.setVideoUrl(videoUrlList);
        //3.save bean to MySQL database
        this.save(issue);
        //4.return the result
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 文件上传封装
     *
     * @param filenameList
     * @param isPhoto
     * @param multipartFile
     * @return
     */
    private List<String> uploadFiles(List<String> filenameList, Boolean isPhoto, MultipartFile multipartFile) {
        List<String> pathUrlList = new ArrayList<>();
        if (!filenameList.isEmpty()) {
            for (String fileName : filenameList) {
                try {
                    String fileNameUrl = fileStorageService.uploadFile(fileName, multipartFile.getInputStream(), isPhoto);
                    pathUrlList.add(fileNameUrl);
                } catch (IOException e) {
                    log.info("上传文件失败——" + multipartFile.getOriginalFilename());
                    throw new RuntimeException(e);
                }
            }
        }
        return pathUrlList;
    }
}
