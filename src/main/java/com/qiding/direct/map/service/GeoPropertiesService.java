package com.qiding.direct.map.service;

import com.qiding.direct.map.param.FloorAndFileName;
import com.qiding.direct.map.param.GeoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class GeoPropertiesService {
    @Autowired
    MongoTemplate mongoTemplate;



    //获取所有的floor和图层
    public List<FloorAndFileName> findAllFloorAndFileName(){
        return findAllFloorAndFileName(null);
    }


    //获取所有的floor和图层
    public List<FloorAndFileName> findAllFloorAndFileName(String floor){
        //选取的字段
        AggregationOperation project=Aggregation.project("floor","fileName");
        //查询条件
        AggregationOperation match=
                StringUtils.isEmpty(floor)?null:
                        Aggregation.match(Criteria.where("floor").is(floor));

        //分组操作
        AggregationOperation group=
                Aggregation.group("floor").addToSet("fileName").as("fileName");

        //数组
        AggregationOperation[] operations=  Stream.of(project,match,group).filter(operation->operation!=null).toArray(AggregationOperation[]::new);

        Aggregation aggregation=Aggregation.newAggregation(operations);

        AggregationResults<FloorAndFileName> result=  mongoTemplate.aggregate(aggregation,GeoProperties.class,FloorAndFileName.class);

        return result.getMappedResults();
    }




    public List<GeoProperties> list(String floor,String fileName,Integer offset, Integer limit){
        Query query=new Query(Criteria.where("floor").is(floor))
                .addCriteria(Criteria.where("fileName").is(fileName));
        query.skip(offset).limit(limit);
        return mongoTemplate.find(query,GeoProperties.class);
    }





    public List<GeoProperties> list(Integer offset, Integer limit){
        Query query=new Query();
        query.skip(offset).limit(limit);
        return mongoTemplate.find(query,GeoProperties.class);
    }



    public Long propertiesCount(){
        Query query=new Query();
        return mongoTemplate.count(query,GeoProperties.class);
    }


    public Long propertiesCount(String floor,String fileName){
        Query query=new Query(Criteria.where("floor").is(floor))
                .addCriteria(Criteria.where("fileName").is(fileName));
        return mongoTemplate.count(query,GeoProperties.class);
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
