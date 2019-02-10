package malidaca.marvellisimo.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.people_online_card.view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.activities.ItemClickListener
import malidaca.marvellisimo.models.User
import malidaca.marvellisimo.services.FireBaseService




class PeopleListAdapter(private var names: MutableMap<String, User>) : RecyclerView.Adapter<PeopleListAdapter.ViewHolder>() {//}, View.OnClickListener {
    //override fun onClick(v: View?) {
    //    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    //}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleListAdapter.ViewHolder {
        //return ViewHolder(LayoutInflater.from(context).inflate(R.layout.people_online_card, viewGroup, false))

        val itemLayoutView = LayoutInflater.from(parent.context).inflate(R.layout.people_online_card, parent,false)
        //changeData()
        //itemLayoutView.setOnClickListener(this)
        // create ViewHolder

        return ViewHolder(itemLayoutView)
    }

    /*override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setOnItemClickListener(object : ItemClickListener {
            override fun onCustomClickListener(view: View, pos: Int) {
            }
        })
    }*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val l = names.values.toMutableList()[position]
        val firstName = l.firstName
        val lastName = l.lastName
        val text = "$firstName $lastName"
        holder.name.text = text
    }



    override fun getItemCount(): Int {
        return names.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {//}, View.OnClickListener {
        val name = view.name
        private var itemClickListener: ItemClickListener? = null

        init {
            //name.setOnClickListener(this)
        }

        fun setOnItemClickListener(itemClickListener: ItemClickListener) {
            this.itemClickListener = itemClickListener
        }

        //override fun onClick(v: View?) {
        //    this.itemClickListener!!.onCustomClickListener(v!!, adapterPosition)
        //}
    }
}


