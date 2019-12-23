package com.qiding.direct.map.controller;

import com.qiding.direct.map.param.CommonResult;
import com.qiding.direct.map.param.DeviceInfo;
import com.qiding.direct.map.param.MapPosition;
import com.qiding.direct.map.param.QueryPositionIn;
import com.qiding.direct.map.service.PositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Api(value="室内导航api")
@RestController
public class UserPositionController {

	@Autowired
	PositionService positionService;

	@ApiOperation(value = "获取坐标",httpMethod = "POST")
	@PostMapping("position")
	public CommonResult position(@RequestBody QueryPositionIn position){
		List<MapPosition> infoList= positionService
			.findPosition(position.getPositionList().stream().toArray(DeviceInfo[]::new));
		return CommonResult.builder().code(200).message("success").data(infoList.get(0)).build();
	}
}
