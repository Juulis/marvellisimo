package malidaca.marvellisimo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_inbox.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.MessageAdapter
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