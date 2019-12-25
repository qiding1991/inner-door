package com.qiding.direct.map.param;

import com.qiding.direct.map.common.Geometry;

public class GeoPolygonMap extends InnerMapInfo<GeoPolygon> {
    public GeoPolygonMap() {
        super();
        setGeometry(Geometry.Polygon.getString());
    }
}
