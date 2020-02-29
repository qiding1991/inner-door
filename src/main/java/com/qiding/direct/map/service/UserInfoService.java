package com.qiding.direct.map.service;

import com.qiding.direct.map.param.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    @Autowired
    MongoTemplate mongoTemplate;


    /**
     * 用户登陆操作
     * @param username
     * @param password
     * @return
     */
    public Boolean login(String username,String password){
        Query query=Query.query(Criteria.where("username").is(username))
                .addCriteria(Criteria.where("password").is(password));
        return mongoTemplate.exists(query, UserInfo.class);
    }





}
