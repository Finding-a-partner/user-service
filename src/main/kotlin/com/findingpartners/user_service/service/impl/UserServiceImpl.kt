package com.findingpartners.user_service.service.impl

import com.findingpartners.user_service.database.entity.User
import com.findingpartners.user_service.database.repository.UserDao
import com.findingpartners.user_service.model.request.UserRequest
import com.findingpartners.user_service.model.response.UserResponse
import com.findingpartners.user_service.service.UserService
import com.findingpartners.user_service.util.UserMapper
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserServiceImpl (
    val dao: UserDao,
    val mapper: UserMapper,
    val passwordEncoder: PasswordEncoder
): UserService {
    override fun getAll(): List<UserResponse> =
        dao.findAll().map{
            mapper.entityToResponse(it)
        }

    override fun getByLogin(login: String): UserResponse {
        return mapper.entityToResponse(
            dao.getByLogin(login).orElseThrow {
                RuntimeException("User with login $login not found")
            }
        )
    }

    override fun getById(id: Long): UserResponse {
        return mapper.entityToResponse(dao.findById(id).orElseThrow(){ throw RuntimeException("")})
    }
    @Transactional
    override fun update (id: Long, request: UserRequest): UserResponse {
       val entity = dao.findById(id).orElseThrow { throw RuntimeException("") }
           .apply{
               login = request.login
               email = request.email
               description = request.description
               password = passwordEncoder.encode(request.password)
           }
        val updatedEntity = dao.save(entity)  // Явно сохраняем изменения
        return mapper.entityToResponse(entity)
    }

    override fun delete (id: Long) {
        val entity = dao.findById(id).orElseThrow { throw RuntimeException("") }
        dao.delete(entity)
    }
    override fun create (request: UserRequest): UserResponse {
        val entity = User(
            login = request.login,
            email = request.email,
            description = request.description,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            surname = request.surname
        )
        return mapper.entityToResponse(dao.save(entity))

    }
}