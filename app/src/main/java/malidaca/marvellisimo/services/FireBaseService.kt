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
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ChildEventListener
import malidaca.marvellisimo.adapters.PeopleListAdapter
import com.google.firebase.database.ValueEventListener
import io.realm.Realm


object FireBaseService {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mUser: FirebaseUser? = null
    private var snackBarManager: SnackbarManager = SnackbarManager()
    private lateinit var userDataRef: DatabaseReference
    private val activityHelper = ActivityHelper()
    //var firebaseUsers: MutableMap<String, User> = mutableMapOf()

    fun toggleOnline(status: Boolean) {
        if (mUser != null)
            userDataRef.child("online").setValue(status)
    }

    fun signIn(email: String, password: String, context: Context, view: View) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in mUser's information
                        mUser = auth.currentUser
                        userDataRef = database.child("users").child(mUser!!.uid)
                        //updateUI(mUser)
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
                        mUser = auth.currentUser
                        userDataRef = database.child("users").child(mUser!!.uid)
                        writeNewUser(firstName, lastName, mUser?.email!!, mUser?.uid!!)
                        activityHelper.changeActivity(context, MenuActivity::class.java)
                    } else {
                        snackBarManager.createSnackbar(view, context.getString(R.string.registration_failed), Color.RED)
                    }
                }
                .addOnFailureListener {
                    snackBarManager.createSnackbar(view, context.getString(R.string.register_already_registered), Color.RED)
                }
    }

    private fun writeNewUser(firstName: String, lastName: String, email: String, uid: String) {
        val newUser = User(email, firstName, lastName)
        userDataRef.setValue(newUser)
        toggleOnline(true)
    }

    fun addFavorite(itemId: Int, type: String) {
        userDataRef.child("favorite$type/$itemId").setValue(true)
    }

    fun deleteFavorite(itemId: Int, type: String) {
        userDataRef.child("favorite$type/$itemId").removeValue()
    }

    fun checkIfOnline(context: Context) {
        if(mUser == null) {
            activityHelper.changeActivity(context, LoginActivity::class.java)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun updateOnlineRealtime(firebaseUsers: MutableMap<String, User>, adapter: PeopleListAdapter) {
        //firebaseUsers = mutableMapOf()

        val databaseReference = database.child("users")
        databaseReference.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val user = dataSnapshot.getValue(User::class.java)!!
                val userKey = dataSnapshot.key!!
                if(!firebaseUsers.containsKey(userKey) && user.isOnline && mUser!!.uid != userKey) {
                    firebaseUsers[userKey] = user
                    println(user)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                val user = dataSnapshot.getValue(User::class.java)!!
                val userKey = dataSnapshot.key!!
                if(!user.isOnline && mUser!!.uid != userKey) {
                    firebaseUsers.remove(userKey)
                    println(user)
                } else {
                    if(!firebaseUsers.containsKey(userKey)) {
                        firebaseUsers[userKey] = user
                        println(user)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)!!
                val userKey = dataSnapshot.key!!
                if(!user.isOnline && mUser!!.uid != userKey) {
                    firebaseUsers.remove(userKey)
                    println(user)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                println("User moved")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error HUE HUE HUE HUE")
            }
        })
    }

    fun getUserFavorites(context: Context, realm: Realm) {
        userDataRef.child("favoriteCharacters")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap in snapshot.children) {
                            RealmService.addFavorite(realm, snap.key!!.toInt(), "Characters")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println("error in db: $error")
                    }
                })
        userDataRef.child("favoriteSeries")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap in snapshot.children) {
                            RealmService.addFavorite(realm, snap.key!!.toInt(), "Series")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println("error in db: $error")
                    }
                })
    }

    fun getMessages(): io.reactivex.Observable<DataSnapshot> {
        return io.reactivex.Observable.create(ObservableOnSubscribe {

            val msgQuery = userDataRef.child("messages")
            msgQuery.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    it.onNext(dataSnapshot)
                }

                override fun onCancelled(p0: DatabaseError) {
                    println(p0)
                    it.onError(error("error getting messages"))
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
                    println(p0)
                    it.onError(error("error getting user"))
                }
            })
        })
    }

    fun writeMessage(message: Message, userId: String) {
        val newMsgRef = database.child("users").child(userId).child("messages").push()
        newMsgRef.setValue(message)
    }
}