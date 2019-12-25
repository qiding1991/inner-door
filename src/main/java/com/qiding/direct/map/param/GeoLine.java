package com.qiding.direct.map.param;

import lombok.Data;

import java.util.List;
import java.util.Map;

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
}
