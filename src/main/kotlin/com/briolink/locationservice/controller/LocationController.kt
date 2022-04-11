package com.briolink.locationservice.controller

import com.briolink.lib.location.enumeration.TypeLocationEnum
import com.briolink.lib.location.model.LocationFullInfo
import com.briolink.lib.location.model.LocationSuggestion
import com.briolink.locationservice.dto.AutocompleteDto
import com.briolink.locationservice.dto.LocationInfoDto
import com.briolink.locationservice.service.LocationService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.jetbrains.annotations.NotNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(description = "Suggestion location and info about location")
@ApiResponses(
    ApiResponse(code = 200, message = "Location found"),
    ApiResponse(code = 204, message = "Location not found"),
)
@RequestMapping("/api/v1/locations")
class LocationController(
    private val locationService: LocationService,
) {
    @GetMapping("/suggestions")
    @ApiOperation("Get list suggestion by query")
    fun getLocationSuggestions(
        @ApiParam(value = "query", required = false) query: String? = null,
    ): List<LocationSuggestion> {
        return locationService.getListLocationSuggestion(query?.ifBlank { null })
    }

    @GetMapping("/info/{type}/{id}")
    @ApiOperation("Get full info about location by locationId")
    fun getLocationInfo(
        @NotNull @PathVariable(value = "type", required = true) type: TypeLocationEnum,
        @NotNull @PathVariable(value = "id", required = true) id: Int
    ): LocationFullInfo? {
        return locationService.getLocationInfo(id = id, type = type)
    }
}

@RestController
@Deprecated("Use LocationController")
class LocationControllerDeprecated(
    private val locationService: LocationService,
) {
    @GetMapping("/locations")
    fun locations(@RequestParam(value = "query", required = true) query: String): List<AutocompleteDto> {
        return locationService.getListLocationSuggestion(query).map { AutocompleteDto(it.locationId.toString(), it.name) }
    }

    @GetMapping("/location")
    fun location(
        @RequestParam(value = "type", required = true) type: TypeLocationEnum,
        @RequestParam(value = "id", required = true) id: Int
    ): LocationInfoDto? {
        return locationService.getLocationInfo(id = id, type = type)?.let {
            LocationInfoDto(
                country = it.country,
                state = it.state,
                city = it.city,
                location = it.toString()
            )
        }
    }
}
