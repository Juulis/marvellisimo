package malidaca.marvellisimo.services

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import malidaca.marvellisimo.R
import malidaca.marvellisimo.activities.MenuActivity
import malidaca.marvellisimo.models.User
import malidaca.marvellisimo.utilities.SnackbarManager

object FireBaseService {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user: FirebaseUser? = auth.currentUser
    private var snackBarManager: SnackbarManager = SnackbarManager()
    private var userDataRef = database.child("users").child(user!!.uid)

    fun toggleOnline(status: Boolean) {
        if (user != null)
            userDataRef.child("online").setValue(status)
    }

    fun signIn(email: String, password: String, context: Context, view: View) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        user = auth.currentUser

                        toggleOnline(true)
                        //updateUI(user)
                        val intent = Intent(context, MenuActivity::class.java)
                        context.startActivity(intent)
                    } else {
                        snackBarManager.createSnackbar(view, context.getString(R.string.signin_failed_wrong_credentials), Color.RED)
                    }

                }
    }

    fun createUser(email: String, password: String, firstName: String, lastName: String, context: Context, view: View) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        user = null
                        user = auth.currentUser

                        writeNewUser(firstName, lastName, user?.email!!, user?.uid!!)
                        val intent = Intent(context, MenuActivity::class.java)
                        context.startActivity(intent)
                    } else {
                        snackBarManager.createSnackbar(view, context.getString(R.string.registration_failed), Color.RED)
                    }
                }
    }

    private fun writeNewUser(firstName: String, lastName: String, email: String, uid: String) {
        val newUser = User(email, firstName, lastName)
        userDataRef.child(uid).setValue(newUser)
        toggleOnline(true)
    }

    fun addFavorite(itemId: String) {
        userDataRef.child("favoriteCharacters/$itemId").setValue(true)
    }

    fun deleteFavorite(itemId: String) {
        userDataRef.child("favoriteCharacters/$itemId").removeValue()
    }

    /*fun getUserFavorites(type: String): Observable<Array<String>> {
         return  userDataRef.child("favorite$type").orderByKey()
    }*/

}