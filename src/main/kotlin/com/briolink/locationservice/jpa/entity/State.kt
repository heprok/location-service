package com.briolink.locationservice.jpa.entity

import com.briolink.locationservice.dto.StateDto
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

@Table(name = "states")
@Entity
class State {
    @Id
    @Column(name = "id", nullable = false)
    @CsvBindByName
    var id: Int? = null

    @Column(name = "name", nullable = false)
    @CsvBindByName
    lateinit var name: String

    @Column(name = "type", length = 191, nullable = false)
    var type: String = "state"

    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    lateinit var country: Country

    @CsvBindByName(column = "country_id")
    @Transient
    var countryIdImpl: Int = 0

    @Column(name = "country_code", nullable = false)
    @CsvBindByName(column = "country_code")
    lateinit var countryCode: String

    @Column(name = "state_code", nullable = false)
    @CsvBindByName(column = "state_code")
    lateinit var stateCode: String

    @Column(name = "subtype", length = 191)
    @CsvBindByName(column = "type")
    var subtype: String? = null

    @Column(name = "latitude")
    @CsvBindByName
    var latitude: Double? = null

    @Column(name = "longitude")
    @CsvBindByName
    var longitude: Double? = null


    fun toDto() : StateDto = StateDto(
            id = id,
            name = name,
            countryId = country.id!!,
            countryCode = countryCode,
            stateCode = stateCode,
            subtype = subtype,
            latitude = latitude,
            longitude = longitude
    )
}
