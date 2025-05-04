package com.findingpartners.user_service.database.entity

import jakarta.persistence.*

@Entity
@Table(name = "`user`")
class User(
    @Column(nullable = true)
    var description: String? = null,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false, unique = true)
    var login: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    var surname: String
) : AbstractEntity()
