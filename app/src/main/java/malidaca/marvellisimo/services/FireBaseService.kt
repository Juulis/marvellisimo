package malidaca.marvellisimo.services

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.activities.LoginActivity
import malidaca.marvellisimo.activities.MenuActivity
import malidaca.marvellisimo.models.User
import malidaca.marvellisimo.utilities.SnackbarManager
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ChildEventListener
import java.util.function.Predicate


object FireBaseService {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user: FirebaseUser? = null
    private var snackBarManager: SnackbarManager = SnackbarManager()
    private lateinit var userDataRef: DatabaseReference
    lateinit var firebaseUsers: MutableMap<String, User>

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

    fun addFavorite(itemId: String) {
        userDataRef.child("favoriteCharacters/$itemId").setValue(true)
    }

    fun deleteFavorite(itemId: String) {
        userDataRef.child("favoriteCharacters/$itemId").removeValue()
    }

    fun checkIfOnline(context: Context) {
        if(user == null) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun updateOnlineRealtime() {
        firebaseUsers = mutableMapOf<String, User>()

        val databaseReference = database.child("users")
        databaseReference.addChildEventListener(object : ChildEventListener {
            
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val user = dataSnapshot.getValue(User::class.java)!!
                val userKey = dataSnapshot.key!!
                if(!firebaseUsers.containsKey(user.id) && user.isOnline) {
                    firebaseUsers[userKey] = user
                    println(user)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                val user = dataSnapshot.getValue(User::class.java)!!
                val userKey = dataSnapshot.key!!
                if(!user.isOnline) {
                    firebaseUsers.remove(userKey)
                    println(user)
                } else {
                    if(!firebaseUsers.containsKey(userKey)) {
                        firebaseUsers[userKey] = user
                        println(user)
                    }
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)!!
                val userKey = dataSnapshot.key!!
                if(!user.isOnline) {
                    firebaseUsers.remove(userKey)
                    println(user)
                }
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                println("User moved")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error HUE HUE HUE HUE")
            }
        })
    }

    /*fun getUserFavorites(type: String): Observable<Array<String>> {
         return  userDataRef.child("favorite$type").orderByKey()
    }*/

}