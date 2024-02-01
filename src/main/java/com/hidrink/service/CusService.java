package com.hidrink.service;


import com.hidrink.pojo.Cus;
import com.hidrink.pojo.CusPageInfo;

import java.util.HashMap;
import java.util.List;


public interface CusService {

    HashMap<String,Integer> getDaily();

    Boolean deleteCus(List<Integer> id);

    Boolean addCus(Cus cus);

    Boolean updateCus(Cus cus);

    CusPageInfo getCus(CusPageInfo cusPageInfo);
}
