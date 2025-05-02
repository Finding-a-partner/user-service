package com.findingpartners.user_service.database.repository

import com.findingpartners.user_service.database.entity.Friendship
import com.findingpartners.user_service.database.entity.FriendshipStatus
import com.findingpartners.user_service.database.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface FriendshipDao : JpaRepository<Friendship, Long> {
    fun findByUserAndFriend(user: User, friend: User): Friendship?
    fun findAllByUserAndStatus(user: User, status: FriendshipStatus): List<Friendship>
    fun findAllByFriendAndStatus(friend: User, status: FriendshipStatus): List<Friendship>
    fun findByUserOrFriendAndStatus(user: User, friend: User, status: FriendshipStatus): List<Friendship>
    fun findByUserIdOrFriendId(currentUserId: Long, friendId: Long): List<Friendship>
}