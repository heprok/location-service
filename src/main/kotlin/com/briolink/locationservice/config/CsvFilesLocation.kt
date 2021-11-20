package com.briolink.locationservice.config

import com.sun.istack.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.net.URL

@Configuration
@ConfigurationProperties(prefix = "app.csv-files-location")
class CsvFilesLocation {
    @NotNull
    lateinit var country: URL
    @NotNull
    lateinit var state: URL
    @NotNull
    lateinit var city: URL
}
