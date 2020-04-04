package com.qiding.direct.map.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiding.direct.map.common.Geometry;
import com.qiding.direct.map.param.*;
import com.qiding.direct.map.service.CacheService;
import com.qiding.direct.map.service.GeoMapService;
import com.qiding.direct.map.service.GeoPropertiesService;
import com.qiding.direct.map.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Log4j2
@CrossOrigin
@Api(tags = "楼层数据管理")
@RestController
@RequestMapping("admin/floor")
public class GeoDataController {

    @Autowired
    GeoPropertiesService propertiesService;

    @Autowired
    GeoMapService geoMapService;

    @Autowired
    CacheService cacheService;


    @Autowired
    SearchService searchService;


    private Gson gson = new Gson();

    @ApiOperation(value = "上传地图数据", httpMethod = "POST")
    @PostMapping("upload/{geometry}/{floor}/{filename}/{order}")
    public CommonResult uploadMapInfo(@ApiParam(value = "文件") @RequestParam(value = "geofile") MultipartFile geofile,
                                      @ApiParam(value = "楼层") @PathVariable(value = "floor") String floor,
                                      @ApiParam(value = "图层顺序") @PathVariable(value = "order") Integer order,
                                      @ApiParam(value = "文件类型 Point, LineString, Polygon") @PathVariable(value = "geometry") String geometry,
                                      @ApiParam(value = "文件名称") @PathVariable(value = "filename") String filename) {
        try {
            String fileInfo = new String(geofile.getBytes(), "utf-8");
            InnerMapInfo<Geo> mapInfo;
            if (Geometry.Point.getString().equals(geometry)) {
                mapInfo = gson.fromJson(fileInfo, new TypeToken<InnerMapInfo<GeoPoint>>() {
                }.getType());
            } else if (Geometry.LineString.getString().equals(geometry)) {
                mapInfo = gson.fromJson(fileInfo, new TypeToken<InnerMapInfo<GeoLine>>() {
                }.getType());
            } else {
                mapInfo = gson.fromJson(fileInfo, new TypeToken<InnerMapInfo<GeoPolygon>>() {
                }.getType());
            }
            mapInfo.setGeometry(geometry);
            mapInfo.setFileName(filename);
            mapInfo.setFloor(floor);
            mapInfo.setOrder(order);

            log.info("upload map data:param {}",mapInfo);

            //删除属性信息
            propertiesService.delProperties(floor,filename);
            //重新添加数据信息
            mapInfo.getFeatures().stream().forEach(geo -> addProperties(geo,floor,filename));
            //添加数据文件
            geoMapService.addMapData(mapInfo);
            //更新缓存
            cacheService.incrVersion();
            //删除所有的缓存
            cacheService.cleanCache();
            searchService.refreshIndex();
            return CommonResult.builder().code(200).message("success").build();
        } catch (IOException e) {
            e.printStackTrace();
            return CommonResult.builder().code(400).message("格式不对").build();
        }
    }

    @ApiOperation(value = "获取楼层数据列表", httpMethod = "POST")
    @PostMapping("list")
    public CommonResult list() {
        List<MapBaseInfo> infoList = geoMapService.findMapBaseInfo();
        log.info("floor data={}",infoList);
        return CommonResult.builder().code(200).data(infoList).message("success").build();
    }


    @ApiOperation(value = "删除楼层数据", httpMethod = "POST")
    @PostMapping("del/{floor}/{filename}")
    public CommonResult del(
            @ApiParam(value = "楼层") @PathVariable(value = "floor") String floor,
            @ApiParam(value = "文件名称") @PathVariable(value = "filename") String filename) {
        //删除楼层数据
        geoMapService.delMapData(floor, filename);
        //删除属性信息
        propertiesService.delProperties(floor,filename);
        //版本更新
        cacheService.incrVersion();
        //删除所有的缓存
        cacheService.cleanCache();
        return CommonResult.builder().code(200).message("success").build();
    }

    public  void addProperties(Geo geo,String floor,String fileName){
        String uuid=UUID.randomUUID().toString();
        geo.getProperties().put("uuid",uuid);
        geo.getProperties().put("floor",floor);
        GeoProperties geoProperties=    GeoProperties.builder().fileName(fileName)
                .floor(floor)
                .uuid(uuid)
                .properties(geo.getProperties()).build();
        propertiesService.addProperties(geoProperties);
    }


}
