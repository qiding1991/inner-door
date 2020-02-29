package com.qiding.direct.map.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiding.direct.map.param.Geo;
import com.qiding.direct.map.param.InnerMapInfo;
import com.qiding.direct.map.param.MapBaseInfo;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;


@Log4j2
@Service
public class GeoMapService {
    @Autowired
    MongoTemplate mongoTemplate;



    public void addMapData(InnerMapInfo mapInfo){
        Query query=Query.query(Criteria.where("fileName").is(mapInfo.getFileName()))
                .addCriteria(Criteria.where("floor").is(mapInfo.getFloor()));
        Document document=Document.parse(new Gson().toJson(mapInfo));
        Update update=Update.fromDocument(document,new String[]{});
        mongoTemplate.upsert(query,update,mapInfo.getClass());
    }

    public InnerMapInfo getMapData(InnerMapInfo mapInfo){
        Query query=Query.query(Criteria.where("fileName").is(mapInfo.getFileName()))
                .addCriteria(Criteria.where("geometry").is(mapInfo.getGeometry()))
                .addCriteria(Criteria.where("floor").is(mapInfo.getFloor()));
        return  mongoTemplate.findOne(query,mapInfo.getClass(),"innerMapInfo");
    }


    public  List<InnerMapInfo> getMapData(InnerMapInfo mapInfo, String floor){
        Query query=Query.query(Criteria.where("floor").is(floor))
                .addCriteria(Criteria.where("geometry").is(mapInfo.getGeometry()))
                .with(Sort.by(Sort.Direction.ASC,"order"));
        List<InnerMapInfo> infoList=new ArrayList<>();
        infoList.addAll(mongoTemplate.find(query,mapInfo.getClass(),"innerMapInfo"));
        log.info("返回值:{}",infoList);
        return infoList;
    }

    public  List<MapBaseInfo> findMapBaseInfo(){
        return mongoTemplate.findAll(MapBaseInfo.class,"innerMapInfo");
    }


    public  void delMapData(String floor,String fileName){
        Query query=Query.query(Criteria.where("fileName").is(fileName))
                .addCriteria(Criteria.where("floor").is(floor));
        mongoTemplate.remove(query,"innerMapInfo");
    }
}
