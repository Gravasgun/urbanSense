package com.cqupt.urbansense.controller;

import com.cqupt.urbansense.service.ClassificationService;
import com.cqupt.urbansense.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classification")
public class ClassificationController {
    @Autowired
    private ClassificationService classificationService;

    @GetMapping("/getAll")
    public ResponseResult getAllClassification(){
        return classificationService.getAllClassification();
    }
}
