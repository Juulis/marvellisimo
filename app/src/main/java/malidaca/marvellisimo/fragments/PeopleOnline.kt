package malidaca.marvellisimo.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.people_online_fragment.view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.PeopleListAdapter
import malidaca.marvellisimo.models.User
import malidaca.marvellisimo.services.FireBaseService


class PeopleOnline : Fragment() {
    var names: MutableMap<String, User> = mutableMapOf()
    lateinit var firebaseUsers: MutableMap<String, User>
    lateinit var adapter: PeopleListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.people_online_fragment, container, false)
        val peopleOnline = rootView.people_online_list
        peopleOnline.layoutManager = LinearLayoutManager(activity)
        adapter = PeopleListAdapter(names)
        peopleOnline.adapter = adapter
        peopleOnline.itemAnimator = DefaultItemAnimator()
        addNames(names)
        return rootView
    }

    fun addNames(firebaseUser: MutableMap<String, User>) {
        FireBaseService.updateOnlineRealtime(firebaseUser, adapter)
    }

}
