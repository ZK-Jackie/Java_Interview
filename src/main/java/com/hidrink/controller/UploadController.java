package com.hidrink.controller;

import com.hidrink.pojo.Result;
import com.hidrink.service.AdService;
import com.hidrink.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {
    @Autowired
    EmpService empService;
    @Autowired
    AdService adService;

    @PostMapping("/upload/{empId}")
    public Result update(@PathVariable Integer empId, MultipartFile file) throws Exception {
        String originFileName = file.getOriginalFilename();
        log.info("员工{}上传文件{}",empId,originFileName);

        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\";

        String externName = originFileName.substring(originFileName.lastIndexOf('.'));
        String newName = UUID.randomUUID()+externName;

        //将前端获取到的文件进行储存，可选择oss或本地，此处选择本地
        file.transferTo(new File(path + newName));
        String img = "http://localhost:8090/"+newName;

        empService.uploadFile(empId,img);

        return Result.success(img);
    }

    @PatchMapping("/uploadAD/{adId}")
    public Result updateAD(@PathVariable Integer adId, MultipartFile file) throws Exception {
        String originFileName = file.getOriginalFilename();
        log.info("前端上传广告图片{}",originFileName);

        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\";

        String externName = originFileName.substring(originFileName.lastIndexOf('.'));
        String newName = UUID.randomUUID()+externName;

        //将前端获取到的文件进行储存，可选择oss或本地，此处选择本地
        file.transferTo(new File(path + newName));
        String img = "http://localhost:8090/"+newName;

        adService.uploadFile(adId,img);

        return Result.success(img);
    }

    @GetMapping(value="/{img}")
    public void presentAvatar(@PathVariable String img, HttpServletResponse response) throws IOException {
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\";
        File avatar = new File(path+img);
        InputStream in = new FileInputStream(avatar);
        response.setContentType("image/png");
        IOUtils.copy(in, response.getOutputStream());
        log.info("前端查看图片：{}",img);
    }
}
