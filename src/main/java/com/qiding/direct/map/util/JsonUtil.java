package com.qiding.direct.map.util;

import com.google.gson.Gson;

public class JsonUtil {
    private static Gson gson=new Gson();

    public static  <U,V> U convent(V v,Class<U> u){
        return gson.fromJson(gson.toJson(v),u);
    }

}
