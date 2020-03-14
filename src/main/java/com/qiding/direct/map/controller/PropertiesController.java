package com.qiding.direct.map.controller;

import com.google.common.collect.ImmutableMap;
import com.qiding.direct.map.param.CommonResult;
import com.qiding.direct.map.param.GeoProperties;
import com.qiding.direct.map.service.CacheService;
import com.qiding.direct.map.service.GeoPropertiesService;
import com.qiding.direct.map.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@CrossOrigin
@Api(tags = "属性管理")
@RestController
@RequestMapping("admin/properties")
public class PropertiesController {

    @Autowired
    GeoPropertiesService propertiesService;

    @Autowired
    CacheService cacheService;

    @Autowired
    SearchService searchService;

    @ApiOperation(value = "分页获取属性的信息列表", httpMethod = "GET")
    @GetMapping("list/{offset}/{limit}")
    public CommonResult list(
            @PathVariable(value = "offset") int offset,
            @PathVariable(value = "limit") int limit){
        log.info("properties list param:{}",offset,limit);
        List<GeoProperties> infoList= propertiesService.list(offset,limit);
        Long count=propertiesService.propertiesCount();
        log.info("result:{}",infoList);

        ImmutableMap map= ImmutableMap.of("properties",infoList,"count",count);
        log.info("分页获取的结果:{}",map);
        return CommonResult.builder().code(200).message("success")
                .data(map).build();
    }

    @ApiOperation(value = "更新属性信息",httpMethod = "POST")
    @PostMapping("update")
    public CommonResult update(@RequestBody GeoProperties geoProperties){
        propertiesService.updateProperties(geoProperties);
        cacheService.incrVersion();
        cacheService.cleanCache();
        searchService.refreshIndex();
        return CommonResult.builder().code(200).message("success")
               .build();
    }
}
