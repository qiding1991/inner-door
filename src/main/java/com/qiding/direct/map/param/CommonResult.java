package com.qiding.direct.map.param;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommonResult<T> {
	private int code;
	private String message;
	private T data;

}
