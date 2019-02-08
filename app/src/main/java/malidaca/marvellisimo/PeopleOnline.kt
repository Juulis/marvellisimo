package malidaca.marvellisimo

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.people_online_fragment.view.*
import malidaca.marvellisimo.adapters.PeopleListAdapter




class PeopleOnline : Fragment() {

    companion object {
        fun newInstance() = PeopleOnline()
    }

    val names: ArrayList<String> = ArrayList()

    private lateinit var viewModel: PeopleOnlineViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.people_online_fragment, container, false)
        addNames()
        val peopleOnline = rootView.people_online_list
        peopleOnline.layoutManager = LinearLayoutManager(activity)
        val adapter = PeopleListAdapter(names)
        peopleOnline.adapter = adapter
        peopleOnline.itemAnimator = DefaultItemAnimator()
        return rootView
    }

    fun addNames() {
        names.add("Stefan")
        names.add("Karin")
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PeopleOnlineViewModel::class.java)
        // TODO: Use the ViewModel
    }*/

}
