package com.example.poeproladder.ui.inventory


import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.poeproladder.BaseApp
import com.example.poeproladder.R
import com.example.poeproladder.dagger.modules.InventoryModule
import com.example.poeproladder.database.CharacterDb
import com.example.poeproladder.database.ItemDb
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.inventory_fragment.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.HashMap


/**
 * A simple [Fragment] subclass.
 *
 */
class InventoryFragment : Fragment(), InventoryContract.InventoryView {

    private lateinit var characterNameTextView: TextView
    private lateinit var characterLevelAndClassTextView: TextView

    private lateinit var progressBar: ProgressBar
    private lateinit var inventoryContainer: ConstraintLayout


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
    private var itemClickedTime: Long = 0

    @Inject
    lateinit var presenter: InventoryContract.InventoryPresenter

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

    override fun showItems(items: HashMap<String, ItemDb>) {

        clearInventory()
        if (items.isNotEmpty()) {
            saveItemsInfo(items)
            for (itemView in itemViews) {
                val key = itemView.key
                val image = itemView.value
                image.setOnClickListener {
                    view?.let { onItemClicked(itemInfo.get(key)) }
                }
                val url = itemInfo.get(key)?.icon
                if (url != null) {
                    Glide.with(this)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(itemView.value)
                }
            }

        }

        val d = Completable.timer(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe { showProgressBar(false) }


    }

    override fun showItem(item: ItemDb) {
        val itemInfoDialog = ItemInfoDialog.newInstance(item)
        itemInfoDialog.show(childFragmentManager, item.name)
    }

    override fun showCharacterInfo(character: CharacterDb) {
        characterNameTextView.text = context?.getString(R.string.character_name_tv_inventory, character.characterName)
        characterLevelAndClassTextView.text = context?.getString(R.string.character_level_and_class_tv_inventory, character.classPoe.capitalize(), character.level)
    }

    override fun showError(error: String) {
        AlertDialog.Builder(this.context!!)
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton("Ok", null)
            .show()
    }

    override fun showProgressBar(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
            inventoryContainer.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            inventoryContainer.visibility = View.VISIBLE
        }
    }


    private fun initViews() {
        characterNameTextView = textView_character_name
        characterLevelAndClassTextView = textView_level_class

        progressBar = progressBar_inventory
        inventoryContainer = container_inventory

        flask1 = imageView_flask1.also { itemViews.put("flask1", it) }
        flask2 = imageView_flask2.also { itemViews.put("flask2", it) }
        flask3 = imageView_flask3.also { itemViews.put("flask3", it) }
        flask4 = imageView_flask4.also { itemViews.put("flask4", it) }
        flask5 = imageView_flask5.also { itemViews.put("flask5", it) }
        helmet = imageView_helm.also { itemViews.put("helm", it) }
        bodyarmour = imageView_bodyarmour.also { itemViews.put("bodyarmour", it) }
        boots = imageView_boots.also { itemViews.put("boots", it) }
        gloves = imageView_gloves.also { itemViews.put("gloves", it) }
        ring = imageView_ring.also { itemViews.put("ring", it) }
        ring2 = imageView_ring2.also { itemViews.put("ring2", it) }
        amulet = imageView_amulet.also { itemViews.put("amulet", it) }
        belt = imageView_belt.also { itemViews.put("belt", it) }
        weapon = imageView_weapon.also { itemViews.put("weapon", it) }
        offhand = imageView_offhand.also { itemViews.put("offhand", it) }
    }

    private fun clearInventory() {
        itemViews.forEach {
            it.value.setImageDrawable(null)
        }
    }

    private fun saveItemsInfo(itemInfo: HashMap<String, ItemDb>) {
        this.itemInfo = itemInfo
    }

    private fun onItemClicked(item: ItemDb?) {
        if (SystemClock.elapsedRealtime() - itemClickedTime < 800) return
        else {
            itemClickedTime = SystemClock.elapsedRealtime()
            item?.let { presenter.openItemInfo(item) }
        }
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
