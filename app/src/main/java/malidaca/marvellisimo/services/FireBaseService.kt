package malidaca.marvellisimo.services

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import malidaca.marvellisimo.R
import malidaca.marvellisimo.activities.LoginActivity
import malidaca.marvellisimo.activities.MenuActivity
import malidaca.marvellisimo.models.User
import malidaca.marvellisimo.utilities.SnackbarManager

object FireBaseService {


    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user: FirebaseUser? = auth.currentUser
    private var mUser: FirebaseUser? = null

    fun toggleOnline(status: Boolean) {
        if (user != null)
            database.child("users").child(user!!.uid).child("online").setValue(status)
    }

    fun signIn(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        user = auth.currentUser

                        database.child("users").child(user!!.uid).child("online").setValue(true)
                        //updateUI(user)
                        val intent = Intent(context, MenuActivity::class.java)
                        context.startActivity(intent)
                    } else {
//                        snackbarManager.createSnackbar(view, resources.getString(R.string.signin_failed_wrong_credentials), Color.RED)
                    }

                }
    }

    fun createUser(email: String, password: String, firstName: String, lastName: String, context: Context) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        user = null
                        user = auth.currentUser

                        writeNewUser(firstName, lastName, user?.email!!, user?.uid!!)
                        val intent = Intent(context, MenuActivity::class.java)
                        context.startActivity(intent)
                    } else {
//                        snackbarManager.createSnackbar(view, resources.getString(R.string.registration_failed), Color.RED)
                    }
                }
    }

    private fun writeNewUser(firstName: String, lastName: String, email: String, uid: String) {
        val newUser = User(email, firstName, lastName)
        database.child("users").child(uid).setValue(newUser)
        toggleOnline(true)
    }

}