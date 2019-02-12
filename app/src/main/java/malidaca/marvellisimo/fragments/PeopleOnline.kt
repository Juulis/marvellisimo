package malidaca.marvellisimo.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.people_online_fragment.view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.PeopleListAdapter
import malidaca.marvellisimo.models.Message
import malidaca.marvellisimo.models.User
import malidaca.marvellisimo.services.FireBaseService


class PeopleOnline : Fragment() {
    var names: MutableMap<String, User> = mutableMapOf()
    lateinit var firebaseUsers: MutableMap<String, User>
    lateinit var adapter: PeopleListAdapter
    private var mMessage: Message? = null
    private val messageKey = "message_key"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.people_online_fragment, container, false)
        val peopleOnline = rootView.people_online_list
        peopleOnline.layoutManager = LinearLayoutManager(activity)
        mMessage = arguments?.getSerializable(messageKey) as Message? ?: null
        adapter = PeopleListAdapter(names, mMessage, context!!)
        peopleOnline.adapter = adapter
        peopleOnline.itemAnimator = DefaultItemAnimator()
        addNames(names)
        return rootView
    }

    private fun addNames(firebaseUser: MutableMap<String, User>) {
        FireBaseService.updateOnlineRealtime(firebaseUser, adapter)
    }

    companion object {
        fun newInstance(message: Message): PeopleOnline {
            val messageKey = "message_key"
            val fragment = PeopleOnline()
            val bundle = Bundle()
            bundle.putSerializable(messageKey, message)
            fragment.arguments = bundle
            return fragment
        }
    }

}
