package com.briolink.locationservice.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CityDto(
    @JsonProperty
    var id: Int? = null,

    @JsonProperty
    var name: String,

    @JsonProperty
    var countryCode: String,

    @JsonProperty
    var countryId: Int,

    @JsonProperty
    var stateId: Int,

    @JsonProperty
    var latitude: Double? = null,

    @JsonProperty
    var longitude: Double? = null,

    @JsonProperty
    var wikiDataId: String? = null,
) {

}
