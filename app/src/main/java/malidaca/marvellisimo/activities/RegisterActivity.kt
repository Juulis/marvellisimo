package malidaca.marvellisimo.activities

import android.content.Intent
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

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        register_button.setOnClickListener(this)
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

    private fun createAccount(email: String, password: String, firstName: String, lastName: String) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        Log.d(RegisterActivity.TAG, "createUserWithEmail:success")
                        val user = auth.currentUser

                        writeNewUser(user?.uid!!, firstName, lastName, user.email!!)
                        val intent = Intent(this@RegisterActivity, MenuActivity::class.java)
                        startActivity(intent)
                        //updateUI(user)
                    } else {
                        Log.w(RegisterActivity.TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.register_button -> createAccount(register_email.text.toString(), register_password.text.toString(), register_firstName.text.toString(), register_lastName.text.toString())
        }
    }

    private fun writeNewUser(userId: String, firstName: String, lastName: String, email: String) {
        val user = User(userId, email, firstName, lastName)
        database.child("users").child(userId).setValue(user)
        database.child("users").child(userId).child("online").setValue(true)
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}
