package malidaca.marvellisimo.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import malidaca.marvellisimo.R

import malidaca.marvellisimo.models.User
import malidaca.marvellisimo.utilities.SnackbarManager

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var mUser: FirebaseUser? = null

    private lateinit var view: View
    private lateinit var snackbarManager: SnackbarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        register_button.setOnClickListener(this)

        view = findViewById(android.R.id.content)
        snackbarManager = SnackbarManager()

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        mUser = auth.currentUser
    }

    override fun onPause() {
        if (mUser != null)
            database.child("users").child(mUser!!.uid).child("online").setValue(false)
        super.onPause()
    }

    override fun onResume() {
        if (mUser != null)
            database.child("users").child(mUser!!.uid).child("online").setValue(true)
        super.onResume()
    }

    private fun createAccount(email: String, password: String, firstName: String, lastName: String) {
        if(email.isNotBlank() && password.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank()) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            writeNewUser(firstName, lastName, user?.email!!)
                            val intent = Intent(this@RegisterActivity, MenuActivity::class.java)
                            startActivity(intent)
                        } else {
                            snackbarManager.createSnackbar(view, resources.getString(R.string.registration_failed), Color.RED)
                        }
                    }
        } else {
            snackbarManager.createSnackbar(view, resources.getString(R.string.registration_failed_fields_missing), Color.RED)
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.register_button -> createAccount(register_email.text.toString(), register_password.text.toString(), register_firstName.text.toString(), register_lastName.text.toString())
        }
    }

    private fun writeNewUser(firstName: String, lastName: String, email: String) {
        val user = User(email, firstName, lastName)
        database.child("users").child(mUser!!.uid).setValue(user)
        database.child("users").child(mUser!!.uid).child("online").setValue(true)
    }
}
