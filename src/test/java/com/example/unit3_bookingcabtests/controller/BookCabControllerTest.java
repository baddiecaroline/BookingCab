package com.example.unit3_bookingcabtests.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.unit3_bookingcab.config.FeignService;
import com.example.unit3_bookingcab.controller.BookCabController;
import com.example.unit3_bookingcab.model.Cab;
import com.example.unit3_bookingcab.service.CabService;

@SpringBootTest
public class BookCabControllerTest {

    @Mock
    private CabService cabService;

    @Mock
    private FeignService feignService;

    @InjectMocks
    private BookCabController bookCabController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        MockMvcBuilders.standaloneSetup(bookCabController).build();
    }

    @Test
    public void testAddBookedCab() throws Exception {
        // Given
        Cab cab = new Cab();
        cab.setFromLocation("2901 S Sepulveda Blvd, Los Angeles, California 90064");
        cab.setToLocation("1223 S Bundy Dr, Los Angeles, California 90025");
        cab.setTypeOfCab("Luxury SUV");

        // Mock the FeignService response
        double expectedRate = 12.196306235189214;
        when(feignService.calculateFare(cab.getFromLocation(), cab.getToLocation(), cab.getTypeOfCab()))
                .thenReturn(ResponseEntity.ok(expectedRate));

        // Mock the CabService response
        when(cabService.bookCab(cab)).thenReturn(cab);

        // When
        ResponseEntity<Cab> response = bookCabController.addBookedCab(cab);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedRate, response.getBody().getRate(), 0.0001);
    }

    @Test
    public void testGetBookedCabById() throws Exception {
        // Given
        int cabId = 255;
        Cab cab = new Cab();
        cab.setCabId(cabId);
        when(cabService.getCabById(cabId)).thenReturn(Optional.of(cab));

        // When
        ResponseEntity<Cab> response = bookCabController.getBookedCabById(cabId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cabId, response.getBody().getCabId());
    }

    @Test
    public void testGetAllBookedCabs() throws Exception {
        // Given
        Cab cab1 = new Cab();
        Cab cab2 = new Cab();
        List<Cab> cabs = Arrays.asList(cab1, cab2);
        when(cabService.getAllBookedCabs()).thenReturn(cabs);

        // When
        ResponseEntity<List<Cab>> response = bookCabController.getAllBookedCabs();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testUpdateBookedCab() throws Exception {
        // Given
        int cabId = 255;
        Cab existingCab = new Cab();
        existingCab.setCabId(cabId);
        existingCab.setFromLocation("2901 S Sepulveda Blvd, Los Angeles, California 90064");
        existingCab.setToLocation("1223 S Bundy Dr, Los Angeles, California 90025");
        existingCab.setTypeOfCab("Luxury SUV");

        Cab updatedCab = new Cab();
        updatedCab.setTypeOfCab("SUV");

        // Mock the existing cab response
        when(cabService.getCabById(cabId)).thenReturn(Optional.of(existingCab));

        // Mock the FeignService response for new fare calculation
        double newFare = 10.0;
        when(feignService.calculateFare(existingCab.getFromLocation(), existingCab.getToLocation(), updatedCab.getTypeOfCab()))
                .thenReturn(ResponseEntity.ok(newFare));

        // Mock the save operation
        when(cabService.bookCab(existingCab)).thenReturn(existingCab);

        // When
        ResponseEntity<Cab> response = bookCabController.updateBookedCab(cabId, updatedCab);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newFare, response.getBody().getRate());
    }

    @Test
    public void testDeleteBookedCab() throws Exception {
        // Given
        int cabId = 255;
        when(cabService.getCabById(cabId)).thenReturn(Optional.of(new Cab()));

        // When
        ResponseEntity<Void> response = bookCabController.deleteBookedCab(cabId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cabService, times(1)).deleteCab(cabId);
    }
}
