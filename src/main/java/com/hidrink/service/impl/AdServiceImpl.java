package com.hidrink.service.impl;

import com.hidrink.mapper.AdMapper;
import com.hidrink.pojo.AD;
import com.hidrink.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdServiceImpl implements AdService {
    @Autowired
    public AdMapper adMapper;

    @Override
    public List<AD> getAD() {
        return adMapper.getAllAD();
    }

    @Override
    public boolean updateAD(AD ad) {
        ad.setUpdateTime(LocalDateTime.now());
        return adMapper.updateADById(ad);
    }

    @Override
    public Integer insertAD(AD ad) {
        ad.setCreateTime(LocalDateTime.now());
        ad.setUpdateTime(LocalDateTime.now());
        adMapper.insertAD(ad);
        return adMapper.getLatest();
    }

    @Override
    public boolean deleteAD(Integer id) {
        return adMapper.deleteADById(id);
    }

    @Override
    public boolean uploadFile(Integer id, String img) {
        return adMapper.updateImgById(id,img,LocalDateTime.now());
    }

    @Override
    public List<AD> getADImg() {
        return adMapper.getImg();
    }
}
