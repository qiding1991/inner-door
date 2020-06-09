package com.qiding.direct.map.controller;


import com.qiding.direct.map.param.AddPark;
import com.qiding.direct.map.param.CommonResult;
import com.qiding.direct.map.param.ParkState;
import com.qiding.direct.map.service.ParkStateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Api(value = "停车管理")
@RestController
public class ParkingController {

    @Autowired
    ParkStateService parkStateService;

    @ApiOperation(value = "获取停车列表", httpMethod = "GET")
    @GetMapping("park/list")
    public CommonResult parkList(@ModelAttribute ParkState parkState){
        List<ParkState> infoList= parkStateService.parkStateList(parkState);
        return CommonResult.builder()
                .data(infoList)
                .code(200)
                .message("success").build();
    }


    @ApiOperation(value = "添加停车信息")
    @PostMapping("admin/park/add")
    public CommonResult parkAdd(@RequestBody AddPark parkState){
        parkStateService.addParkState(parkState.parkState());
        return CommonResult.builder().code(200).message("success").build();
    }

    @ApiOperation(value = "删除车位信息")
    @PostMapping("admin/park/del/{uuid}")
    public CommonResult parkDel(@PathVariable(value = "uuid")  String uuid){
        ParkState parkState=new ParkState();
        parkState.setUuid(uuid);
        parkStateService.removeParkState(parkState);
        return CommonResult.builder().code(200).message("success").build();
    }


    @ApiOperation(value = "更新车位信息")
    @PostMapping("park/update")
    public CommonResult parkUpdate(@RequestBody ParkState parkState){
        parkStateService.saveParkState(parkState);
        return CommonResult.builder().code(200).message("success").build();
    }


}
