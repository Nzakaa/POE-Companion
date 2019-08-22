package com.example.poeproladder.ui.inventory


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.poeproladder.BaseApp

import com.example.poeproladder.R
import com.example.poeproladder.dagger.modules.InventoryModule
import com.example.poeproladder.database.CharacterDatabase
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.ItemDb
import com.example.poeproladder.database.getDatabase
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractor
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractorImpl
import com.example.poeproladder.interactors.Network.CharacterNetworkInteractor
import com.example.poeproladder.interactors.Network.CharacterNetworkInteractorImpl
import com.example.poeproladder.repository.CharactersRepository
import com.example.poeproladder.repository.CharactersRepositoryImpl
import kotlinx.android.synthetic.main.inventory_fragment.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class InventoryFragment : Fragment(), InventoryContract.InventoryView {
    //dependency injection
//    private lateinit var database: CharacterDatabase
//    private lateinit var repository: CharactersRepository
//    private lateinit var databaseInteractor: CharacterDatabaseInteractor
//    private lateinit var networkInteractor: CharacterNetworkInteractor

    private lateinit var characterNameTextView: TextView
    private lateinit var characterLevelAndClassTextView: TextView

    private lateinit var flask1: ImageView
    private lateinit var flask2: ImageView
    private lateinit var flask3: ImageView
    private lateinit var flask4: ImageView
    private lateinit var flask5: ImageView
    private lateinit var helmet: ImageView
    private lateinit var bodyarmour: ImageView
    private lateinit var boots: ImageView
    private lateinit var gloves: ImageView
    private lateinit var ring: ImageView

    private lateinit var ring2: ImageView
    private lateinit var amulet: ImageView
    private lateinit var belt: ImageView
    private lateinit var weapon: ImageView
    private lateinit var offhand: ImageView

    private var itemViews: HashMap<String, ImageView> = hashMapOf()

    @Inject lateinit var presenter: InventoryPresenter

    private var itemInfo: HashMap<String, ItemDb> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dependencyInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.inventory_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        presenter.onBind()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
        presenter.detachView()
    }

    override fun showItems(item: HashMap<String, ItemDb>) {
        saveItemsInfo(item)
        for (itemView in itemViews) {
            val key = itemView.key
            val image = itemView.value
            image.setOnClickListener {
                view?.let { onItemClicked(itemInfo.get(key)) }
            }
            val url = itemInfo.get(key)?.icon
            Glide.with(this).load(url).into(itemView.value)
        }
    }

    override fun showItem(item: ItemDb) {
        val itemInfoDialog = ItemInfoDialog.newInstance(item)
        itemInfoDialog.show(childFragmentManager, "${item.name}")
    }

    override fun showCharacterInfo(character: CharacterDb) {
        characterNameTextView.text = character.characterName
        characterLevelAndClassTextView.text = "${character.classPoe.capitalize()} Level: ${character.level}"
    }

    override fun showError(error: String) {
        AlertDialog.Builder(this.context!!)
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton("Ok", null)
            .show()
    }

    override fun showProgressBar(show: Boolean) {
        //TODO
    }


    private fun initViews() {
        characterNameTextView = textView_character_name
        characterLevelAndClassTextView = textView_level_class

        flask1 = imageView_flask1.also {itemViews.put("flask1", it)}
        flask2 = imageView_flask2.also {itemViews.put("flask2", it)}
        flask3 = imageView_flask3.also {itemViews.put("flask3", it)}
        flask4 = imageView_flask4.also {itemViews.put("flask4", it)}
        flask5 = imageView_flask5.also {itemViews.put("flask5", it)}
        helmet = imageView_helm.also {itemViews.put("helm", it)}
        bodyarmour = imageView_bodyarmour.also {itemViews.put("bodyarmour", it)}
        boots = imageView_boots.also {itemViews.put("boots", it)}
        gloves = imageView_gloves.also {itemViews.put("gloves", it)}
        ring = imageView_ring.also {itemViews.put("ring", it)}
        ring2 = imageView_ring2.also {itemViews.put("ring2", it)}
        amulet = imageView_amulet.also {itemViews.put("amulet", it)}
        belt = imageView_belt.also {itemViews.put("belt", it)}
        weapon = imageView_weapon.also {itemViews.put("weapon", it)}
        offhand = imageView_offhand.also {itemViews.put("offhand", it)}
    }

    private fun saveItemsInfo(itemInfo: HashMap<String, ItemDb>) {
        this.itemInfo = itemInfo
    }

    private fun onItemClicked(item: ItemDb?) {
        item?.let { presenter.openItemInfo(item) }
    }

    private fun dependencyInjection() {
        BaseApp.getAppComponent().plus(InventoryModule((this))).inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InventoryFragment()
    }
}
