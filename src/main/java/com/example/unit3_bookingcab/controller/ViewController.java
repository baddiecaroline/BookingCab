package com.example.unit3_bookingcab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.unit3_bookingcab.config.FeignService;
import com.example.unit3_bookingcab.service.CabService;

@Controller
@RequestMapping("/view")
public class ViewController {

	
	@Autowired
	private CabService cabService;
	@Autowired
	private FeignService feignService;
	
	@PostMapping("/bookcab")
		public String bookCabReturn(@RequestParam(name="fromLocation", required=false) String fromLocation,
        @RequestParam(name = "toLocation", required = true) String toLocation,
        @RequestParam(name = "typeOfCab", required = true) String typeOfCab,
		Model model) {
		
		ResponseEntity<Double> calculateFareResponse = feignService.calculateFare(fromLocation,
				toLocation, typeOfCab);
		double calculatedFare = calculateFareResponse.getBody();
		System.out.println(calculatedFare);
		
		model.addAttribute("fare", calculatedFare);

    return "bookcab";
}

	// methods that return views aka html pages instead of data
	// create a method that returns the populated cab data in the form of my
	// existing html

//	@GetMapping
//	public String populatedHtml() {
//		return "bookcab.html";
//		
//	}

}
