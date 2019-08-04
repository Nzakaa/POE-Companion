package com.example.poeproladder.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ItemView(
    val name: String,
    val baseMods: String,
    val implicit: String,
    val corrupted: Boolean,
    val labEnchant: String,
    val rarity: Int,
    val properties: String
) : Parcelable {
}

