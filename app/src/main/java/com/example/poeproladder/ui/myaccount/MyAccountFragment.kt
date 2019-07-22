package com.example.poeproladder.ui.myaccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

import com.example.poeproladder.R
import com.example.poeproladder.database.*
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractor
import com.example.poeproladder.interactors.Database.CharacterDatabaseInteractorImpl
import com.example.poeproladder.interactors.Network.CharacterNetworkInteractor
import com.example.poeproladder.interactors.Network.CharacterNetworkInteractorImpl
import com.example.poeproladder.network.Network
import com.example.poeproladder.repository.CharactersRepository
import com.example.poeproladder.repository.CharactersRepositoryImpl
import com.example.poeproladder.session.SessionService
import com.example.poeproladder.session.SessionServiceImpl
import kotlinx.android.synthetic.main.fragment_my_account.*


class MyAccountFragment : Fragment(), MyAccountContract.MyAccountView {
    //dependecy injection
    private lateinit var database: CharacterDatabase
    private lateinit var repository: CharactersRepository
    private lateinit var session: SessionService
    private lateinit var databaseInteractor: CharacterDatabaseInteractor
    private lateinit var networkInteractor: CharacterNetworkInteractor

    lateinit var textView: TextView
    lateinit var getItemsButton: Button
    lateinit var getAccountButton: Button
    lateinit var progressBar: ProgressBar


    lateinit var presenter: MyAccountPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dependencyInjection()
        presenter = MyAccountPresenter(this, repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        getAccountButton.setOnClickListener {
            presenter.getCharacters("nzaka")
        }
        getItemsButton.setOnClickListener {
            presenter.getItems()
        }
//        presenter.onBind()
    }

    override fun showProgressBar(show: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCharacterList(characters: List<CharacterDb>) {
        val builder = StringBuilder()
        for (item in characters) {
            builder.append("${item.characterName}\n")
        }
        textView.text = builder.toString()
    }

    override fun showCharacterWindow(items: CharacterItemsDb) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError() {
        textView.text = "Error"
    }

    private fun initViews() {
        textView = textview_mafrag
        getItemsButton = button_get_items_mafrag
        getAccountButton = button_get_account_mafrag
        progressBar = progressBar_mafrag
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
            MyAccountFragment()
    }
}
