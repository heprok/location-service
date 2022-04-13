package com.briolink.locationservice.jpa.entity

import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "info")
@Entity
class Info(
    @Id
    @Column(name = "id", nullable = false)
    var id: Int = 1,
    @Column(name = "hash", nullable = false)
    var hash: String = ""
) {
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now()
}
