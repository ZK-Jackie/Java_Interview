package com.hidrink.service;

import com.hidrink.pojo.AD;

import java.util.List;

public interface AdService {
    List<AD> getAD();

    boolean updateAD(AD ad);

    Integer insertAD(AD ad);

    boolean deleteAD(Integer id);

    boolean uploadFile(Integer id, String img);

    List<AD> getADImg();
}
