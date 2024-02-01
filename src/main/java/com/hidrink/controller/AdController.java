package com.hidrink.controller;

import com.hidrink.annotaion.Recorder;
import com.hidrink.pojo.AD;
import com.hidrink.pojo.Result;
import com.hidrink.service.AdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class AdController {
    @Autowired
    public AdService adService;

    @GetMapping("/AD")
    public Result getAD(){
        log.info("前端管理员获取广告列表");
        return Result.success(adService.getAD());
    }

    @GetMapping("/loadAD")
    public Result loadAD(){ //用户加载广告，请求广告链接
        return Result.success(adService.getADImg());
    }

    @Recorder
    @PostMapping("/addAD")
    public Result insertAD(@RequestBody AD ad){
//        log.info("前端管理员添加广告：{}",ad.getIntro());
        return Result.success(adService.insertAD(ad));
    }

    @Recorder
    @PutMapping("/updateAD")
    public Result updateAD(@RequestBody AD ad){
        log.info("前端管理员修改id为：{}的广告信息",ad.getId());
        return Result.success(adService.updateAD(ad));
    }

    @Recorder
    @DeleteMapping("/deleteAD/{id}")
    public Result deleteAD(@PathVariable Integer id){
        return Result.success(adService.deleteAD(id));
    }
}
