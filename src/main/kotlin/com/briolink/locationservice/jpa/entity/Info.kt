package com.briolink.locationservice.jpa.entity

import java.time.Instant
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "info")
@Entity
class Info {
    @Id
    @Column(name = "md5_city", nullable = false)
    var id: UUID? = null

    @Column(name = "md5_country", nullable = false)
    lateinit var md5Country: UUID

    @Column(name = "md5_state", nullable = false)
    lateinit var md5State: UUID

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now()
}
