package com.briolink.locationservice.jpa.entity

import com.briolink.lib.location.model.Country
import com.opencsv.bean.CsvBindByName
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table

@Table(name = "countries")
@Entity
class Country {
    @Id
    @Column(name = "id", nullable = false)
    @CsvBindByName
    var id: Int? = null

    @Column(name = "name", nullable = false, length = 100)
    @CsvBindByName
    lateinit var name: String

    @Column(name = "type", length = 191, nullable = false)
    var type: String = "Country"

    @Column(name = "iso3", length = 10)
    @CsvBindByName
    var iso3: String? = null

    @Column(name = "iso2", length = 10)
    @CsvBindByName
    var iso2: String? = null

    @Column(name = "numeric_code", length = 10)
    @CsvBindByName(column = "numeric_code")
    var numericCode: String? = null

    @Column(name = "phone_code")
    @CsvBindByName(column = "phone_code")
    var phoneCode: String? = null

    @Column(name = "capital")
    @CsvBindByName
    var capital: String? = null

    @Column(name = "currency")
    @CsvBindByName
    var currency: String? = null

    @Column(name = "currency_symbol")
    @CsvBindByName(column = "currency_symbol")
    var currencySymbol: String? = null

    @Column(name = "tld")
    @CsvBindByName
    var tld: String? = null

    @Column(name = "native")
    @CsvBindByName(column = "native")
    var _native: String? = null

    @Column(name = "region")
    @CsvBindByName
    var region: String? = null

    @Column(name = "subregion")
    @CsvBindByName
    var subregion: String? = null

    @Lob
    @Column(name = "timezones")
    @CsvBindByName
    var timezones: String? = null

    @Column(name = "latitude")
    @CsvBindByName
    var latitude: Double? = null

    @Column(name = "longitude")
    @CsvBindByName
    var longitude: Double? = null

    @Column(name = "emoji", length = 191)
    @CsvBindByName
    var emoji: String? = null

    @Column(name = "emoji_u", length = 191)
    @CsvBindByName(column = "emojiU")
    var emojiU: String? = null

    fun toModel() = Country(
        id = id!!,
        name = name,
        iso3 = iso3,
        iso2 = iso2,
        numericCode = numericCode,
        phoneCode = phoneCode,
        capital = capital,
        currency = currency,
        currencySymbol = currencySymbol,
        tld = tld,
        native = _native,
        region = region,
        subregion = subregion,
        timezones = timezones,
        latitude = latitude,
        longitude = longitude,
        emoji = emoji,
        emojiU = emojiU,
    )
}
