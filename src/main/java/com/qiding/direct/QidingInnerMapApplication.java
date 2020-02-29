package com.qiding.direct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class QidingInnerMapApplication {

	public static void main(String[] args) {
		SpringApplication application=new SpringApplication();
		application.addListeners(new ApplicationPidFileWriter("qiding-inner-map.pid"));
		application.run(QidingInnerMapApplication.class, args);
	}

}
