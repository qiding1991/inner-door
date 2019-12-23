package com.qiding.direct.map.param;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
public class DeviceInfo {
	private String time;
	private String uuid;
	private int major;
	private int minor;
	private int rssi;
	private String name;
	private String address;
	private int txpower;
}
