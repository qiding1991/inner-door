package com.qiding.direct.map.param;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Data
public class TypeInfo {
    @Id
    private String typeId;
    private String typeName;
    private String typeData;
}
