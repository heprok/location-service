package com.briolink.locationservice.jpa.repository

import com.briolink.locationservice.jpa.entity.Country
import org.springframework.data.jpa.repository.JpaRepository

interface CountryRepository : JpaRepository<Country, Int>
