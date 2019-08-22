package com.example.poeproladder.ui.characterselection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.poeproladder.R
import com.example.poeproladder.database.CharacterDb
import kotlinx.android.synthetic.main.character_selection_item.view.*

class CharacterListAdapter(val characters: List<CharacterDb>, val listener: (CharacterDb) -> Unit) : RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.character_selection_item, parent, false))
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.BindViews(characters[position], listener)
    }

    class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var characterNameTextView: TextView
        private var characterLevelAndClassTextView: TextView
        private var leagueTextView: TextView
        private var classIcon: ImageView

        init {
            characterNameTextView = view.textView_character_name
            characterLevelAndClassTextView = view.textView_level_class
            leagueTextView = view.textView_league
            classIcon = view.imageView_ascendancy
        }

        fun BindViews(character: CharacterDb, listener: (CharacterDb) -> Unit) {
            characterNameTextView.text = character.characterName
            characterLevelAndClassTextView.text = "Level: ${character.level} ${character.classPoe.capitalize()}"
            leagueTextView.text = "${character.league} League"
            itemView.setOnClickListener { listener(character) }
        }
    }
}

