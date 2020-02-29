package com.qiding.direct.map.param;


import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class UserInfo {
    @Id
    private String id;
    private String username;
    private String password;
}
