package com.qiding.direct.map.param;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class MapBaseInfo {
    @Id
    String id;
    String type;
    String floor;
    String fileName;
    Integer order;
    String geometry;
}
