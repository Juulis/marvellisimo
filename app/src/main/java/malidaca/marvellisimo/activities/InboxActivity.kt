package malidaca.marvellisimo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_inbox.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.MessageAdapter
import malidaca.marvellisimo.fragments.PeopleOnline
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
        FireBaseService.getMessages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    if (data != null) {
                        var messages = mutableListOf<Message>()
                        for (item in data.children) {
                            messages.add(item.getValue(Message::class.java)!!)
                        }
                        viewAdapter = MessageAdapter(messages, this)
                        recyclerView = findViewById<RecyclerView>(R.id.inbox_recycler_view).apply {
                            setHasFixedSize(true)
                            layoutManager = viewManager
                            adapter = viewAdapter
                        }
                    }
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onPause() {
        FireBaseService.toggleOnline(false)
        super.onPause()
    }

    override fun onResume() {
        FireBaseService.checkIfOnline(this)
        FireBaseService.toggleOnline(true)
        super.onResume()
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
            R.id.people_online -> {
                val fragment = PeopleOnline()
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .addToBackStack(null)
                        .add(R.id.fragment_container, fragment)
                        .commit()
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

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}