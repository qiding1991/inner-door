package com.qiding.direct.map.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.geojson.Point;
import com.qiding.direct.map.common.Geometry;
import com.qiding.direct.map.param.*;
import com.qiding.direct.map.service.GeoMapService;
import com.qiding.direct.map.service.PositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@Api(value="室内导航api")
@RestController
public class UserPositionController {

	private Gson gson=new Gson();

	@Autowired
	PositionService positionService;

	@Autowired
	GeoMapService geoMapService;

	@ApiOperation(value = "获取坐标",httpMethod = "POST")
	@PostMapping("position")
	public CommonResult position(@RequestBody QueryPositionIn position){
		List<MapPosition> infoList= positionService
			.findPosition(position.getPositionList().stream().toArray(DeviceInfo[]::new));
		return CommonResult.builder().code(200).message("success").data(infoList.get(0)).build();
	}


	@ApiOperation(value = "上传地图数据",httpMethod = "POST")
	@PostMapping("upload/{geometry}/{floor}/{filename}")
	public  CommonResult uploadMapInfo(@ApiParam(value = "文件")@RequestParam(value = "geofile") MultipartFile geofile,
                                       @ApiParam(value = "楼层")@PathVariable(value = "floor")String  floor,
									   @ApiParam(value = "文件类型 Point, LineString, Polygon")@PathVariable(value = "geometry")String geometry,
									   @ApiParam(value = "文件名称") @PathVariable(value = "filename")String filename){
		try {
			String fileInfo=new String(geofile.getBytes(),"utf-8");
			InnerMapInfo<Geo> mapInfo;
			if(Geometry.Point.getString().equals(geometry)){
				mapInfo=gson.fromJson(fileInfo,new TypeToken<InnerMapInfo<GeoPoint>>(){}.getType());
			}else  if(Geometry.LineString.getString().equals(geometry)){
				mapInfo=gson.fromJson(fileInfo,new TypeToken<InnerMapInfo<GeoLine>>(){}.getType());
			}else{
				mapInfo=gson.fromJson(fileInfo,new TypeToken<InnerMapInfo<GeoPolygon>>(){}.getType());
			}
			mapInfo.setGeometry(geometry);
			mapInfo.setFileName(filename);
            mapInfo.setFloor(floor);
			mapInfo.getFeatures().parallelStream().forEach(geo -> geo.getProperties().put("uuid", UUID.randomUUID().toString()));
			geoMapService.addMapData(mapInfo);
			return CommonResult.builder().code(200).message("success").build();
		} catch (IOException e) {
			e.printStackTrace();
			return  CommonResult.builder().code(400).message("格式不对").build();
		}
	}

	@ApiOperation(value = "根据自定义名称获取",httpMethod = "GET")
	@GetMapping("get/{geometry}/{filename}")
	public CommonResult getMapInfo(   @ApiParam(value = "文件类型 Point, LineString, Polygon")@PathVariable(value = "geometry")String geometry,
									  @ApiParam(value = "文件名称")@PathVariable(value = "filename")String filename){
		InnerMapInfo mapInfo;
		if(Geometry.Point.getString().equals(geometry)){
			mapInfo=new GeoPointMap();
		}else  if(Geometry.LineString.getString().equals(geometry)){
			mapInfo=new GeoLineMap();
		}else{
			mapInfo=new GeoPolygonMap();
		}
		mapInfo.setGeometry(geometry);
		mapInfo.setFileName(filename);
		return CommonResult.builder().code(200).message("success").data(geoMapService.getMapData(mapInfo)).build();
	}



    @ApiOperation(value = "根据楼层获取",httpMethod = "GET")
    @GetMapping("getByFloor/{floor}")
    public CommonResult getByFloor(
            @ApiParam(value = "根据楼层获取")@PathVariable(value = "floor")String floor,
            @ApiParam(value = "文件类型 point，line，Polygon,默认为全部")
            @RequestParam(value = "geometry",required = false) String geometry
    ){
        InnerMapInfo innerMapInfo=new InnerMapInfo();
        innerMapInfo.setFloor(floor);
        List<InnerMapInfo> infoList=new ArrayList<>();

        InnerMapInfo floorResult=new InnerMapInfo();
        floorResult.setGeometry(geometry);
        floorResult.setId(UUID.randomUUID().toString());
        floorResult.setType("FeatureCollection");
        floorResult.setFileName(floor);
        floorResult.setFloor(floor);

        if(StringUtils.isEmpty(geometry)){
            floorResult.setGeometry("complex");
            infoList.add(new GeoPointMap());
            infoList.add(new GeoLineMap());
            infoList.add(new GeoPolygonMap());
        }else if(Geometry.Point.getString().equals(geometry)){
            infoList.add(new GeoPointMap());
        }else  if(Geometry.LineString.getString().equals(geometry)){
            infoList.add(new GeoLineMap());
        }else {
            infoList.add(new GeoPolygonMap());
        }

        infoList.parallelStream().forEach(mapInfo -> {
            geoMapService.getMapData(mapInfo,floor).forEach(detail->{
                List<Geo> features=detail.getFeatures();
                if(CollectionUtils.isEmpty(features)){
                    return;
                }
                floorResult.getFeatures().addAll(features);
            });
        });
        return CommonResult.builder().code(200).message("success").data(floorResult).build();
    }








}
