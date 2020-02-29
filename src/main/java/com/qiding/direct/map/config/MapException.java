package com.qiding.direct.map.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MapException extends Exception {
    private int code;
    private String message;
}
