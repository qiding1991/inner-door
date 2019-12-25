package com.qiding.direct.map.param;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GeoPoint extends Geo {
    private String type;
    private Geometry geometry;

    @Data
    public static class Geometry {
        /**
         * type : Point
         * coordinates : [102,0.5]
         */
        private String type;
        private List<Double> coordinates;

    }
}
