package com.briolink.locationservice.jpa.entity

import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "location")
@Entity
class Location {
    @EmbeddedId
    var id: LocationId? = null

    @Column(name = "country_name")
    var countryName: String? = null

    @Column(name = "state_name")
    var stateName: String? = null

    @Column(name = "city_name")
    var cityName: String? = null

    @Column(name = "state_code")
    var stateCode: String? = null

    @Column(name = "country_code_iso2")
    var countryCodeIso2: String? = null

    @Column(name = "country_code_iso3")
    var countryCodeIso3: String? = null
}
