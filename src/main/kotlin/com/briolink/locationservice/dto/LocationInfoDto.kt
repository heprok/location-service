package com.briolink.locationservice.dto

import com.briolink.lib.location.model.City
import com.briolink.lib.location.model.Country
import com.briolink.lib.location.model.State
import com.fasterxml.jackson.annotation.JsonProperty

@Deprecated("Remove after update")
data class LocationInfoDto(
    @JsonProperty
    val country: Country,
    @JsonProperty
    val state: State? = null,
    @JsonProperty
    val city: City? = null,
    @JsonProperty
    val location: String
)
