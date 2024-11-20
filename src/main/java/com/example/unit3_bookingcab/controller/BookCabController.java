package com.example.unit3_bookingcab.controller;

import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.unit3_bookingcab.config.FeignService;
import com.example.unit3_bookingcab.model.Cab;
import com.example.unit3_bookingcab.service.CabService;

@RestController
@Configuration
@RequestMapping("/bookcab")
public class BookCabController {

	// random test
	@Value("${app.bookcabwelcome}")
	private String bookCabWelcome;

	// (autowired fields) // @Autowired //inject bean from BeanDefinitions
	@Autowired
	private CabService cabService;
	@Autowired
	private FeignService feignService;

	// handler methods aka methods that return a response to the user

	@PostMapping("/addbooking") // RequestBody takes cab info from form as JSON and transforms into Cab object
	public ResponseEntity<Cab> addBookedCab(@RequestBody Cab cab) {

		ResponseEntity<Double> calculateFareResponse = feignService.calculateFare(cab.getFromLocation(),
				cab.getToLocation(), cab.getTypeOfCab());
		double calculatedFare = calculateFareResponse.getBody();
		cab.setRate(calculatedFare);

		// create + send cab object to DB
		Cab bookedCab = cabService.bookCab(cab);

		// option 1 - redirect to new page -- serving web content
		// option 2 - send JSON back ot front end, which can do whatever you want with
		// random test
		// System.out.println("Testing universal application.properties: "+
		// testGreeting);
		// print bookedCab on controller
		return ResponseEntity.status(HttpStatus.CREATED).body(bookedCab);
	}

	// GET request to get a specific booking by ID
	@GetMapping("/booking/{id}")
	public ResponseEntity<Cab> getBookedCabById(@PathVariable Integer id) {
		Optional<Cab> cab = cabService.getCabById(id);
		return cab.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
	}

	// GET request to retrieve all bookings
	@GetMapping("/bookings")
	public ResponseEntity<List<Cab>> getAllBookedCabs() {
		List<Cab> cabs = cabService.getAllBookedCabs();
		return ResponseEntity.ok(cabs);
	}

	// PUT request to update a booking
@PutMapping("/booking/{id}")
public ResponseEntity<Cab> updateBookedCab(@PathVariable Integer id, @RequestBody Cab updatedCab) {
    // Retrieve the existing booking by ID
    Optional<Cab> existingCab = cabService.getCabById(id);
    
    // Check if the booking exists
    if (existingCab.isPresent()) {
        Cab currentCab = existingCab.get();
        
        // Only update fields that are provided (non-null)
        if (updatedCab.getFromLocation() != null) {
            currentCab.setFromLocation(updatedCab.getFromLocation());
        }
        if (updatedCab.getToLocation() != null) {
            currentCab.setToLocation(updatedCab.getToLocation());
        }
        if (updatedCab.getTypeOfCab() != null) {
            currentCab.setTypeOfCab(updatedCab.getTypeOfCab());
        }

        // Use FeignService to call the fare calculation microservice
        ResponseEntity<Double> newFareResponse = feignService.calculateFare(
            currentCab.getFromLocation(), 
            currentCab.getToLocation(), 
            currentCab.getTypeOfCab()
        );

        // Extract the fare value from the ResponseEntity
        Double newFare = newFareResponse.getBody();

        // Set the recalculated fare
        currentCab.setRate(newFare);

        // Save the updated booking back to the database
        Cab savedCab = cabService.bookCab(currentCab);
        
        // Return the updated booking in the response
        return ResponseEntity.ok(savedCab);
    } else {
        // If the booking doesn't exist, return a 404 response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}


	// DELETE request to delete a booking by ID
	@DeleteMapping("/booking/{id}")
	public ResponseEntity<Void> deleteBookedCab(@PathVariable Integer id) {
		Optional<Cab> cab = cabService.getCabById(id);
		if (cab.isPresent()) {
			cabService.deleteCab(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}
