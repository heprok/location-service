package com.briolink.locationservice.jpa.repository;

import com.briolink.locationservice.jpa.entity.City
import org.springframework.data.jpa.repository.JpaRepository

interface CityRepository : JpaRepository<City, Int> {
}
