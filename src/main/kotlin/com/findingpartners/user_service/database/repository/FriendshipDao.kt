package com.findingpartners.user_service.database.repository

import com.findingpartners.user_service.database.entity.Friendship
import com.findingpartners.user_service.database.entity.FriendshipStatus
import com.findingpartners.user_service.database.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface FriendshipDao : JpaRepository<Friendship, Long> {
    fun findByUserAndFriend(user: User, friend: User): Friendship?
    fun findByUserOrFriendAndStatus(user: User, friend: User, status: FriendshipStatus): List<Friendship>

       @Query("""
        SELECT f FROM Friendship f
        WHERE (f.user.id = :userId1 AND f.friend.id = :userId2)
           OR (f.user.id = :userId2 AND f.friend.id = :userId1)
    """)
    fun findAllBetweenUsers(
        @Param("userId1") userId1: Long,
        @Param("userId2") userId2: Long
    ): List<Friendship>
}