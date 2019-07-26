package com.example.poeproladder.ui.inventory


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

import com.example.poeproladder.R
import com.example.poeproladder.database.CharacterDatabase
import com.example.poeproladder.database.CharacterItemsDb
import com.example.poeproladder.database.getDatabase
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractor
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractorImpl
import com.example.poeproladder.interactors.Network.CharacterNetworkInteractor
import com.example.poeproladder.interactors.Network.CharacterNetworkInteractorImpl
import com.example.poeproladder.network.Network
import com.example.poeproladder.repository.CharactersRepository
import com.example.poeproladder.repository.CharactersRepositoryImpl
import com.example.poeproladder.session.SessionService
import com.example.poeproladder.session.SessionServiceImpl
import kotlinx.android.synthetic.main.fragment_inventory.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class InventoryFragment : Fragment(), InventoryContract.InventoryView {
    //dependency injection
    private lateinit var database: CharacterDatabase
    private lateinit var repository: CharactersRepository
    private lateinit var session: SessionService
    private lateinit var databaseInteractor: CharacterDatabaseInteractor
    private lateinit var networkInteractor: CharacterNetworkInteractor

    private lateinit var textView: TextView

    private lateinit var presenter: InventoryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dependencyInjection()
        presenter = InventoryPresenter(this, repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        presenter.onBind()
        presenter.getItems()
    }

    override fun showItems(items: CharacterItemsDb) {
        val builder = StringBuilder()
        for (item in items.characterItems) {
            if (builder.isNotEmpty()) builder.append(", ")
            when {
                item.name == "" -> builder.append("${item.base}")
                else -> builder.append("${item.name} ${item.base}")
            }
        }
        textView.text = builder.toString()
    }

    override fun showError(error: String) {
        AlertDialog.Builder(this.context!!)
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton("Ok", null)
            .show()
    }

    private fun initViews() {
        textView = test_textview
    }

    private fun dependencyInjection() {
        database = getDatabase(activity!!.application)
        session = SessionServiceImpl.getInstance(activity!!.application)
        databaseInteractor = CharacterDatabaseInteractorImpl
            .getInstance(database, session)
        networkInteractor = CharacterNetworkInteractorImpl
            .getInstance(database, Network, databaseInteractor)
        repository = CharactersRepositoryImpl
            .getInstance(session, databaseInteractor, networkInteractor)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InventoryFragment()
    }
}
