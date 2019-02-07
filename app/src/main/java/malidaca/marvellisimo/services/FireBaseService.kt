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
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


object FireBaseService {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user: FirebaseUser? = null
    private var snackBarManager: SnackbarManager = SnackbarManager()
    private lateinit var userDataRef: DatabaseReference

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
                        userDataRef = database.child("users").child(user!!.uid)
                        getUserFavorites(context)
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
                        user = auth.currentUser
                        userDataRef = database.child("users").child(user!!.uid)
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
        userDataRef.setValue(newUser)
        toggleOnline(true)
    }

    fun addFavorite(itemId: Int) {
        userDataRef.child("favoriteCharacters/$itemId").setValue(true)
    }

    fun deleteFavorite(itemId: Int) {
        userDataRef.child("favoriteCharacters/$itemId").removeValue()
    }

    private fun getUserFavorites(context: Context) {
        val favoriteList: MutableList<String> = arrayListOf()
        userDataRef.child("favoriteCharacters")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap in snapshot.children) {
                            favoriteList.add(snap.key!!)
                        }
                        }

                    override fun onCancelled(error: DatabaseError) {
                        println("error in db: $error")
                    }
                })

    }

}