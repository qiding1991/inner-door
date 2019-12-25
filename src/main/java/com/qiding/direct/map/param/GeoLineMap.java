package com.qiding.direct.map.param;

import com.qiding.direct.map.common.Geometry;

public class GeoLineMap extends InnerMapInfo<GeoLine> {
    public GeoLineMap() {
        super();
        setGeometry(Geometry.LineString.getString());
    }
}
