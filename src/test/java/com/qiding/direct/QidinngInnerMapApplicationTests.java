package com.qiding.direct;

import com.qiding.direct.map.param.Geo;
import com.qiding.direct.map.param.GeoLine;
import com.qiding.direct.map.param.GeoPoint;
import com.qiding.direct.map.param.GeoPolygon;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.security.MD5Encoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.text.DecimalFormat;
import java.util.*;

@Log4j2
@SpringBootTest
class QidinngInnerMapApplicationTests {

	@Test
	void contextLoads() {
	}


	public static void main(String[] args) {
		testSrc();
	}

	@Test
	public void testDouble(){
		Double xxx=11.1111d;
		DecimalFormat format=new DecimalFormat("0.00");
		xxx=Double.valueOf(format.format(xxx));
		System.out.println("1111");
	}

	@Test
	public void md5(){
		String aaa= DigestUtils.md5DigestAsHex("aaaaa".getBytes());
		System.out.println("111");


		Map<String,String> map=new HashMap<>();
		System.out.println(map.putIfAbsent("aa","bb"));
		System.out.println(map.putIfAbsent("aa","bb"));

		System.out.println("aaa");
	}


   @Test
   public void testOption(){
		String a=null;
		Optional.ofNullable(a).orElse("1111");

		String b="111";



   }


	@Test
	public void testGeoChange(){

		List<Double> coordinates= Arrays.asList(5.0696941592476004E8,5.0696941592476004E8,5.0696941592476004E8);

		//单点
		GeoPoint.Geometry geometry=new GeoPoint.Geometry();
		geometry.setCoordinates(coordinates);
		GeoPoint point = new GeoPoint();
		point.setGeometry(geometry);
		point.changePrecision();

		log.info("point:{}",point);

         //线
		GeoLine.Geometry geometry2=new GeoLine.Geometry();
		List<List<Double>> coordinate2=new ArrayList<>();
		coordinate2.add(coordinates);
		coordinate2.add(coordinates);
		geometry2.setCoordinates(coordinate2);
		GeoLine line=new GeoLine();
		line.setGeometry(geometry2);
		line.changePrecision();
		log.info("line:{}",line);
		//面
		GeoPolygon.GeometryBean geometry3=new GeoPolygon.GeometryBean();

		List<List<List<Double>>> coordinate3=new ArrayList<>();
		coordinate3.add(coordinate2);
		coordinate3.add(coordinate2);
		geometry3.setCoordinates(coordinate3);

		GeoPolygon polygon=new GeoPolygon();
		polygon.setGeometry(geometry3);
		polygon.changePrecision();
		log.info("polygon:{}",polygon);
	}





	@Test
	public static void testSrc(){
		Map<String,Integer> map=new HashMap<>();
		map.computeIfAbsent("111",(key)->getData());
		map.computeIfAbsent("111",(key)->getData());
		System.out.println("111");
	}

	public static int getData(){
		System.out.println(12232);
		return 1;
	}
}
