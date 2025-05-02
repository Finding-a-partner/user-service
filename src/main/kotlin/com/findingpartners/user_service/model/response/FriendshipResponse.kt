package com.findingpartners.user_service.model.response

import com.findingpartners.user_service.database.entity.FriendshipStatus
import org.bouncycastle.util.Longs

data class FriendResponse (
    val id: Long,
    val status: FriendshipStatus,
    val friend: UserResponse
)

data class FriendshipResponse (
    val id: Long,
    val userId: Long,
    val friendId: Long,
    val status: FriendshipStatus
)