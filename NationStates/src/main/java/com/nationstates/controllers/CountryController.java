package com.nationstates.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nationstates.entities.Country;
import com.nationstates.repositories.CityRepository;
import com.nationstates.repositories.CountryRepository;

@RestController
public class CountryController {

	@Autowired
	private CountryRepository countryRepository;
	@Autowired
	private CityRepository cityRepository;

	@PreAuthorize("hasRole('EDITOR')")
	@PostMapping("/country/save")
	public ResponseEntity<?> saveCountry(@RequestBody Country country) {
		Country savedCountry = countryRepository.save(country);
		if (country.getCities().size() > 0) {
			savedCountry.addCities(country.getCities());
			country.getCities().forEach(obj -> cityRepository.save(obj));
		}
		return new ResponseEntity<>(savedCountry, HttpStatus.CREATED);
	}
	
	@GetMapping("/countries")
	public ResponseEntity<?> getAllCountries() {
		return new ResponseEntity<>(countryRepository.findAll(), HttpStatus.CREATED);
	}

}
