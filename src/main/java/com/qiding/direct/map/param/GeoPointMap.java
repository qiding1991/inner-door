package com.qiding.direct.map.param;

import com.qiding.direct.map.common.Geometry;

public class GeoPointMap extends InnerMapInfo<GeoPoint> {
    public GeoPointMap() {
        super();
        setGeometry(Geometry.Point.getString());
    }
}
