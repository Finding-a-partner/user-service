package com.findingpartners.user_service.database.entity

import jakarta.persistence.*

@Entity
@Table(name = "friendship")
class Friendship (
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    @ManyToOne
    @JoinColumn(name = "friend_id")
    val friend: User,

    @Enumerated(EnumType.STRING)
    var status: FriendshipStatus = FriendshipStatus.PENDING,

) : AbstractEntity()

enum class FriendshipStatus {
    PENDING, ACCEPTED, REJECTED, BLOCKED
}