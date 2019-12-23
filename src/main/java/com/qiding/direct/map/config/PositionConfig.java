package com.qiding.direct.map.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "direct.server")
@Data
public class PositionConfig {
	private String host;
	private Integer port;
}
