package com.qiding.direct.map.param;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

@Log4j2
@Data
public  abstract class Geo {
    private Map<String,String> properties;
    public abstract   void changePrecision();

    public Double changePrecision(Double origin){
        log.info("原始值:{}",origin);
        DecimalFormat format=new DecimalFormat("0.000");
        Double result=  Double.valueOf(format.format(origin));
        log.info("转换后{}",result);
        return result;
    }
}
