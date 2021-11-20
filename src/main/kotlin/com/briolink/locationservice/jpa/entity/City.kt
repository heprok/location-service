package com.briolink.locationservice.jpa.entity

import com.briolink.locationservice.dto.CityDto
import com.opencsv.bean.CsvBindByName
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Transient

@Table(name = "cities")
@Entity
class City {
    @Id
    @Column(name = "id", nullable = false)
    @CsvBindByName
    var id: Int? = null

    @Column(name = "name", nullable = false)
    @CsvBindByName
    lateinit var name: String

    @Column(name = "type", nullable = false)
    var type: String = "City"

    @ManyToOne(optional = false)
    @JoinColumn(name = "state_id", nullable = false)
    lateinit var state: State

    @CsvBindByName(column = "country_id")
    @Transient
    var countryIdImpl: Int = 0

    @CsvBindByName(column = "state_id")
    @Transient
    var stateIdImpl: Int = 0

    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    lateinit var country: Country

    @Column(name = "country_code", nullable = false, length = 2)
    @CsvBindByName(column = "country_code")
    lateinit var countryCode: String

    @Column(name = "latitude")
    @CsvBindByName
    var latitude: Double? = null

    @Column(name = "longitude")
    @CsvBindByName
    var longitude: Double? = null

    @Column(name = "wiki_data_id")
    @CsvBindByName(column = "wiki_data_id")
    var wikiDataId: String? = null

    fun toDto() : CityDto = CityDto(
            id = id,
            name = name,
            countryCode = countryCode,
            countryId = country.id!!,
            stateId = state.id!!,
            latitude = latitude,
            longitude = longitude,
            wikiDataId = wikiDataId
    )
}
