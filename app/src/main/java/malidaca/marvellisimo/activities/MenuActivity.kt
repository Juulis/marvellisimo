package malidaca.marvellisimo.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_menu.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.services.FireBaseService
import malidaca.marvellisimo.utilities.ActivityHelper

class MenuActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var topToolbar: android.support.v7.widget.Toolbar
    private lateinit var activityHelper: ActivityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        topToolbar = findViewById(R.id.top_toolbar)
        setSupportActionBar(topToolbar)
        activityHelper = ActivityHelper()

        menu_button_characters.setOnClickListener(this)
        menu_button_series.setOnClickListener(this)
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

    override fun onClick(v: View) {
        val i = v.id
        when(i) {
            R.id.menu_button_characters -> {
                activityHelper.changeActivity(this, CharacterListActivity::class.java)
            }
            R.id.menu_button_series -> {
                activityHelper.changeActivity(this, SeriesActivity::class.java)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> {
                FireBaseService.signOut()
                activityHelper.changeActivity(this, LoginActivity::class.java)
                finish()
            }
            R.id.favorite_characters -> {}
            R.id.favorite_series -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(isTaskRoot) {
            FireBaseService.signOut()
        }
        super.onBackPressed()
    }
}
