package com.findingpartners.user_service.service

import com.findingpartners.user_service.model.request.UserRequest
import com.findingpartners.user_service.model.response.UserResponse

interface UserService {
    fun getAll(): List<UserResponse>
    fun getById(id: Long) : UserResponse
    fun getByLogin(login: String) : UserResponse
    fun update(id: Long, request: UserRequest) : UserResponse
    fun delete(id: Long)
    fun create (request: UserRequest) : UserResponse
}