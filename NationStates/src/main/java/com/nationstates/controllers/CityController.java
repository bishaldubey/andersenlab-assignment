package com.nationstates.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nationstates.entities.City;
import com.nationstates.entities.Country;
import com.nationstates.repositories.CityRepository;
import com.nationstates.repositories.CountryRepository;

@RestController
@RequestMapping("/cities")
public class CityController {
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private CountryRepository countryRepository;

	@GetMapping
	public ResponseEntity<List<City>> getCities(@RequestParam(defaultValue = "0", required = false) int page,
			@RequestParam(defaultValue = "5", required = false) int size) {
		PageRequest paging = PageRequest.of(page, size);
		Page<City> cityPage = cityRepository.findAll(paging);

		return new ResponseEntity<>(cityPage.getContent(), HttpStatus.OK);
	}

	@GetMapping("/unique")
	public ResponseEntity<List<City>> getUniqueCityNames() {
		List<City> cityNames = cityRepository.fetchDistinctCities();
		return new ResponseEntity<>(cityNames, HttpStatus.OK);
	}

	@GetMapping("/country/{countryName}")
	public ResponseEntity<List<City>> getCitiesByCountryName(@PathVariable String countryName) {
		List<City> cities = cityRepository.findByCountryName(countryName);
		return new ResponseEntity<>(cities, HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<List<City>> searchCitiesByName(@RequestParam(required = true) String cityName) {
		List<City> cities = cityRepository.findByNameContainingIgnoreCase(cityName);
		return new ResponseEntity<>(cities, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('EDITOR')")
	@PutMapping("/{id}")
	public ResponseEntity<City> editCity(@PathVariable Long id, @RequestParam(required = false) String name,
			@RequestParam(required = false) String logo) {
		Optional<City> optionalCity = cityRepository.findById(id);

		if (optionalCity.isPresent()) {
			City city = optionalCity.get();
			if (name != null) {
				city.setName(name);
			}
			if (logo != null) {
				city.setLogo(logo);
			}

			cityRepository.save(city);
			return new ResponseEntity<>(city, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PreAuthorize("hasRole('EDITOR')")
	@PostMapping("/save")
	public ResponseEntity<Country> searchCitiesByName(@RequestParam(required = true) Long countryId, @RequestBody List<City> cities) {
		Optional<Country> optionalCountry = countryRepository.findById(countryId);
		if (optionalCountry.isPresent()) {
			for(City city : cities) {
				city.setCountry(optionalCountry.get());
				cityRepository.save(city);
			}
			return new ResponseEntity<>(optionalCountry.get(), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
