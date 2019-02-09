package malidaca.marvellisimo.activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_inbox.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.MessageAdapter
import malidaca.marvellisimo.models.Message
import malidaca.marvellisimo.services.FireBaseService
import malidaca.marvellisimo.utilities.ActivityHelper

class InboxActivity : AppCompatActivity() {

    private lateinit var activityHelper: ActivityHelper
    private lateinit var toolbar: android.support.v7.widget.Toolbar
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: MessageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        activityHelper = ActivityHelper()
        initToolbar()
        val viewManager = LinearLayoutManager(this)
        val fab: FloatingActionButton = findViewById(R.id.fab)
 /*       FireBaseService.getMessages()
                .observeOn(Schedulers.io())
                .subscribe { data ->
                    var messages = mutableListOf<Message>()
                    fab.backgroundTintList = ColorStateList.valueOf(Color.RED)
                    for (item in data.children)
                        messages.add(data.getValue(Message::class.java)!!)
                    viewAdapter = MessageAdapter(messages, this)
                    recyclerView = findViewById<RecyclerView>(R.id.inbox_recycler_view).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                }
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                FireBaseService.signOut()
                activityHelper.changeActivity(this, LoginActivity::class.java)
                finish()
            }
            R.id.favorite_characters -> {
            }
            R.id.favorite_series -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        setClickListener()
    }

    private fun setClickListener() {
        homeButton4.setOnClickListener {
            activityHelper.changeActivity(this, MenuActivity::class.java)
        }
    }
}