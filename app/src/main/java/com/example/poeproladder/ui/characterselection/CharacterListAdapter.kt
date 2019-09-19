package com.example.poeproladder.ui.characterselection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.util.ClassPoe
import kotlinx.android.synthetic.main.character_selection_item.view.*
import com.example.poeproladder.R

class CharacterListAdapter(val characters: List<CharacterDb>, val listener: (CharacterDb) -> Unit) : RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.character_selection_item, parent, false))
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bindViews(characters[position], listener)
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

        fun bindViews(character: CharacterDb, listener: (CharacterDb) -> Unit) {
            characterNameTextView.text = character.characterName
            characterLevelAndClassTextView.text = itemView.context.getString(R.string.character_level_and_class_tv_selection, character.level, character.classPoe.capitalize())
            leagueTextView.text = itemView.context.getString(R.string.league_tv_selection, character.league)
            itemView.setOnClickListener { listener(character) }
            ClassPoe.classIcon.forEach {
                if (it.key == character.classPoe) classIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, it.value))
            }
        }
    }
}

