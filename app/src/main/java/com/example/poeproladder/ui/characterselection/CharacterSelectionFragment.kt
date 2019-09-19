package com.example.poeproladder.ui.characterselection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.poeproladder.BaseApp

import com.example.poeproladder.R
import com.example.poeproladder.activities.HostingActivity
import com.example.poeproladder.dagger.modules.CharacterSelectionModule
import com.example.poeproladder.database.*
import com.example.poeproladder.util.hideKeyboard
import kotlinx.android.synthetic.main.character_selection_fragment.*
import java.util.*
import javax.inject.Inject


class CharacterSelectionFragment :
    Fragment(),
    CharacterSelectionContract.MyAccountView,
    FragmentNotVisible {

    private lateinit var accountNameTextView: TextView
    private lateinit var defaultMessageTextView: TextView
    private lateinit var accountNameEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var adapter: CharacterListAdapter

    private lateinit var hostingActivity: HostingActivity

    @Inject
    lateinit var presenter: CharacterSelectionContract.CharacterSelectionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.character_selection_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupAccountEditText()
        presenter.onBind()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hostingActivity = activity as HostingActivity
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
        presenter.detachView()
    }

    override fun showProgressBar(show: Boolean) {
        if (show) {
            recyclerView.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    override fun showCharacterList(characters: List<CharacterDb>) {
        adapter = CharacterListAdapter(
            characters,
            { character: CharacterDb -> characterClicked(character) })
        defaultMessageTextView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)
        runAnimation(recyclerView)
    }

    override fun showDefaultScreen() {
        defaultMessageTextView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }


    override fun showError(error: String) {
        when (error) {
            "HTTP 404 " -> showAlertDialog("Wrong account name")
            else -> showAlertDialog(error)
        }
    }

    override fun navigateToInventory() {
        hostingActivity.navigateToPage(HostingActivity.INVENTORYPAGE)
    }

    override fun notVisible() {
        accountNameEditText.visibility = View.GONE
    }


    private fun injectDependencies() {
        BaseApp.getAppComponent().plus(CharacterSelectionModule(this)).inject(this)
    }

    private fun runAnimation(recyclerView: RecyclerView) {
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down)
        recyclerView.layoutAnimation = controller
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()

    }

    private fun showAlertDialog(error: String) {
        AlertDialog.Builder(this.context!!)
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton("Ok", null)
            .show()
    }

    private fun characterClicked(character: CharacterDb) {
        presenter.onCharacterPicked(character.characterName)
    }

    private fun setupAccountEditText() {
        accountNameEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)

        accountNameEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                accountNameEditText.hideKeyboard()
                if (accountNameEditText.text.toString() != "") {
                    val requestedAccountName = accountNameEditText.text.toString()
                    accountNameEditText.text = null
                    accountNameEditText.clearFocus()
                    defaultMessageTextView.visibility = View.GONE
                    accountNameEditText.visibility = View.GONE
                    presenter.getCharacters(requestedAccountName)
                } else {
                    showError("Please insert account name")
                }
                true
            } else false
        }
    }

    private fun initViews() {
        accountNameTextView = textView_account_name.apply {
            setOnClickListener {
                if (accountNameEditText.visibility != View.VISIBLE)
                    accountNameEditText.visibility = View.VISIBLE
                else accountNameEditText.visibility = View.GONE
            }
        }

        accountNameEditText = editText_account.apply {
            isFocusableInTouchMode = true
        }
        defaultMessageTextView = textView_default_message
        progressBar = progressBar_account

        recyclerView = recyclerView_character_list.apply {
            layoutManager = LinearLayoutManager(this.context)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

    }

    override fun showCurrentAccount(accountName: String) {
        accountNameTextView.text = accountName.toUpperCase(Locale.getDefault())
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CharacterSelectionFragment()
    }
}
