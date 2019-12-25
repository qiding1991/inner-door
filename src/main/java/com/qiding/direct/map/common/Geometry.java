package com.qiding.direct.map.common;

public enum Geometry {
    Point("Point"),LineString("LineString"),Polygon("Polygon");
    private String string;

    Geometry(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
