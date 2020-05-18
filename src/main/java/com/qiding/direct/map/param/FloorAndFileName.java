package com.qiding.direct.map.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FloorAndFileName {
    @Id
    private String floor;
    private List<String> fileName;
}
