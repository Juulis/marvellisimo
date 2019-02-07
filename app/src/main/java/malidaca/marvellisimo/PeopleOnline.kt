package malidaca.marvellisimo

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class PeopleOnline : Fragment() {

    companion object {
        fun newInstance() = PeopleOnline()
    }

    private lateinit var viewModel: PeopleOnlineViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.people_online_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PeopleOnlineViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
