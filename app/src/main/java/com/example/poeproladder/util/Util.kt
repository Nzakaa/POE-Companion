package com.example.poeproladder.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.poeproladder.R

object BuildConfig {
    const val CHARACTERWINDOWURL = "https://www.pathofexile.com"
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

object LinkPosition {
    const val START = R.drawable.skill_gem_link_start
    const val MIDDLE = R.drawable.skill_gem_link_mid
    const val END = R.drawable.skill_gem_link_end
    const val SINGLE = R.drawable.skill_gem_link_single
}

object ClassPoe {
    val classIcon: HashMap<String, Int> = hashMapOf(
        "Shadow" to R.drawable.shadow_avatar,
        "Witch" to R.drawable.witch_avatar,
        "Marauder" to R.drawable.marauder_avatar,
        "Duelist" to R.drawable.duelist_avatar,
        "Scion" to R.drawable.scion_avatar,
        "Templar" to R.drawable.templar_avatar,
        "Saboteur" to R.drawable.saboteur_avatar,
        "Trickster" to R.drawable.trickster_avatar,
        "Assassin" to R.drawable.assassin_avatar,
        "Elementalist" to R.drawable.elementalist_avatar,
        "Occultist" to R.drawable.occultist_avatar,
        "Necromancer" to R.drawable.necromancer_avatar,
        "Berserker" to R.drawable.berserker_avatar,
        "Juggernaut" to R.drawable.juggernaut_avatar,
        "Chieftain" to R.drawable.chieftain_avatar,
        "Champion" to R.drawable.champion_avatar,
        "Gladiator" to R.drawable.gladiator_avatar,
        "Slayer" to R.drawable.slayer_avatar,
        "Ascendant" to R.drawable.ascendant_avatar,
        "Hierophant" to R.drawable.hierophant_avatar,
        "Guardian" to R.drawable.guardian_avatar,
        "Inquisitor" to R.drawable.inquisitor_avatar,
        "Deadeye" to R.drawable.deadeye_avatar,
        "Pathfinder" to R.drawable.pathfinder_avatar,
        "Raider" to R.drawable.raider_avatar
    )

}