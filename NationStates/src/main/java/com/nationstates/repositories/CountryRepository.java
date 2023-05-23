package com.nationstates.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nationstates.entities.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
	Country findByName(String countryName);
}
