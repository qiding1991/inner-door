package com.qiding.direct.map.controller;


import com.qiding.direct.map.param.CommonResult;
import com.qiding.direct.map.param.TypeInfo;
import com.qiding.direct.map.service.TypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@CrossOrigin
@Api(tags = "分类管理")
@RestController
@RequestMapping("admin/type")
public class TypeController {

    @Autowired
    TypeService typeService;

    @ApiOperation(value = "分类列表", httpMethod = "GET")
    @GetMapping("list")
    public CommonResult list(){
        List<TypeInfo>infoList=typeService.list();
        log.info("get all type info result:{}",infoList);
        return CommonResult.builder().code(200)
                .data(infoList)
                .message("success")
                .build();
    }

    @ApiOperation(value = "添加分类", httpMethod = "POST")
    @PostMapping("add")
    public CommonResult add(@RequestBody TypeInfo typeInfo){
        log.info("addNewTypeInfo:{}",typeInfo);
        typeService.addTypeInfo(typeInfo);
        return CommonResult.builder().code(200)
                .message("success")
                .build();
    }

    @ApiOperation(value = "删除分类", httpMethod = "POST")
    @PostMapping("del")
    public CommonResult del(@RequestParam(value = "typeName")String typeName){
        typeService.delTypeInfo(typeName);
        return CommonResult.builder().code(200)
                .message("success")
                .build();
    }




    @ApiOperation(value = "更新分类", httpMethod = "POST")
    @PostMapping("update")
    public CommonResult update(@RequestBody TypeInfo typeInfo){
        log.info("updateTypeInfo:{}",typeInfo);
        typeService.updateTypeInfo(typeInfo);
        return CommonResult.builder().code(200)
                .message("success")
                .build();
    }















}
