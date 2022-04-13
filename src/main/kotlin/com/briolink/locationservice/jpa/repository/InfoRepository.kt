package com.briolink.locationservice.jpa.repository

import com.briolink.locationservice.jpa.entity.Info
import org.springframework.data.jpa.repository.JpaRepository

interface InfoRepository : JpaRepository<Info, Int>
