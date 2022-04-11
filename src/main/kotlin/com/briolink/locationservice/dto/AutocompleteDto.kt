package com.briolink.locationservice.dto

import com.fasterxml.jackson.annotation.JsonProperty

@Deprecated("Remove after update version")
data class AutocompleteDto(
    @JsonProperty
    val id: String,
    @JsonProperty
    val name: String,
)
