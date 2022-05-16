package com.briolink.locationservice.service

import com.briolink.lib.location.enumeration.TypeLocationEnum
import com.briolink.lib.location.model.LocationFullInfo
import com.briolink.lib.location.model.LocationId
import com.briolink.lib.location.model.LocationSuggestion
import com.briolink.locationservice.config.CsvFilesLocation
import com.briolink.locationservice.jpa.entity.City
import com.briolink.locationservice.jpa.entity.Country
import com.briolink.locationservice.jpa.entity.Info
import com.briolink.locationservice.jpa.entity.Location
import com.briolink.locationservice.jpa.entity.State
import com.briolink.locationservice.jpa.repository.CityRepository
import com.briolink.locationservice.jpa.repository.CountryRepository
import com.briolink.locationservice.jpa.repository.InfoRepository
import com.briolink.locationservice.jpa.repository.LocationRepository
import com.briolink.locationservice.jpa.repository.StateRepository
import com.briolink.locationservice.utils.HashUtils
import mu.KLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream

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
    companion object : KLogging()

    fun refreshDatabase() {
        logger.info { "Refreshing location database" }

        val mapCountry: MutableMap<Int, Country> = mutableMapOf()
        val mapState: MutableMap<Int, State> = mutableMapOf()

        val bytesCountry = csvFilesLocation.country.openStream().readAllBytes()
        val bytesState = csvFilesLocation.state.openStream().readAllBytes()
        val bytesCity = csvFilesLocation.city.openStream().readAllBytes()

        val hashSum = HashUtils.md5(
            bytesCountry + bytesState + bytesCity
        )
        logger.info { "Hash sum: $hashSum" }

        val info = infoRepository.findByIdOrNull(1) ?: Info()
        if (info.hash == hashSum) {
            logger.info { "Location database is up to date" }
            return
        }

        val streamBufferCountry = ByteArrayInputStream(bytesCountry)
        val streamBufferState = ByteArrayInputStream(bytesState)
        val streamBufferCity = ByteArrayInputStream(bytesCity)

        logger.info { "Loading countries" }
        csvService.upload<Country>(streamBufferCountry)?.forEach {
            mapCountry[it.id!!] = countryRepository.save(it)
        }

        logger.info { "Loading states" }
        csvService.upload<State>(streamBufferState)?.forEach {
            it.country = mapCountry[it.countryIdImpl]!!
            mapState[it.id!!] = stateRepository.save(it)
        }

        logger.info { "Loading cities" }
        csvService.upload<City>(streamBufferCity)?.forEach {
            it.country = mapCountry[it.countryIdImpl]!!
            it.state = mapState[it.stateIdImpl]!!
            cityRepository.save(it)
        }

        streamBufferCountry.close()
        streamBufferState.close()
        streamBufferCity.close()

        info.hash = hashSum
        infoRepository.save(info)

        locationRepository.deleteLocations()
        locationRepository.insertLocation()

        logger.info { "Location database has been refreshed" }
    }

    fun getListLocationSuggestion(
        query: String?,
        limit: Int? = null,
        offset: Int? = null,
        type: TypeLocationEnum? = null
    ): List<LocationSuggestion> =
        locationRepository.findByQueryAndType(query, type?.name ?: "", limit ?: 10, offset ?: 0).map {
            val locationId = LocationId(type = TypeLocationEnum.valueOf(it.type), id = it.id.toInt())
            val name =
                if (it.cityName != null) "${it.cityName}, ${it.stateName}, ${it.countryName}"
                else if (it.stateName != null) "${it.stateName}, ${it.countryName}"
                else it.countryName
            LocationSuggestion(locationId = locationId, name = name)
        }

    fun getLocationInfo(id: Int, type: TypeLocationEnum): LocationFullInfo? {
        return when (type) {
            TypeLocationEnum.City -> {
                cityRepository.findByIdOrNull(id)?.let {
                    LocationFullInfo(
                        city = it.toModel(),
                        state = it.state.toModel(),
                        country = it.country.toModel()
                    )
                }
            }
            TypeLocationEnum.State -> {
                stateRepository.findByIdOrNull(id)?.let {
                    LocationFullInfo(
                        city = null,
                        state = it.toModel(),
                        country = it.country.toModel()
                    )
                }
            }
            TypeLocationEnum.Country -> {
                countryRepository.findByIdOrNull(id)?.let {
                    LocationFullInfo(
                        city = null,
                        state = null,
                        country = it.toModel()
                    )
                }
            }
        }
    }

    fun search(cityName: String?, stateName: String?, countryName: String?): LocationFullInfo? {
        var location: List<Location> = emptyList()

        if (!cityName.isNullOrBlank() && !stateName.isNullOrBlank() && !countryName.isNullOrBlank()) {
            location = locationRepository.search(cityName, stateName, countryName, "City")
        }
        if (!cityName.isNullOrBlank() && !countryName.isNullOrBlank() && location.isEmpty()) {
            location = locationRepository.search(cityName, "", countryName, "City")
            if (location.isEmpty()) {
                location = locationRepository.search("", cityName, countryName, "State")
            }
        }
        if (!stateName.isNullOrBlank() && !countryName.isNullOrBlank() && location.isEmpty()) {
            location = locationRepository.search("", stateName, countryName, "State")
        }
        if (!countryName.isNullOrBlank() && location.isEmpty()) {
            location = locationRepository.search("", "", countryName, "Country")
        }

        return if (location.isNotEmpty()) getLocationInfo(location[0].id!!.id!!.toInt(), TypeLocationEnum.valueOf(location[0].id!!.type!!))
        else null
    }
}
