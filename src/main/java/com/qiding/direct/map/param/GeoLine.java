package com.qiding.direct.map.param;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Data
public class GeoLine extends Geo {

    /**
     * type : Feature
     * geometry : {"type":"LineString","coordinates":[[102,0],[103,1],[104,0],[105,1]]}
     */

    private String type;
    private Geometry geometry;


    @Data
    public static class Geometry {
        /**
         * type : LineString
         * coordinates : [[102,0],[103,1],[104,0],[105,1]]
         */

        private String type;
        private List<List<Double>> coordinates;
    }

    @Override
    public void changePrecision() {
        log.info("current geo data:{}",this);
        if(this.geometry==null||this.geometry.coordinates==null){
            log.info("data error==>{}",this);
            return;
        }
        try {
            this.geometry.coordinates= this.geometry.coordinates
                    .stream()
                    .map(list-> list.stream().map(x->changePrecision(x)).collect(Collectors.toList()))
                    .collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            log.error("data error ==>{}",this,e);
        }
    }
}
