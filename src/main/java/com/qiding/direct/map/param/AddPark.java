package com.qiding.direct.map.param;

import com.qiding.direct.map.util.JsonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class AddPark {

    @ApiModelProperty(value = "唯一id")
    private String id;//为止ID
    @ApiModelProperty(value = "楼层")
    private String floor;//楼层
    @ApiModelProperty(value = "名称")
    private String name;//

   public ParkState parkState(){
        ParkState parkState= JsonUtil.convent(this,ParkState.class);
        return parkState;
    }

}
