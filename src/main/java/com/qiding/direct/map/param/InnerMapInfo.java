package com.qiding.direct.map.param;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class InnerMapInfo<T extends Geo> {
    @Id
    String id;
    String type;
    String floor;
    String fileName;
    String geometry;
    List<T> features=new ArrayList<>();
}
