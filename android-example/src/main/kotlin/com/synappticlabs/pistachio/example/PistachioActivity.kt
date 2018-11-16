package com.synappticlabs.pistachio.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.synappticlabs.pistachio.*

class PistachioActivity : AppCompatActivity() {
    var accountView: AccountView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pistachio)

        val accountName: EditText = findViewById(R.id.account_name_edit_text)
        val addBtn: Button = findViewById(R.id.account_add_button)
        addBtn.setOnClickListener {
            val name = accountName.text.toString()
            if (name.isEmpty()) { return@setOnClickListener }
            val addCommand = AddAccountCommand(accountName = name)
            store.dispatch(addCommand)

            accountName.text = null
        }
        val accountTotal: TextView = findViewById(R.id.account_total_text_view)

        val accountView = AccountView()
        val accountAdapter = AccountAdapter(accountView)
        accountView.listener = object: StoreViewListener {
            override fun onViewReady(view: StoreView) {
                updateViews()
            }

            override fun onViewChanged(view: StoreView) {
                updateViews()
            }

            fun updateViews() {
                accountTotal.text = accountView.count.toString()
                accountAdapter.notifyDataSetChanged()
            }
        }
        val accountLayoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.accounts_recycler_view).apply {
            adapter = accountAdapter
            layoutManager = accountLayoutManager
            addItemDecoration(DividerItemDecoration(this@PistachioActivity, DividerItemDecoration.VERTICAL))
        }

        store.registerView(accountView)
    }

    override fun onDestroy() {
        super.onDestroy()
        accountView?.let { store.unregisterView(it) }
    }
}

class AccountView: StoreView() {
    var count: Int = 0
    var accounts: ArrayList<Account> = ArrayList()

    override fun initialize(repositories: Map<String, Repository<*>>) {
        rebuild(repositories)
    }

    override fun update(repositories: Map<String, Repository<*>>, changeSet: ChangeList) {
        accounts.clear()
        rebuild(repositories)
    }

    private fun rebuild(repositories: Map<String, Repository<*>>) {
        val accountRepo = repositories[accountRepoName] as? AccountRepository ?: return
        accountRepo.scan { account ->
            accounts.add(account)
        }
        count = accounts.size
    }
}

class AccountAdapter(private val accountView: AccountView): RecyclerView.Adapter<AccountAdapter.ViewHolder>() {

    class ViewHolder(val view: View, val textView: TextView): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.account_recycler_view_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.accounts_recycler_view_item_name)

        return ViewHolder(view, textView)
    }

    override fun getItemCount(): Int {
        return accountView.count
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val account = accountView.accounts[position]
        holder.textView.text = account.name
    }
}