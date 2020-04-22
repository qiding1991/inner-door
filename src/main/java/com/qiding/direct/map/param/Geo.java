package com.qiding.direct.map.param;

import com.qiding.direct.map.service.GeoPropertiesService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

@Log4j2
@Data
public  abstract class Geo {
    private Map<String,String> properties;
    public abstract   void changePrecision();

    public void changeProperties(GeoPropertiesService propertiesService){
         GeoProperties geoProperties= propertiesService.detail(properties.get("uuid"));
         geoProperties.getProperties().put("floor",geoProperties.getFloor());
         this.properties.putAll(geoProperties.getProperties());
    }


    public Double changePrecision(Double origin){
        log.info("begin:{}",origin);
        DecimalFormat format=new DecimalFormat("0.000");
        Double result=  Double.valueOf(format.format(origin));
        log.info("after{}",result);
        return result;
    }
}
