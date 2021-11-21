package com.briolink.locationservice.jpa.repository;

import com.briolink.locationservice.jpa.projection.LocationInfo
import com.briolink.locationservice.jpa.entity.Location
import com.briolink.locationservice.jpa.entity.LocationId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LocationRepository : JpaRepository<Location, LocationId> {


    @Modifying
    @Query(
            """
        INSERT INTO location (id, type, city_name, state_name, state_code, country_name, country_code_iso2, country_code_iso3, textsearch_tsv)
        (
            SELECT
                countries.id AS id,
                    countries.type as type,
                    null as city_name,
                    null as state_name,
                    null as state_code,
                countries.name as country_name,
                    countries.iso2 as country_code_iso2,
                    countries.iso3 as country_code_iso3,
                to_tsvector('english', concat(countries.name, ' ', countries.iso2, ' ', countries.iso3)) as textsearch_tsv
            FROM
                countries
            
            UNION ALL
            
            SELECT
                states.id AS id,
                    states.type as type,
                    null as city_name,
                    states.name as state_name,
                    states.state_code as state_code,
                countries.name as country_name,
                    countries.iso2 as country_code_iso2,
                    countries.iso3 as country_code_iso3,
                to_tsvector('english', concat(states.name, ' ', countries.name, ' ', countries.iso2, ' ', countries.iso3)) as textsearch_tsv
            FROM
                states
            LEFT JOIN countries ON countries.id = states.country_id
            
            UNION ALL
            
            SELECT
                cities.id AS id,
                    cities.type as type,
                    cities.name as city_name,
                    states.name as state_name,
                    states.state_code as state_code,
                    countries.name as country_name,
                    countries.iso2 as country_code_iso2,
                    countries.iso3 as country_code_iso3,
                to_tsvector('english', concat(cities.name, ' ', states.name, ' ', countries.name, ' ', countries.iso2, ' ', countries.iso3)) as textsearch_tsv
            FROM
                cities
            LEFT JOIN states ON states.id = cities.state_id
            LEFT JOIN countries ON countries.id = cities.country_id
            )
    """,
            nativeQuery = true,
    )
    fun insertLocation()

    @Modifying
    @Query("delete from Location")
    fun deleteLocations()

    @Query(
            """
                WITH myconstants (query) as (
   values (quote_literal(quote_literal(:query)) || ':*')
)
                SELECT
                    id,
                    city_name as cityName,
                    state_name as stateName,
                    country_name as countryName,
                    type,
                    ts_rank(textsearch_tsv, to_tsquery('english', query)) AS rank
                FROM
                    location, myconstants
                WHERE
                    (textsearch_tsv @@ to_tsquery('english', query))
                ORDER BY type = 'Country' desc, type = 'State' desc, type = 'City' desc, rank desc
                LIMIT 5
            """,
            nativeQuery = true,
    )
    fun findByQuery(@Param("query") query: String): List<LocationInfo>
}