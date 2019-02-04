package malidaca.marvellisimo.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import malidaca.marvellisimo.R

class MenuActivity : AppCompatActivity() {
    private lateinit var characterButton: Button
    private lateinit var seriesButton: Button

    private lateinit var database: DatabaseReference
    private var user: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        initButtons()
        setClickListeners()
    }

    override fun onPause() {
        if(user != null)
            database.child("users").child(user!!.uid).child("online").setValue(false)
        super.onPause()
    }

    override fun onResume() {
        if(user != null)
            database.child("users").child(user!!.uid).child("online").setValue(true)
        super.onResume()
    }

    private fun initButtons() {
        characterButton = findViewById(R.id.menu_button_characters)
        seriesButton = findViewById(R.id.menu_button_series)
    }


    //TODO Add your own activity in your own clickListener
    fun setClickListeners() {
        characterButton.setOnClickListener {

            val intent = Intent(this, CharacterListActivity::class.java)
            startActivity(intent)
        }

        seriesButton.setOnClickListener {
            val intent = Intent(this, SeriesActivity::class.java)
            startActivity(intent)
        }
    }
}
