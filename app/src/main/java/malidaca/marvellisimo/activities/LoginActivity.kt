package malidaca.marvellisimo.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import malidaca.marvellisimo.R
import android.view.View
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import android.graphics.Color
import malidaca.marvellisimo.utilities.SnackbarManager


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var user: FirebaseUser? = null

    private lateinit var view: View
    private lateinit var snackbarManager: SnackbarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_login_button.setOnClickListener(this)
        login_register_button.setOnClickListener(this)

        view = findViewById(android.R.id.content)
        snackbarManager = SnackbarManager()

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

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

    private fun signIn(email: String, password: String) {
        if(email.isNotBlank() && password.isNotBlank()) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success")
                            user = auth.currentUser

                            database.child("users").child(user!!.uid).child("online").setValue(true)
                            //updateUI(user)
                            val intent = Intent(this@LoginActivity, MenuActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.exception)
                            snackbarManager.createSnackbar(view, resources.getString(R.string.signin_failed_wronte_credentials), Color.RED)
                            //Toast.makeText(this@LoginActivity, "Authentication failed.",
                            //        Toast.LENGTH_SHORT).show()
                            //updateUI(null)
                        }

                        // ...
                    }
        } else {
            snackbarManager.createSnackbar(view , resources.getString(R.string.signin_failed_missing_fields), Color.RED)
            //Toast.makeText(this, "Not all fields are set",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.login_register_button -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
            R.id.login_login_button -> signIn(login_email.text.toString(), login_password.text.toString())
        }
    }
}
