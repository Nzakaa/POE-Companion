package com.example.poeproladder.ui.skillgems


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import com.bumptech.glide.Glide
import com.example.poeproladder.BaseApp

import com.example.poeproladder.R
import com.example.poeproladder.dagger.modules.SkillGemsModule
import com.example.poeproladder.domain.SkillGemsLinks
import com.example.poeproladder.network.SocketedItemJson
import com.example.poeproladder.util.LinkPosition
import kotlinx.android.synthetic.main.skill_gems_row.*
import javax.inject.Inject


class SkillGemsFragment : Fragment(), SkillGemsContract.SkillGemsView {

    lateinit var linkImage: AppCompatImageView
    lateinit var gemImage: AppCompatImageView
    lateinit var gemNameTV: AppCompatTextView
    lateinit var gemLevelTV: AppCompatTextView

    private lateinit var skillLinks: List<SkillGemsLinks>
    private val generatedIds: HashMap<String, Int> = hashMapOf()


    @Inject
    lateinit var presenter: SkillGemsContract.SkillGemsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dependencyInjection()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.skill_gems_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val inflater = LayoutInflater.from(BaseApp.applicationContext())
//        val lLayoutCard = view.findViewById<LinearLayoutCompat>(R.id.lLayout_skill_gem_card)
//        val cardView = inflater.inflate(R.layout.skill_gems_card_item, lLayoutCard, true)
//        val lLayoutRow = cardView.findViewById<LinearLayoutCompat>(R.id.lLayout_skill_gem_row)
//        val firstRow = inflater.inflate(R.layout.skill_gems_row, lLayoutRow, false)
//        firstRow.findViewById<ImageView>(R.id.imageView_link).apply {
//            setImageResource(R.drawable.skill_gem_link_start)
//        }
//        val secondRow = inflater.inflate(R.layout.skill_gems_row, lLayoutRow, false)
//        secondRow.findViewById<ImageView>(R.id.imageView_link).apply {
//            setImageResource(R.drawable.skill_gem_link_mid)
//        }
//        val thirdRow = inflater.inflate(R.layout.skill_gems_row, lLayoutRow, false)
//        thirdRow.findViewById<ImageView>(R.id.imageView_link).apply {
//            setImageResource(R.drawable.skill_gem_link_end)
//        }
//        lLayoutRow.addView(firstRow)
//        lLayoutRow.addView(secondRow)
//        lLayoutRow.addView(thirdRow)

        presenter.onBind()
//
//        val thirdRow = inflater.inflate(R.layout.skill_gems_row, lLayoutRow, true)
//        thirdRow.findViewById<ImageView>(R.id.imageView_link).apply {
//            setImageResource(R.drawable.skill_gem_link_end)
//        }

//        lLayoutCard.addView(cardView)
//        linkImage.setImageResource(R.drawable.skill_gem_link_start)
//        linkImage.setImageResource(R.drawable.skill_gem_link_mid)
    }

    override fun showSkillGems(links: List<SkillGemsLinks>) {
        skillLinks = links
        val inflater = LayoutInflater.from(BaseApp.applicationContext())
        val lLayoutCard = view!!.findViewById<LinearLayoutCompat>(R.id.lLayout_skill_gem_card)
        lLayoutCard.removeAllViews()
        for (item in skillLinks) {
            prepareCardForItem(lLayoutCard, inflater, item)
        }

        //TODO proper sorting by links with socketed supports (search with thru both lists, find maximum links then validate what gems inserted, and set link status)

    }

    private fun prepareCardForItem(layout: LinearLayoutCompat, infl: LayoutInflater, item: SkillGemsLinks) {
        val cardView = infl.inflate(R.layout.skill_gems_card_item, layout, false)
        cardView.id = View.generateViewId()
            .also { generatedIds.put("cardViewFor${item.inventoryId}", cardView.id) }
        cardView.findViewById<AppCompatTextView>(R.id.textView_inventory_slot)
            .apply { text = item.inventoryId }
        val lLayoutRow = cardView.findViewById<LinearLayoutCompat>(R.id.lLayout_skill_gem_row)
        lLayoutRow.id = View.generateViewId()
            .also { generatedIds.put("llayoutCardViewFor${item.inventoryId}", lLayoutRow.id) }

        layout.addView(cardView)  // TODO after completing card??? looks fine

        for (key in item.links.keys.sorted()) {
            val socketedSkills = item.links[key]!!
            for ((row, skill) in socketedSkills.withIndex()) {
                if (socketedSkills.size == 1)
                    prepareRowForSkill(lLayoutRow, infl, LinkPosition.SINGLE, skill)
                else
                    when (row) {
                        0 -> prepareRowForSkill(lLayoutRow, infl, LinkPosition.START, skill)
                        socketedSkills.size - 1 -> prepareRowForSkill(lLayoutRow, infl, LinkPosition.END, skill)
                        else -> prepareRowForSkill(lLayoutRow, infl, LinkPosition.MIDDLE, skill)
                    }
            }
        }
    }

    private fun prepareRowForSkill(
        container: LinearLayoutCompat,
        infl: LayoutInflater,
        linkDrawable: Int,
        skill: SocketedItemJson
    ) {
        val row = infl.inflate(R.layout.skill_gems_row, container, false)
        row.findViewById<ImageView>(R.id.imageView_link)
            .apply { setImageResource(linkDrawable) }
        row.findViewById<ImageView>(R.id.imageView_gem_icon)
            .also { Glide.with(this).load(skill.icon).into(it) }
        row.findViewById<TextView>(R.id.textView_gem_name)
            .apply { text = skill.name }

        var level = ""
        var qual = "0"
        for (property in skill.socketedItem) {
            when (property.name) {
//                "Level" -> stringBuilder.append("(").append(property.values[0][0].toString()).append("/")
                "Level" -> {
                    level = property.values[0][0].toString()
                    level = level.filter { it.isDigit() }
                }
                "Quality" -> {
                    qual = property.values[0][0].toString()
                    val reg = Regex("[^A-Za-z0-9 ]")
                    qual = reg.replace(qual, "")
                }
            }
        }

        row.findViewById<TextView>(R.id.textView_gem_level)
            .apply { text = "($level/$qual)" }
        container.addView(row)
    }

    override fun showError(error: String) {
        AlertDialog.Builder(this.context!!)
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton("Ok", null)
            .show()
    }

    private fun initViews() {
        linkImage = imageView_link
        gemImage = imageView_gem_icon
        gemNameTV = textView_gem_name
        gemLevelTV = textView_gem_level
    }

    private fun dependencyInjection() {
        BaseApp.getAppComponent().plus(SkillGemsModule(this)).inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SkillGemsFragment()
    }
}
