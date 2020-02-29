package com.qiding.direct.map.param;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class InnerMapInfo<T extends Geo>  extends MapBaseInfo{
    List<T> features=new ArrayList<>();
}
