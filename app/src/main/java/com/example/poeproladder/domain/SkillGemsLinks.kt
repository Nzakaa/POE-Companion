package com.example.poeproladder.domain

import com.example.poeproladder.network.SocketedItemJson

data class SkillGemsLinks (
    val links: HashMap<Int, Array<SocketedItemJson>> = hashMapOf(),
    val inventoryId: String,
    val sockets: Int
    )