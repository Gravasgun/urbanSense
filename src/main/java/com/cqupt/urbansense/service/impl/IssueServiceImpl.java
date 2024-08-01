package com.cqupt.urbansense.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.urbansense.bean.Issue;
import com.cqupt.urbansense.mapper.IssueMapper;
import com.cqupt.urbansense.service.IssueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class IssueServiceImpl extends ServiceImpl<IssueMapper, Issue> implements IssueService {
}
