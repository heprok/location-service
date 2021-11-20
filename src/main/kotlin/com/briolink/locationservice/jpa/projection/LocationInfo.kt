package com.briolink.locationservice.jpa.projection

interface LocationInfo {
    val cityName: String?
    val countryName: String
    val stateName: String?
    val id: Long
    val type: String
}
