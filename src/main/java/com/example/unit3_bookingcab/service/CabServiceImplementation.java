package com.example.unit3_bookingcab.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.unit3_bookingcab.dao.CabRepository;
import com.example.unit3_bookingcab.model.Cab;

@Service
public class CabServiceImplementation implements CabService{

	@Autowired
	private CabRepository cabRepository; //import CabRepository
	
	
	//implemented methods from CabService
	
    //save a Cab + return their details
	@Override
	public Cab bookCab(Cab cab) {
		return cabRepository.save(cab);
	}
	
	//get a Cab based on id
	@Override
	public Optional<Cab> getCabById(Integer id) {
		return cabRepository.findById(id);
	}

	//return list of all Products
	@Override
	public List<Cab> getAllBookedCabs() {
		return new ArrayList<Cab>((Collection<? extends Cab>) cabRepository.findAll());
	}
	
	//delete a Product based on id
	@Override
	public void deleteCab(Integer id) {
		cabRepository.deleteById(id);

	}

	
}
