package com.example.unit3_bookingcab.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@JsonIgnoreProperties
@Entity //will make it a table
public class Cab {

	
	@JsonProperty("cabId")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer cabId; //make this autoincremented primary key
	@JsonProperty("fromLocation") //reinforces lowercase see below
	private String FromLocation;
	@JsonProperty("toLocation")
	private String ToLocation;
	@JsonProperty("typeOfCab")
	private String TypeOfCab; //do this as a dropdown
	
	//calculated fare - comes from calculate fare microservice
	private Double rate;

	public Cab() {
		super();
	}

	public Cab(Integer cabId, String fromLocation, String toLocation, String typeOfCab, Double rate) {
		super();
		this.cabId = cabId;
		FromLocation = fromLocation;
		ToLocation = toLocation;
		TypeOfCab = typeOfCab;
		this.rate = rate;
	}

	public Integer getCabId() {
		return cabId;
	}

	public void setCabId(Integer cabId) {
		this.cabId = cabId;
	}

	public String getFromLocation() {
		return FromLocation;
	}

	public void setFromLocation(String fromLocation) {
		FromLocation = fromLocation;
	}

	public String getToLocation() {
		return ToLocation;
	}

	public void setToLocation(String toLocation) {
		ToLocation = toLocation;
	}

	public String getTypeOfCab() {
		return TypeOfCab;
	}

	public void setTypeOfCab(String typeOfCab) {
		TypeOfCab = typeOfCab;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "Cab [cabId=" + cabId + ", FromLocation=" + FromLocation + ", ToLocation=" + ToLocation + ", TypeOfCab="
				+ TypeOfCab + ", rate=" + rate + "]";
	}
	

}
