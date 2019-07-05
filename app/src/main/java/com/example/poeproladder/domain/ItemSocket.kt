package com.example.poeproladder.domain

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ItemSocket(
    val group: Int = -1,
    val attr: String = "",
    val sColour: String = ""
)