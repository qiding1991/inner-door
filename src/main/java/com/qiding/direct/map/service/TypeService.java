package com.qiding.direct.map.service;

import com.qiding.direct.map.param.TypeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    @Autowired
    MongoTemplate mongoTemplate;
    public List<TypeInfo> list(){
        return   mongoTemplate.findAll(TypeInfo.class);
    }

    public TypeInfo detail(String typeName){
        Query query= Query.query(Criteria.where("typeName").is(typeName));
        return mongoTemplate.findOne(query,TypeInfo.class);
    }

    public void addTypeInfo(TypeInfo typeInfo){
        mongoTemplate.insert(typeInfo);
    }


    public  void delTypeInfo(String typeName){
        Query query= Query.query(Criteria.where("typeName").is(typeName));
          mongoTemplate.remove(query,TypeInfo.class);
    }



}
