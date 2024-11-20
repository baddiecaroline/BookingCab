package com.example.unit3_bookingcab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Unit3BookingCabApplication {

	public static void main(String[] args) {
		SpringApplication.run(Unit3BookingCabApplication.class, args);
	}

}
