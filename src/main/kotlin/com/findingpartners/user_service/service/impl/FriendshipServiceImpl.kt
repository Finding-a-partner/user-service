package com.findingpartners.user_service.service.impl


import com.findingpartners.user_service.database.entity.Friendship
import com.findingpartners.user_service.database.entity.FriendshipStatus
import com.findingpartners.user_service.database.repository.FriendshipDao
import com.findingpartners.user_service.database.repository.UserDao
import com.findingpartners.user_service.model.request.FriendshipRequest
import com.findingpartners.user_service.model.response.FriendResponse
import com.findingpartners.user_service.model.response.FriendshipResponse
import com.findingpartners.user_service.model.response.UserResponse
import com.findingpartners.user_service.service.FriendshipService
import com.findingpartners.user_service.util.FriendshipMapper
import com.findingpartners.user_service.util.UserMapper
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException
import org.springframework.stereotype.Service

@Service
@Transactional
class FriendshipServiceImpl(
    private val friendshipDao: FriendshipDao,
    private val userDao: UserDao,
    val userMapper: UserMapper,
    val friendMapper: FriendshipMapper
) : FriendshipService {
    // Отправить запрос в друзья
    override fun sendFriendRequest(request: FriendshipRequest): FriendshipResponse {
        if (request.userId == request.friendId) {
            throw IllegalArgumentException("Нельзя отправить запрос самому себе")
        }

        val sender = userDao.findById(request.userId)
            .orElseThrow { NoSuchElementException("Отправитель не найден") }

        val receiver = userDao.findById(request.friendId)
            .orElseThrow { NoSuchElementException("Получатель не найден") }

        friendshipDao.findByUserAndFriend(sender, receiver)?.let {
            throw IllegalStateException("Запрос уже отправлен")
        }

        val entity = Friendship(
            user = sender,
            friend = receiver,
            status = FriendshipStatus.PENDING
        )
        return friendMapper.entityToResponse(friendshipDao.save(entity))
    }

    // Ответить на запрос
    override fun respondToFriendRequest(requestId: Long, request: FriendshipRequest): FriendshipResponse {
        val entity = friendshipDao.findById(requestId).orElseThrow {
            NoSuchElementException("Запрос с ID $requestId не найден")
        }

        if (entity.friend.id != request.friendId) {
            throw SecurityException("Только получатель может ответить на запрос")
        }

        if (entity.status != FriendshipStatus.PENDING) {
            throw IllegalStateException("Запрос уже обработан")
        }

        entity.status = if (request.status == FriendshipStatus.ACCEPTED) FriendshipStatus.ACCEPTED else FriendshipStatus.REJECTED
        return friendMapper.entityToResponse(friendshipDao.save(entity))
    }
    override fun getUserRequests (userId: Long, status: FriendshipStatus): List<FriendResponse> {
        val user = userDao.findById(userId).orElseThrow {
            NoSuchElementException("Пользователь с ID $userId не найден")
        }
        val friendships = friendshipDao.findByUserOrFriendAndStatus(user, user, status)
        return friendships.map { friendship ->
            // Определяем, кто является другом (не текущий пользователь)
            val friend = if (friendship.user.id == userId) friendship.friend else friendship.user

            FriendResponse(
                friend = userMapper.entityToResponse(friend),  // маппим User в UserResponse
                status = friendship.status,
                id = friendship.id
            )
            }
    }

    override fun deleteFriend(currentUserId: Long, friendId: Long) {
        val currentUser = userDao.findById(currentUserId)
            .orElseThrow { NotFoundException("Current user not found") }

        val friend = userDao.findById(friendId)
            .orElseThrow { NotFoundException("Friend not found") }

        val friendships = friendshipDao.findAllBetweenUsers(currentUserId, friendId)
        if (friendships.isEmpty()) {
            throw NotFoundException("Friendship not found")
        }

        friendshipDao.deleteAll(friendships)
    }

}
