package com.briolink.locationservice.jpa.entity

import org.hibernate.Hibernate
import java.io.Serializable
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
open class LocationId : Serializable {
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "type", nullable = false, length = 100)
    open var type: String? = null

    override fun hashCode(): Int = Objects.hash(id, type)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as LocationId

        return id == other.id &&
            type == other.type
    }

    companion object {
        private const val serialVersionUID = -409975816991089335L
    }
}
