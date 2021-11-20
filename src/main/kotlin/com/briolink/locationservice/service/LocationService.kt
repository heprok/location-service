package com.briolink.locationservice.service

import com.briolink.locationservice.config.CsvFilesLocation
import com.briolink.locationservice.dto.AutocompleteDto
import com.briolink.locationservice.dto.LocationInfoDto
import com.briolink.locationservice.jpa.entity.City
import com.briolink.locationservice.jpa.entity.Country
import com.briolink.locationservice.jpa.entity.Info
import com.briolink.locationservice.jpa.entity.State
import com.briolink.locationservice.jpa.repository.CityRepository
import com.briolink.locationservice.jpa.repository.CountryRepository
import com.briolink.locationservice.jpa.repository.InfoRepository
import com.briolink.locationservice.jpa.repository.LocationRepository
import com.briolink.locationservice.jpa.repository.StateRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.DigestUtils
import java.io.BufferedInputStream
import java.util.*

@Service
@Transactional
class LocationService(
    private val cityRepository: CityRepository,
    private val locationRepository: LocationRepository,
    private val stateRepository: StateRepository,
    private val countryRepository: CountryRepository,
    private val csvService: CsvService,
    private val csvFilesLocation: CsvFilesLocation,
    private val infoRepository: InfoRepository
) {

    fun md5Check(): Boolean {
//        val md5Country = BufferedInputStream(csvFilesLocation.country.openStream()).let { DigestUtils.md5Digest(it) }
//        val md5State = BufferedInputStream(csvFilesLocation.state.openStream()).let { DigestUtils.md5Digest(it) }
        val md5City = BufferedInputStream(csvFilesLocation.city.openStream()).let { DigestUtils.md5Digest(it) }

        return infoRepository.findByIdOrNull(UUID.nameUUIDFromBytes(md5City)) == null
    }

    fun refresh() {
        println("refresh database")
        val mapCountry: MutableMap<Int, Country> = mutableMapOf()
        val mapState: MutableMap<Int, State> = mutableMapOf()
        val streamBufferCountry = BufferedInputStream(csvFilesLocation.country.openStream())
        val streamBufferState = BufferedInputStream(csvFilesLocation.state.openStream())
        val streamBufferCity = BufferedInputStream(csvFilesLocation.city.openStream())
        println("Download country...")
        csvService.upload<Country>(streamBufferCountry)?.forEach {
            mapCountry[it.id!!] = countryRepository.save(it)
        }
        println("Download state...")
        csvService.upload<State>(streamBufferState)?.forEach {
            it.country = mapCountry[it.countryIdImpl]!!
            mapState[it.id!!] = stateRepository.save(it)
        }
        println("Download city...")

        csvService.upload<City>(streamBufferCity)?.forEach {
            it.country = mapCountry[it.countryIdImpl]!!
            it.state = mapState[it.stateIdImpl]!!
            cityRepository.save(it)
        }
        val md5Country = UUID.nameUUIDFromBytes(DigestUtils.md5Digest(streamBufferCountry))
        println("md5Country")
        println(md5Country.toString())
        val md5State = UUID.nameUUIDFromBytes(DigestUtils.md5Digest(streamBufferState))
        println("md5State")
        println(md5State.toString())
        val md5City = UUID.nameUUIDFromBytes(DigestUtils.md5Digest(streamBufferCity))
        println("md5City")
        println(md5City.toString())
        Info().apply {
            this.md5City = md5City
            this.md5State = md5State
            this.md5Country = md5Country
            infoRepository.save(this)
        }
        locationRepository.refreshLocation()
    }

    fun getListForAutocomplete(query: String): List<AutocompleteDto> {
        return locationRepository.findByQuery(query).let {
            val firstType = it.firstOrNull()?.type
            it.filter { location -> location.type == firstType }
        }.map {
            val id: String = it.type + ";" + it.id.toString()
            val name =
                    if (it.cityName != null) "${it.cityName}, ${it.stateName}, ${it.countryName}"
                    else if (it.stateName != null) "${it.stateName}, ${it.countryName}"
                    else it.countryName
            AutocompleteDto(id = id, name = name)
        }
    }

    fun getLocationInfo(id: Int, type: String): LocationInfoDto? {
        return when (type) {
            "city" -> {
                cityRepository.findByIdOrNull(id)?.let {
                    LocationInfoDto(
                            city = it.toDto(),
                            state = it.state.toDto(),
                            country = it.country.toDto(),
                            location = it.name + ", " + it.state.name + ", " + it.country.name,
                    )
                }
            }
            "state" -> {
                stateRepository.findByIdOrNull(id)?.let {
                    LocationInfoDto(
                            city = null,
                            state = it.toDto(),
                            country = it.country.toDto(),
                            location = it.name + ", " + it.country.name,
                    )
                }
            }
            "country" -> {
                countryRepository.findByIdOrNull(id)?.let {
                    LocationInfoDto(
                            city = null,
                            state = null,
                            country = it.toDto(),
                            location = it.name,
                    )
                }
            }

            else -> null
        }
    }
}
