package com.example.youcandoit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 자정마다 데이터베이스를 업데이트하기 위해 사용
@SpringBootApplication
@EnableAsync // 비동기 사용을 위해 선언
public class YoucandoitApplication {

	public static void main(String[] args) {
		SpringApplication.run(YoucandoitApplication.class, args);
	}

}
