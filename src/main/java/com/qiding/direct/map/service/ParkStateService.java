package com.qiding.direct.map.service;

import com.qiding.direct.map.param.ParkState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkStateService {

    @Autowired
    MongoTemplate mongoTemplate;


    /**
     * 获取停车列表
     * @param parkState
     * @return
     */
    public List<ParkState> parkStateList(ParkState parkState){
        Query query=parkState.selectQuery();
        return mongoTemplate.find(query,ParkState.class);
    }

    /**
     * 删除停车记录
     * @param parkState
     */
    public void removeParkState(ParkState parkState){
        Query query=parkState.selectQuery();
        mongoTemplate.findAndRemove(query,ParkState.class);
    }

    /**
     * 添加停车记录
     * @param addParkState
     */
    public void addParkState(ParkState addParkState) {
        mongoTemplate.insert(addParkState.addParkState());
    }

    /**
     * 更新记录
     * @param updateState
     */
    public void saveParkState(ParkState updateState){
        Query query=updateState.updateQuery();
        Update update=updateState.updateSet();
        mongoTemplate.findAndModify(query,update,ParkState.class);
    }

}
