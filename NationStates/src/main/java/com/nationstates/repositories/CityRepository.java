package com.nationstates.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nationstates.entities.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
	@Query("select distinct c from City c")
	List<City> fetchDistinctCities();

	List<City> findByCountryName(String countryName);

	List<City> findByNameContainingIgnoreCase(String cityName);
}
