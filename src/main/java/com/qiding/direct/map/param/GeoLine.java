package com.qiding.direct.map.param;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        this.geometry.coordinates= this.geometry.coordinates
                .parallelStream()
                .map(list-> list.parallelStream().map(x->changePrecision(x)).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
