package com.findingpartners.user_service.model.response

import java.time.LocalDateTime


data class UserResponse(
    val id: Long,
    val createdAt: LocalDateTime,
    val login: String,
    val email: String,
    val description: String? = null,
    val name: String,
    val surname: String? = null
)
