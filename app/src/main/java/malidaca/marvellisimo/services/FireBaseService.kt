package malidaca.marvellisimo.services

import android.content.Context
import android.graphics.Color
import android.view.View
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import io.reactivex.ObservableOnSubscribe
import malidaca.marvellisimo.R
import malidaca.marvellisimo.activities.LoginActivity
import malidaca.marvellisimo.activities.MenuActivity
import malidaca.marvellisimo.models.Message
import malidaca.marvellisimo.models.User
import malidaca.marvellisimo.utilities.ActivityHelper
import malidaca.marvellisimo.utilities.SnackbarManager

object FireBaseService {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user: FirebaseUser? = null
    private var snackBarManager: SnackbarManager = SnackbarManager()
    private lateinit var userDataRef: DatabaseReference
    private val activityHelper = ActivityHelper()

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
                        activityHelper.changeActivity(context, MenuActivity::class.java)
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
                        activityHelper.changeActivity(context, MenuActivity::class.java)
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
        if (user == null) {
            activityHelper.changeActivity(context, LoginActivity::class.java)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    /*fun getUserFavorites(type: String): Observable<Array<String>> {
         return  userDataRef.child("favorite$type").orderByKey()
    }*/

    fun getMessages(): io.reactivex.Observable<DataSnapshot> {
        return io.reactivex.Observable.create(ObservableOnSubscribe {

            val msgQuery = userDataRef.child("messages")
            msgQuery.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    it.onNext(dataSnapshot)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        })
    }

    fun getUsersName(): io.reactivex.Observable<DataSnapshot> {
        return io.reactivex.Observable.create(ObservableOnSubscribe<DataSnapshot> {

            userDataRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    it.onNext(dataSnapshot)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        })
    }

    fun writeMessage(message: Message, userId: String) {
        val newMsgRef = database.child("users").child(userId).child("messages").push()
        newMsgRef.setValue(message)
    }
}