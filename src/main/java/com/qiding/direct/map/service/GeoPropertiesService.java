package com.qiding.direct.map.service;

import com.qiding.direct.map.param.GeoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeoPropertiesService {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<GeoProperties> list(Integer offset, Integer limit){
        Query query=new Query();
        query.skip(offset).limit(limit);
        return mongoTemplate.find(query,GeoProperties.class);
    }

    public List<GeoProperties> detail(List<String> uuids){
        Query query= Query.query(Criteria.where("uuid").in(uuids));
        return mongoTemplate.find(query,GeoProperties.class);
    }

    public GeoProperties detail(String uuid){
        Query query= Query.query(Criteria.where("uuid").is(uuid));
        return mongoTemplate.findOne(query,GeoProperties.class);
    }








    public void delProperties(String floor,String fileName){
        Query query= Query.query(Criteria.where("floor").is(floor))
                .addCriteria(Criteria.where("fileName").is(fileName));
        mongoTemplate.remove(query,GeoProperties.class);
    }

    public void updateProperties(GeoProperties geoProperties){
        Query query=Query.query(Criteria.where("uuid").is(geoProperties.getUuid()));
        Update update=Update.update("properties",geoProperties.getProperties());
        mongoTemplate.findAndModify(query,update,GeoProperties.class);
    }

    public void addProperties(GeoProperties geoProperties){
        mongoTemplate.insert(geoProperties);
    }
}
