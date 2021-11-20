package com.briolink.locationservice.controller

import com.briolink.locationservice.dto.AutocompleteDto
import com.briolink.locationservice.dto.LocationInfoDto
import com.briolink.locationservice.jpa.entity.City
import com.briolink.locationservice.jpa.entity.Country
import com.briolink.locationservice.jpa.entity.State
import com.briolink.locationservice.jpa.repository.CityRepository
import com.briolink.locationservice.jpa.repository.CountryRepository
import com.briolink.locationservice.jpa.repository.LocationRepository
import com.briolink.locationservice.jpa.repository.StateRepository
import com.briolink.locationservice.service.CsvService
import com.briolink.locationservice.service.LocationService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URL


@RestController
@Transactional
class LocationController(
    private val countryRepository: CountryRepository,
    private val cityRepository: CityRepository,
    private val stateRepository: StateRepository,
    private val locationService: LocationService,
    private val csvService: CsvService,
    private val mapper: ObjectMapper,
) {
    @GetMapping("/autocomplete")
    fun autocomplete(@RequestParam(value = "query", required = true) query: String): List<AutocompleteDto> {
        return locationService.getListForAutocomplete(query)
    }

    @GetMapping("/location")
    fun location(@RequestParam(value = "type", required = true) type: String, @RequestParam(value = "id") id: Int): LocationInfoDto? {
        return locationService.getLocationInfo(id = id, type = type)
    }

}
