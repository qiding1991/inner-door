package com.qiding.direct.map.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoProperties {
    @Id
    private String uuid;
    private String floor;
    private String fileName;
    private Map<String,String> properties;
}
