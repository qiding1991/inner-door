package com.qiding.direct.map.service;

import com.google.common.collect.ImmutableList;
import com.qiding.direct.UserPositionOuterClass;
import com.qiding.direct.UserPositionServiceGrpc;
import com.qiding.direct.map.config.PositionConfig;
import com.qiding.direct.map.param.DeviceInfo;
import com.qiding.direct.map.param.MapPosition;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
			UserPositionOuterClass.DeviceInfoList.Builder builder = UserPositionOuterClass.DeviceInfoList.newBuilder();
			Stream.of(infos).forEach(info -> builder.addDeviceInfo(userPosition(info)));
			UserPositionOuterClass.DeviceInfoList deviceInfoList = builder.build();
			UserPositionOuterClass.UserPositionList userPositionList = grpcClient.userPosition(deviceInfoList);
			return userPositionList.getPostionList().parallelStream()
				.map(position -> new MapPosition(String.valueOf(position.getPositionX()), String.valueOf(position.getPositionY()),String.valueOf(position.getPositionZ())))
				.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			return ImmutableList.of(MapPosition.builder().postionX("1.0f").postionY("1.0f").postionZ("1.0f").build());
		}
	}

	/**
	 * string version=1;
	 * string UUID=2;
	 * string Major=3;
	 * string Minor=4;
	 * string rssi=5;
	 * string name=6;
	 * string address=7;
	 * string txpower=8;
	 *
	 * @param deviceInfo
	 * @return
	 */
	public UserPositionOuterClass.DeviceInfo userPosition(DeviceInfo deviceInfo) {
		UserPositionOuterClass.DeviceInfo.Builder builder = UserPositionOuterClass.DeviceInfo.newBuilder();
		builder.setTime(deviceInfo.getTime());
		builder.setUUID(deviceInfo.getUuid());
		builder.setMajor(deviceInfo.getMajor());
		builder.setMinor(deviceInfo.getMinor());
		builder.setRssi(deviceInfo.getRssi());
		builder.setName(deviceInfo.getName());
		builder.setAddress(deviceInfo.getAddress());
		builder.setTxpower(deviceInfo.getTxpower());
		return builder.build();
	}


}
