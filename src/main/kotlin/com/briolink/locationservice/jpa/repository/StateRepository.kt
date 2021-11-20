package com.briolink.locationservice.jpa.repository;

import com.briolink.locationservice.jpa.entity.State
import org.springframework.data.jpa.repository.JpaRepository

interface StateRepository : JpaRepository<State, Int> {
}
