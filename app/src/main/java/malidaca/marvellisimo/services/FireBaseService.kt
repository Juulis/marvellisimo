package malidaca.marvellisimo.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FireBaseService {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user: FirebaseUser? = auth.currentUser

   fun toggleOnline(status: Boolean) {
       if(user != null)
       database.child("users").child(user!!.uid).child("online").setValue(status)
   }

}