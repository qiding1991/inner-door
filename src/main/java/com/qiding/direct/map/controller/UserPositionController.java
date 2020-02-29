package com.qiding.direct.map.controller;

import com.google.common.collect.ImmutableList;
import com.qiding.direct.map.common.Geometry;
import com.qiding.direct.map.param.*;
import com.qiding.direct.map.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@CrossOrigin
@Api(tags = "室内导航api")
@RestController
public class UserPositionController {


    @Autowired
    PositionService positionService;

    @Autowired
    GeoMapService geoMapService;

    @Autowired
    TypeService typeService;

    @Autowired
    CacheService cacheService;

    @Autowired
    GeoPropertiesService propertiesService;




//  SoftReference<Map<String, List<InnerMapInfo>>> reference = new SoftReference<Map<String, List<InnerMapInfo>>>(new HashMap<>());
   // SoftReference<Map<String, List<Geo>>> referenceGeo = new SoftReference<Map<String, List<Geo>>>(new HashMap<>());


    @ApiOperation(value = "获取坐标", httpMethod = "POST")
    @PostMapping("position")
    public CommonResult position(@RequestBody QueryPositionIn position) {
        log.info("获取定位的请求参数，请求参数：{}",position);
        List<MapPosition> infoList = positionService.findPosition(position.getPositionList().stream().toArray(DeviceInfo[]::new));
        return CommonResult.builder().code(200).message("success").data(infoList.get(0)).build();
    }




    @ApiOperation(value = "根据自定义名称获取", httpMethod = "GET")
    @GetMapping("get/{geometry}/{filename}")
    public CommonResult getMapInfo(@ApiParam(value = "文件类型 Point, LineString, Polygon") @PathVariable(value = "geometry") String geometry,
                                   @ApiParam(value = "文件名称") @PathVariable(value = "filename") String filename) {
        InnerMapInfo mapInfo;
        if (Geometry.Point.getString().equals(geometry)) {
            mapInfo = new GeoPointMap();
        } else if (Geometry.LineString.getString().equals(geometry)) {
            mapInfo = new GeoLineMap();
        } else {
            mapInfo = new GeoPolygonMap();
        }
        mapInfo.setGeometry(geometry);
        mapInfo.setFileName(filename);
        return CommonResult.builder().code(200).message("success").data(geoMapService.getMapData(mapInfo)).build();
    }


    @ApiOperation(value = "根据楼层获取", httpMethod = "GET")
    @GetMapping("getByFloor/{floor}")
    public CommonResult getByFloor(
            @ApiParam(value = "根据楼层获取") @PathVariable(value = "floor") String floor,
            @ApiParam(value = "文件类型 point，line，Polygon,默认为全部")
            @RequestParam(value = "geometry", required = false) String geometry,
            HttpServletResponse response) {
        InnerMapInfo innerMapInfo = new InnerMapInfo();
        innerMapInfo.setFloor(floor);
        List<InnerMapInfo> infoList = new ArrayList<>();

        InnerMapInfo floorResult = new InnerMapInfo();
        floorResult.setGeometry(geometry);
        floorResult.setId(UUID.randomUUID().toString());
        floorResult.setType("FeatureCollection");
        floorResult.setFileName(floor);
        floorResult.setFloor(floor);

        if (StringUtils.isEmpty(geometry)) {
            floorResult.setGeometry("complex");
			infoList.add(new GeoPolygonMap());
			infoList.add(new GeoLineMap());
            infoList.add(new GeoPointMap());
        } else if (Geometry.Point.getString().equals(geometry)) {
            infoList.add(new GeoPointMap());
        } else if (Geometry.LineString.getString().equals(geometry)) {
            infoList.add(new GeoLineMap());
        } else {
            infoList.add(new GeoPolygonMap());
        }

//        infoList.stream().forEach(mapInfo -> {
//            String cacheKey = mapInfo.getGeometry()+floor;
//            referenceGeo.get().computeIfAbsent(cacheKey, (key) -> {
//                List<Geo> geoList = new ArrayList<>();
//                List<InnerMapInfo> mapInfoList = geoMapService.getMapData(mapInfo, floor);
//                mapInfoList.forEach(detail -> {
//                    List<Geo> features = detail.getFeatures();
//                    if (CollectionUtils.isEmpty(features)) {
//                        return;
//                    }
//                    features.forEach(geo -> {
//                        geo.changePrecision();
//                        geo.changeProperties(propertiesService);
//                    });
//                    geoList.addAll(features);
//                });
//                return geoList;
//            });
//
//            List<Geo> features = referenceGeo.get().get(cacheKey);
//            if (CollectionUtils.isEmpty(features)) {
//                return;
//            }
//            floorResult.getFeatures().addAll(features);
//
////                referenceGeo.get().get(geoMetry).forEach(features -> {
////                    floorResult.getFeatures().addAll(features);
////                });
//
//
////            reference.get().computeIfAbsent(geoMetry, (key) -> geoMapService.getMapData(mapInfo, floor));
////            reference.get().get(geoMetry).forEach(detail -> {
////                List<Geo> features = detail.getFeatures();
////                if (CollectionUtils.isEmpty(features)) {
////                    return;
////                }
////                features.forEach(geo -> {
////                    geo.changePrecision();
////                });
////                floorResult.getFeatures().addAll(features);
////            });
//
//
////            geoMapService.getMapData(mapInfo,floor).forEach(detail->{
////                List<Geo> features=detail.getFeatures();
////                if(CollectionUtils.isEmpty(features)){
////                    return;
////                }
////                floorResult.getFeatures().addAll(features);
////            });
//        });
        response.setDateHeader("expires", System.currentTimeMillis() + 1 * 6000);
        cacheService.updateDataCache(infoList,floorResult,propertiesService,geoMapService);
        return CommonResult.builder().code(200).message("success").data(floorResult).build();
    }

    @ApiOperation(value = "获取导航", httpMethod = "GET")
    @GetMapping("direction")
    public CommonResult direction() {
        List<List<Double>> direction = new ArrayList<>();
        direction.add(ImmutableList.of(Double.valueOf(308242561.9612449), Double.valueOf(506914096.89033026)).asList());
        direction.add(ImmutableList.of(Double.valueOf(308242561.9612449), Double.valueOf(506961415.9401813)).asList());
        return CommonResult.builder().code(200).message("success").data(direction).build();
    }

    @ApiOperation(value = "获取颜色配置信息", httpMethod = "GET")
    @GetMapping("typeList")
    public CommonResult typelist(){
       List<TypeInfo> infoList=typeService.list();
        return CommonResult.builder().code(200).message("success").data(infoList).build();
    }


    @ApiOperation(value = "获取当前数据的版本", httpMethod = "GET")
    @GetMapping("refresh/currentVersion")
    public CommonResult refresh() {
              Integer verison= cacheService.currentVersion();
        return CommonResult.builder().code(200).message("success").data(verison).build();
    }

}

