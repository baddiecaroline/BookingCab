package com.example.unit3_bookingcab.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="UNIT3TEMPLATECALCULATEFARE", url="http://localhost:8084")//name of microservice
public interface FeignService {
	
    @GetMapping("/calculatefare") //with this you can now use calculatefare across applications
    ResponseEntity<Double> calculateFare(@RequestParam String fromLocation, @RequestParam String toLocation, @RequestParam String typeOfCab);


}
