package com.qiding.direct.map.param;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapPosition {
	private String positionX;
	private String positionY;
	private String positionZ;
}
