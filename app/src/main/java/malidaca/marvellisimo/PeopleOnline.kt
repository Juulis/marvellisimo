package malidaca.marvellisimo

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.people_online_fragment.view.*
import malidaca.marvellisimo.adapters.PeopleListAdapter
import malidaca.marvellisimo.models.User
import malidaca.marvellisimo.services.FireBaseService


class PeopleOnline : Fragment() {

    companion object {
        fun newInstance() = PeopleOnline()
    }

    var names: MutableMap<String, User> = mutableMapOf()
    lateinit var firebaseUsers: MutableMap<String, User>
    lateinit var adapter: PeopleListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.people_online_fragment, container, false)
        val peopleOnline = rootView.people_online_list
        peopleOnline.layoutManager = LinearLayoutManager(activity)
        //addNames()
        //firebaseUsers = mutableMapOf()
        adapter = PeopleListAdapter(names)
        peopleOnline.adapter = adapter
        peopleOnline.itemAnimator = DefaultItemAnimator()
        //updateOnlineRealtime()
        addNames(names)
        return rootView
    }

    fun addNames(firebaseUser: MutableMap<String, User>) {
        FireBaseService.updateOnlineRealtime(firebaseUser, adapter)
        //names = FireBaseService.firebaseUsers
        //val user = User("hej", "hej", "hej", null, null, true)
        //val user2 = User("hej1", "hej1", "hej1", null, null, true)
        //names["12345"] = user
        //names["67890"] = user2
    }

    fun updateOnlineRealtime() {
        //firebaseUsers = mutableMapOf()
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference

        val databaseReference = database.child("users")
        databaseReference.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val user = dataSnapshot.getValue(User::class.java)!!
                val userKey = dataSnapshot.key!!
                if(!firebaseUsers.containsKey(userKey) && user.isOnline) {
                    firebaseUsers[userKey] = user
                    println(user)
                }
                adapter.notifyDataSetChanged()
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
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)!!
                val userKey = dataSnapshot.key!!
                if(!user.isOnline) {
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

}
