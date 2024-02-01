package com.jackie.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
public class ImgController {

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
