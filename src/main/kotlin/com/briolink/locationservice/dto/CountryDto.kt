package com.briolink.locationservice.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.opencsv.bean.CsvBindByName
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.Lob

class CountryDto(
    @Id
    @JsonProperty
    var id: Int? = null,

    @JsonProperty
    var name: String,

    @JsonProperty
    var iso3: String? = null,

    @JsonProperty
    var iso2: String? = null,

    @JsonProperty
    var numericCode: String? = null,

    @JsonProperty
    var phoneCode: String? = null,

    @JsonProperty
    var capital: String? = null,

    @JsonProperty
    var currency: String? = null,

    @JsonProperty
    var currencySymbol: String? = null,

    @JsonProperty
    var tld: String? = null,

    @JsonProperty
    var native: String? = null,

    @JsonProperty
    var region: String? = null,

    @JsonProperty
    var subregion: String? = null,

    @JsonProperty
    var timezones: String? = null,

    @JsonProperty
    var latitude: Double? = null,

    @JsonProperty
    var longitude: Double? = null,

    @JsonProperty
    var emoji: String? = null,

    @JsonProperty
    var emojiU: String? = null,
)
