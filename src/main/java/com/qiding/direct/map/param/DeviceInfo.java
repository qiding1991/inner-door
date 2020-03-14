package com.qiding.direct.map.param;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
public class DeviceInfo {
	private int major;
	private int minor;
	private int rssi;
}
