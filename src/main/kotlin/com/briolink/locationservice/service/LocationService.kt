package com.briolink.locationservice.service

import com.briolink.location.enumeration.TypeLocationEnum
import com.briolink.location.model.LocationId
import com.briolink.location.model.LocationInfo
import com.briolink.location.model.LocationSuggestion
import com.briolink.locationservice.config.CsvFilesLocation
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
import java.io.BufferedInputStream
import java.util.UUID

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
        val md5City =
            BufferedInputStream(csvFilesLocation.city.openStream()).let { UUID.nameUUIDFromBytes(it.readAllBytes()) }
        return infoRepository.findByIdOrNull(md5City) == null
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
        val md5Country = UUID.nameUUIDFromBytes(streamBufferCountry.readAllBytes())
        println("md5Country")
        println(md5Country.toString())
        val md5State = UUID.nameUUIDFromBytes(streamBufferState.readAllBytes())
        println("md5State")
        println(md5State.toString())
        val md5City = UUID.nameUUIDFromBytes(streamBufferCity.readAllBytes())
        println("md5City")
        println(md5City.toString())
        streamBufferCountry.close()
        streamBufferState.close()
        streamBufferCity.close()
        Info().apply {
            this.id = md5City
            this.md5State = md5State
            this.md5Country = md5Country
            infoRepository.save(this)
        }
        locationRepository.deleteLocations()
        locationRepository.insertLocation()
    }

    fun getListLocationSuggestion(query: String?): List<LocationSuggestion> =
        locationRepository.findByQueryAndType(query).let {
            println(query)
            val firstType = it.firstOrNull()?.type
            it.filter { location -> location.type == firstType }
        }.map {
            val locationId = LocationId(type = TypeLocationEnum.valueOf(it.type), id = it.id.toInt())
            val name =
                if (it.cityName != null) "${it.cityName}, ${it.stateName}, ${it.countryName}"
                else if (it.stateName != null) "${it.stateName}, ${it.countryName}"
                else it.countryName
            LocationSuggestion(locationId = locationId, name = name)
        }

    fun getLocationInfo(id: Int, type: TypeLocationEnum): LocationInfo? {
        return when (type) {
            TypeLocationEnum.City -> {
                cityRepository.findByIdOrNull(id)?.let {
                    LocationInfo(
                        city = it.toModel(),
                        state = it.state.toModel(),
                        country = it.country.toModel(),
                        location = it.name + ", " + it.state.name + ", " + it.country.name,
                    )
                }
            }
            TypeLocationEnum.State -> {
                stateRepository.findByIdOrNull(id)?.let {
                    LocationInfo(
                        city = null,
                        state = it.toModel(),
                        country = it.country.toModel(),
                        location = it.name + ", " + it.country.name,
                    )
                }
            }
            TypeLocationEnum.Country -> {
                countryRepository.findByIdOrNull(id)?.let {
                    LocationInfo(
                        city = null,
                        state = null,
                        country = it.toModel(),
                        location = it.name,
                    )
                }
            }
        }
    }
}
