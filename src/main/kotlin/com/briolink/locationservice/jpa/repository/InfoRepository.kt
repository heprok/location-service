package com.briolink.locationservice.jpa.repository;

import com.briolink.locationservice.jpa.entity.Info
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface InfoRepository : JpaRepository<Info, UUID> {
}
