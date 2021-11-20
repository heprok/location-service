package com.briolink.locationservice.controller

import com.briolink.locationservice.dto.AutocompleteDto
import com.briolink.locationservice.dto.LocationInfoDto
import com.briolink.locationservice.jpa.enumartion.TypeLocationEnum
import com.briolink.locationservice.service.LocationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class LocationController(
    private val locationService: LocationService,
) {
    @GetMapping("/locations")
    fun locations(@RequestParam(value = "query", required = true) query: String): List<AutocompleteDto> {
        return locationService.getListForAutocomplete(query)
    }

    @GetMapping("/location")
    fun location(@RequestParam(value = "type", required = true) type: TypeLocationEnum, @RequestParam(value = "id", required = true) id: Int): LocationInfoDto? {
        return locationService.getLocationInfo(id = id, type = type)
    }

}
