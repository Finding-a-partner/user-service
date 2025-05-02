package com.findingpartners.user_service.database.repository

import com.findingpartners.user_service.database.entity.User
import com.findingpartners.user_service.model.response.UserResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserDao : JpaRepository<User, Long> {
    fun getByLogin(login: String) : Optional<User>
    fun findByLoginContainingOrNameContainingOrSurnameContaining(query: String, query1: String, query2: String): List<User>
}
