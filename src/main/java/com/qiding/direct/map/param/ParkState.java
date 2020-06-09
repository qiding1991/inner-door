package com.qiding.direct.map.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ParkState {
    @Id
    private String uuid;

    @ApiModelProperty(value = "唯一标示")
    private String id;//为止ID
    @ApiModelProperty(value = "楼层")
    private String floor;//楼层
    @ApiModelProperty(value = "车位名称")
    private String name;//
    @ApiModelProperty(value = "状态 1 占用  0 未占用")
    private Integer state=0;

    @ApiModelProperty(value = "车牌号")
    private String carID;

    @ApiModelProperty(value = "停车开始时间,查询可以为空")
    private Long startTime;

    @ApiModelProperty(value = "停车结束时间,查询可以为空")
    private Long endTime;



    public ParkState addParkState(){
        startTime=Instant.now().toEpochMilli();
        return this;
    }

    public  Query selectQuery(){
        Query query=new Query();
        List<Criteria> criteriaList=new ArrayList<>();
        if(!StringUtils.isEmpty(uuid)){
            criteriaList.add(Criteria.where("uuid").is(uuid));
        }
        if(!StringUtils.isEmpty(floor)){
            criteriaList.add(Criteria.where("floor").is(floor));
        }
        if(!StringUtils.isEmpty(name)){
            criteriaList.add(Criteria.where("name").is(name));
        }
        if(!StringUtils.isEmpty(state)){
            criteriaList.add(Criteria.where("state").is(state));
        }
        criteriaList.stream().forEach(criteria -> {
            query.addCriteria(criteria);
        });
        return query;
    }

    public Query updateQuery(){
        Query query=new Query();
        query.addCriteria(Criteria.where("uuid").is(uuid));
        return query;
    }

    public Update updateSet(){
        Update update=new  Update();

        update.set("uuid", uuid);

        if(!StringUtils.isEmpty(floor)){
            update.set("floor",floor);
        }

        if(!StringUtils.isEmpty(id)){
            update.set("id",id);
        }

        if(!StringUtils.isEmpty(name)){
            update.set("name", name);
        }

        if(!StringUtils.isEmpty(state)){
            update.set("state",state);
        }
        return update;
    }

}
