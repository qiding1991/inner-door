package com.qiding.direct.map.controller;

import com.qiding.direct.map.common.AppConstant;
import com.qiding.direct.map.config.MapException;
import com.qiding.direct.map.param.CommonResult;
import com.qiding.direct.map.service.CacheService;
import io.swagger.annotations.ResponseHeader;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;

@Log4j2
@Component
@RestControllerAdvice
public class ControllerHandler {
    @Autowired
    CacheService cacheService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${app.auth.verifyUrl}")
    private String tokenVerifyUrl;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public CommonResult commonResult(HttpServletRequest request,   MapException exception) {
       log.error(request.getRequestURI()+" error",exception);
        return CommonResult.builder()
                .code(exception.getCode())
                .message(exception.getMessage()).build();
    }

    @ModelAttribute
    public void preHandler(
            @RequestHeader(value = AppConstant.USER_NAME,required = false) String username,
            @RequestHeader(value = AppConstant.USER_TOKEN,required = false) String userToken,
            HttpServletRequest request) throws MapException {
        String path = request.getRequestURI();
        log.info("username={},usertoken={},uri={},url={}",username,userToken,request.getRequestURI(),request.getRequestURL());
        if (path.startsWith(AppConstant.ADMIN_PATH)) {
//            if (!cacheService.checkToken(username, userToken)) {
//                throw new MapException(100, "usertoken错误");
//            }

             if(!checkToken(username,userToken)){
                 throw new MapException(100, "usertoken错误");
             }
        }
    }

    private Boolean checkToken(String username,String userToken){
        HttpHeaders header = new HttpHeaders();
        // 需求需要传参为form-data格式
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set(AppConstant.USER_NAME,username);
        header.set(AppConstant.USER_TOKEN, userToken);
        HttpEntity httpEntity = new HttpEntity<>(null, header);
        CommonResult commonResult=restTemplate.postForObject(tokenVerifyUrl,httpEntity,CommonResult.class);
        return commonResult.getCode()==200;
    }



}
