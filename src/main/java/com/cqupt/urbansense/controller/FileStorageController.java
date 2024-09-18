package com.cqupt.urbansense.controller;

import com.cqupt.urbansense.service.FileStorageService;
import com.cqupt.urbansense.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class FileStorageController {
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFiles")
    public ResponseResult uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        return fileStorageService.uploadFile(multipartFile);
    }
}
