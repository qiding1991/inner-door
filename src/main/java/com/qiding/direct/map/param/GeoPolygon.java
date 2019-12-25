package com.qiding.direct.map.param;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GeoPolygon extends Geo {

    /**
     * type : Feature
     * geometry : {"type":"Polygon","coordinates":[[[100,0],[101,0],[101,1],[100,1],[100,0]]]}
     * properties : {"prop0":"value0","prop1":{"this":"that"}}
     */

    private String type;
    private GeometryBean geometry;


    @Data
    public static class GeometryBean {
        /**
         * type : Polygon
         * coordinates : [[[100,0],[101,0],[101,1],[100,1],[100,0]]]
         */

        private String type;
        private List<List<List<Double>>> coordinates;

    }
}
