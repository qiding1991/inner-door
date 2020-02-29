package com.qiding.direct.map.controller;

import com.google.gson.internal.$Gson$Preconditions;
import com.qiding.direct.map.param.CommonResult;
import com.qiding.direct.map.param.UserInfo;
import com.qiding.direct.map.service.CacheService;
import com.qiding.direct.map.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.valueextraction.UnwrapByDefault;

@Log4j2
@CrossOrigin
@Api(tags = "用户登陆")
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    CacheService cacheService;


    @ApiOperation(value = "用户登陆", httpMethod = "POST")
    @PostMapping("login")
    public CommonResult login(@RequestBody UserInfo userInfo){
        log.info("login param：{}",userInfo);
        String username=userInfo.getUsername();
        String password=DigestUtils.md5DigestAsHex(userInfo.getPassword().getBytes());
        if(userInfoService.login(username,password)){
            String token=cacheService.token(username);
            return CommonResult.builder().code(200).data(token).message("success").build();
        }else {
            return CommonResult.builder().code(-1).message("用户名或密码错误").build();
        }
    }



}
