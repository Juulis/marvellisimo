package malidaca.marvellisimo.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import malidaca.marvellisimo.R
import com.google.firebase.auth.FirebaseUser
import android.support.annotation.NonNull
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import malidaca.marvellisimo.MainActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase




class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        //setClickLIsteners()

        auth = FirebaseAuth.getInstance()
    }

    private fun initViews() {
        email = findViewById(R.id.login_email_edittext)
        password = findViewById(R.id.login_password_edittext)
        loginButton = findViewById(R.id.login_login_button)
        registerButton = findViewById(R.id.login_register_button)

        loginButton.setOnClickListener(this)
        registerButton.setOnClickListener(this)
    }

    private fun signIn(email: String, password: String) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            //updateUI(user)
                            val database = FirebaseDatabase.getInstance()
                            val myRef = database.getReference("message")

                            myRef.setValue("Hello, World!")
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(this@LoginActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            //updateUI(null)
                        }

                        // ...
                    }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("message")

                        myRef.setValue("Hello, World!")
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        //updateUI(user)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.login_register_button -> createAccount(email.text.toString(), password.text.toString())
            R.id.login_login_button -> signIn(email.text.toString(), password.text.toString())
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}
