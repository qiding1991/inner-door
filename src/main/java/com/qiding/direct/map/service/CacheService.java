package com.qiding.direct.map.service;

import com.qiding.direct.map.param.Geo;
import com.qiding.direct.map.param.InnerMapInfo;
import com.qiding.direct.map.param.MapBaseInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Service
public class CacheService {


    Map<String, String> cacheMap = new HashMap<>();

    AtomicInteger currentVersion = new AtomicInteger(1);


    volatile SoftReference<Map<String, List<Geo>>> referenceGeo = new SoftReference<Map<String, List<Geo>>>(new ConcurrentHashMap<>());


    public String token(String username) {
        return cacheMap.computeIfAbsent(username,(key)-> UUID.randomUUID().toString());
    }

    public Boolean checkToken(String username, String token) {
        log.info("username={},token={}",username,token);
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(token)) return false;
        log.info("username={},token={},realtoken={}",username,token,cacheMap.get(username));
        return token.equalsIgnoreCase(cacheMap.get(username));
    }


    public void incrVersion() {
        currentVersion.incrementAndGet();
    }

    public Boolean change(Integer version) {
        return version.compareTo(currentVersion.get()) != 0;
    }

    public Integer currentVersion() {
        return currentVersion.get();
    }


    public void cleanCache() {
        log.info(referenceGeo.get());
        if(referenceGeo.get()!=null){
            referenceGeo.get().clear();
        }else{
            referenceGeo = new SoftReference<>(new HashMap<>());
        }
    }


    public void updateDataCache(List<InnerMapInfo> infoList, InnerMapInfo floorResult,
                                GeoPropertiesService propertiesService, GeoMapService geoMapService) {
        String floor = floorResult.getFloor();

        log.info("updateDataCache param:infoList={},floorResult={}",infoList,floorResult);
        //gc以后，重新设置值
        synchronized (this){
            if(referenceGeo==null||referenceGeo.get()==null){
                referenceGeo = new SoftReference<>(new ConcurrentHashMap<>());
            }
        }

        infoList.stream().forEach(mapInfo -> {
            String cacheKey = mapInfo.getGeometry() + floor;
            log.info("referenceGeo, referenceGeo={},cacheData={}",referenceGeo,referenceGeo.get());
            referenceGeo.get().computeIfAbsent(cacheKey, (key) -> {
                List<Geo> geoList = new ArrayList<>();
                List<InnerMapInfo> mapInfoList = geoMapService.getMapData(mapInfo, floor);

                log.info("mapInfoList={}",mapInfoList);
                if(CollectionUtils.isEmpty(mapInfoList)){
                    log.info("mapInfo={},floor={},mapInfoList={}",mapInfo,floor,mapInfoList);
                    return new ArrayList<>();
                }

                mapInfoList.forEach(detail -> {
                    List<Geo> features = detail.getFeatures();
                    if (CollectionUtils.isEmpty(features)) {
                        return;
                    }
                    features.forEach(geo -> {
                        geo.changePrecision();
                        geo.changeProperties(propertiesService);
                    });
                    geoList.addAll(features);
                });
                return geoList;
            });

            List<Geo> features = referenceGeo.get().get(cacheKey);
            if (CollectionUtils.isEmpty(features)) {
                return;
            }
            floorResult.getFeatures().addAll(features);
        });
    }


}
