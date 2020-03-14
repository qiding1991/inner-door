package com.qiding.direct.map.service;

import com.google.common.collect.ImmutableList;
import com.qiding.direct.UserPositionOuterClass;
import com.qiding.direct.UserPositionServiceGrpc;
import com.qiding.direct.map.config.PositionConfig;
import com.qiding.direct.map.param.DeviceInfo;
import com.qiding.direct.map.param.MapPosition;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Service
public class PositionService {

	UserPositionServiceGrpc.UserPositionServiceBlockingStub grpcClient = null;

	public PositionService(PositionConfig positionConfig) {
		ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(positionConfig.getHost(), positionConfig.getPort())
			.usePlaintext(true)
			.build();
		grpcClient = UserPositionServiceGrpc.newBlockingStub(managedChannel);
	}

	public List<MapPosition> findPosition(DeviceInfo... infos) {
		try {
			log.info("请求数据列表:{}",infos);
			UserPositionOuterClass.DeviceInfoList.Builder builder = UserPositionOuterClass.DeviceInfoList.newBuilder();
			Stream.of(infos).forEach(info -> builder.addDeviceInfo(userPosition(info)));
			UserPositionOuterClass.DeviceInfoList deviceInfoList = builder.build();
			UserPositionOuterClass.UserPositionList userPositionList = grpcClient.userPosition(deviceInfoList);
			return userPositionList.getPositionList().parallelStream()
				.map(position -> new MapPosition(String.valueOf(position.getPositionX()), String.valueOf(position.getPositionY()),String.valueOf(position.getPositionZ())))
				.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取定位数据失败",e);
			return ImmutableList.of(MapPosition.builder().positionX("1.0").positionY("1.0").positionZ("1.0").build());
		}
	}



	public List<MapPosition> findDirection(MapPosition startPosition,MapPosition endPosition){


		try {
		UserPositionOuterClass.Direction.Builder builder = UserPositionOuterClass.Direction.newBuilder();

		UserPositionOuterClass.UserPosition.Builder positionBuilder=UserPositionOuterClass.UserPosition.newBuilder();
		positionBuilder.setPositionX(startPosition.getPositionX());
		positionBuilder.setPositionY(startPosition.getPositionY());
		positionBuilder.setPositionZ(startPosition.getPositionZ());
		UserPositionOuterClass.UserPosition start= positionBuilder.build();

		positionBuilder.setPositionX(endPosition.getPositionX());
		positionBuilder.setPositionY(endPosition.getPositionY());
		positionBuilder.setPositionZ(endPosition.getPositionZ());
		UserPositionOuterClass.UserPosition end= positionBuilder.build();

		builder.setStartPosition(start);
		builder.setEndPosition(end);

		log.info("start:{},\nend:{}",start,end);

		UserPositionOuterClass.UserPositionList pointList=grpcClient.userDirection(builder.build());

		log.info("start:{},\nend:{},\nresult:{}",start,end,pointList);

		return pointList.getPositionList().stream()
				.map(position -> new MapPosition(String.valueOf(position.getPositionX()), String.valueOf(position.getPositionY()),String.valueOf(position.getPositionZ())))
				.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取导航失败",e);
			return ImmutableList.of(MapPosition.builder().positionX("0.0").positionY("0.0").positionZ("0.0").build());
		}
	}






	/**
	 * string Major=1;
	 * string Minor=2;
	 * string rssi=3;
	 *
	 * @param deviceInfo
	 * @return
	 */
	public UserPositionOuterClass.DeviceInfo userPosition(DeviceInfo deviceInfo) {
		UserPositionOuterClass.DeviceInfo.Builder builder = UserPositionOuterClass.DeviceInfo.newBuilder();
		builder.setMajor(deviceInfo.getMajor());
		builder.setMinor(deviceInfo.getMinor());
		builder.setRssi(deviceInfo.getRssi());
		return builder.build();
	}


}
