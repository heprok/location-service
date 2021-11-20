package com.briolink.locationservice.init

import com.briolink.locationservice.service.LocationService
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
class InitRunner(
    private val locationService: LocationService
) : CommandLineRunner {
    @Throws(Exception::class)
    override fun run(vararg args: String?) {
        if(locationService.md5Check())
            locationService.refresh()
        else {
            println(5555)
        }
    }

}
