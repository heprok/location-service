package com.briolink.locationservice

import com.briolink.locationservice.service.LocationService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class LocationServiceApplication(
    locationService: LocationService
) {
    init {
        // locationService.refreshDatabase()
    }
}

fun main(args: Array<String>) {
    runApplication<LocationServiceApplication>(*args)
}
