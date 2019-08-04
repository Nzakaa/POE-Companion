package com.example.poeproladder.ui.inventory

import android.app.Dialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils.indexOf
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.poeproladder.BaseApp
import com.example.poeproladder.R
import com.example.poeproladder.database.ItemDb
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.regex.Pattern

class ItemInfoDialog : DialogFragment() {

    lateinit var itemInfo: ItemDb

    lateinit var itemNameTextView: TextView
    lateinit var itemNameFrame: FrameLayout
    lateinit var itemBaseTextView: TextView
    lateinit var itemImplicitTextView: TextView
    lateinit var itemLavEnchantTextView: TextView
    lateinit var itemPropertiesTextView: TextView
    lateinit var itemCorruptedTextView: TextView

    lateinit var itemCard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString("item")?.let {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapterMoshi = moshi.adapter(ItemDb::class.java)
            val item = adapterMoshi.fromJson(it)
            if (item != null) itemInfo = item
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_item_info, null)
        view?.let { view -> initViews(view) }
        showItemInfo()
        itemCard.setOnClickListener { dismiss() }
        builder.setView(view)
        return builder.create()
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.dialog_item_info, null)
//    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initViews()
//        showItemInfo()
//        itemCard.setOnClickListener { dismiss() }
//    }

    private fun initViews(view: View) {
        itemNameTextView = view.findViewById(R.id.textView_item_name)
        itemNameFrame = view.findViewById(R.id.frame_item_name)
        itemBaseTextView = view.findViewById(R.id.textView_base_properties)
        itemImplicitTextView = view.findViewById(R.id.textView_implicit)
        itemLavEnchantTextView = view.findViewById(R.id.textView_lab_enchant)
        itemPropertiesTextView = view.findViewById(R.id.textView_properties)
        itemCorruptedTextView = view.findViewById(R.id.textView_corrupted)
        itemCard = view.findViewById(R.id.cardView_item)
    }

    private fun showItemInfo() {
        prepareTitle()
        prepareBaseModes()

//        itemBaseTextView.text = itemInfo.base
//        itemImplicitTextView.text = itemInfo.implicit
//        itemLavEnchantTextView.text = itemInfo.labEnchant
//        itemPropertiesTextView.text = itemInfo.properties
//        if (!itemInfo.corrupted) itemCorruptedTextView.visibility = View.GONE
    }

    private fun prepareTitle() {
        val builder = SpannableStringBuilder()
        var color: Int

        when (itemInfo.itemRarity) {
            0 -> {
                color = ContextCompat.getColor(BaseApp.applicationContext(), R.color.white)
                itemNameTextView.setBackgroundResource(R.color.whiteBackground)
            }
            1 -> {
                color = ContextCompat.getColor(BaseApp.applicationContext(), R.color.magic)
                itemNameTextView.setBackgroundResource(R.color.magicBackground)
            }
            2 -> {
                color = ContextCompat.getColor(BaseApp.applicationContext(), R.color.rare)
                itemNameTextView.setBackgroundResource(R.color.rareBackground)
            }
            3 -> {
                color = ContextCompat.getColor(BaseApp.applicationContext(), R.color.unique)
                itemNameTextView.setBackgroundResource(R.color.uniqueBackground)
            }
            else -> throw IllegalArgumentException("Item rarity: ${itemInfo.itemRarity} out of range")
        }
        var title = if (itemInfo.name != "") "${itemInfo.name}\n${itemInfo.base}" else "${itemInfo.base}"

        val titleSpan = SpannableString(title)
            .apply {
                setSpan(ForegroundColorSpan(color), 0, this.length, 0)
            }
        builder.append(titleSpan)

        itemNameTextView.setText(builder, TextView.BufferType.SPANNABLE)
        val gradientDrawable =
            ContextCompat.getDrawable(BaseApp.applicationContext(), R.drawable.item_rarity_border)?.mutate()
        gradientDrawable?.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        itemNameFrame.background = gradientDrawable
    }

    private fun prepareBaseModes() {
        val builder = SpannableStringBuilder()
        var color: Int = -1
        var value: String = ""
        for (property in itemInfo.properties) {
            if (property.values.isEmpty())
                builder.append(property.name).append("\n")
            else {
                when {
                    property.values.size == 1 && itemInfo.inventoryId != "Flask" -> {
                        builder.append(property.name).append(": ")
                        color = valueColor((property.values[0][1] as Double).toInt())
                        value = property.values[0][0].toString()
                        var span = SpannableString(value)
                        span.setSpan(ForegroundColorSpan(color), 0, span.length, 0)
                        builder.append(span).append("\n")
                    }

                    property.values.size >= 2 && itemInfo.inventoryId != "Flask" -> {
                        builder.append(property.name).append(": ")
                        for ((index, values) in property.values.withIndex()) {
                            color = valueColor((values[1] as Double).toInt())
                            value = values[0].toString()
                            var span = SpannableString(value)
                            span.setSpan(ForegroundColorSpan(color), 0, span.length, 0)
                            builder.append(span)
                            if (index != property.values.size - 1) builder.append(", ") else builder.append("\n")
                        }
                    }

                    itemInfo.inventoryId == "Flask" -> {
                        if (property.name == "Quality") {
                            builder.append(property.name).append(": ")
                            color = valueColor((property.values[0][1] as Double).toInt())
                            value = property.values[0][0].toString()
                            var span = SpannableString(value)
                            span.setSpan(ForegroundColorSpan(color), 0, span.length, 0)
                            builder.append(span).append("\n")
                        } else {
                            var flaskText = SpannableStringBuilder(property.name)
                            for ((index, values) in property.values.withIndex()) {
                                color = valueColor((values[1] as Double).toInt())
                                value = values[0].toString()
                                var divider = "%$index"
                                var start = indexOf(flaskText, divider)
                                flaskText.replace(start, start+2, value)
                                flaskText.setSpan(ForegroundColorSpan(color), start, start + value.length, 0)
                            }
                            builder.append(flaskText).append("\n")
                        }
                    }
                }
                itemBaseTextView.setText(builder, TextView.BufferType.SPANNABLE)
            }
        }
    }


    private fun valueColor(color: Int): Int {
        return when (color) {
            0 -> ContextCompat.getColor(BaseApp.applicationContext(), R.color.itemTextWhiteColor)
            1 -> ContextCompat.getColor(BaseApp.applicationContext(), R.color.magic)
            2 -> ContextCompat.getColor(BaseApp.applicationContext(), R.color.rare)
            3 -> ContextCompat.getColor(BaseApp.applicationContext(), R.color.unique)
            4 -> ContextCompat.getColor(BaseApp.applicationContext(), R.color.fire)
            5 -> ContextCompat.getColor(BaseApp.applicationContext(), R.color.cold)
            6 -> ContextCompat.getColor(BaseApp.applicationContext(), R.color.lightning)
            else -> ContextCompat.getColor(BaseApp.applicationContext(), R.color.itemTextColor)
        }
    }

    companion object {
        fun newInstance(item: ItemDb): ItemInfoDialog {
            val f = ItemInfoDialog()
            val args = Bundle()
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapterMoshi = moshi.adapter(ItemDb::class.java)
            val itemJson = adapterMoshi.toJson(item)
            args.putString("item", itemJson)
            f.setArguments(args)
            return f
        }
    }
}