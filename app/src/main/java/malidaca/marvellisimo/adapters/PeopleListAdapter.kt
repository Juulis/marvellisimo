package malidaca.marvellisimo.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.people_online_card.view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.activities.ItemClickListener
import malidaca.marvellisimo.models.Message
import malidaca.marvellisimo.models.User
import malidaca.marvellisimo.services.FireBaseService
import malidaca.marvellisimo.utilities.SnackbarManager


class PeopleListAdapter(private var names: MutableMap<String, User>, private val message: Message?) : RecyclerView.Adapter<PeopleListAdapter.ViewHolder>() {//}, View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleListAdapter.ViewHolder {

        val itemLayoutView = LayoutInflater.from(parent.context).inflate(R.layout.people_online_card, parent, false)
        //itemLayoutView.setOnClickListener(this)

        return ViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val l = names.values.toMutableList()[position]
        val firstName = l.firstName
        val lastName = l.lastName
        val text = "$firstName $lastName"
        holder.name.text = text
        val key = getUserKey(l)

        holder.setOnItemClickListener(object : ItemClickListener {
            override fun onCustomClickListener(view: View, pos: Int) {
                if (message != null){
                    FireBaseService.writeMessage(message, key)
                    SnackbarManager().createSnackbar(view,"${message.itemType} sent", R.color.colorPrimaryDark)
                }
            }
        })
    }

    private fun getUserKey(user: User): String {
        var key = ""
        for (item in names.entries) {
            if (item.value == user)
                key = item.key
        }
        return key
    }


    override fun getItemCount(): Int {
        return names.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val name = view.name
        private var itemClickListener: ItemClickListener? = null

        init {
            name.setOnClickListener(this)
        }

        fun setOnItemClickListener(itemClickListener: ItemClickListener) {
            this.itemClickListener = itemClickListener
        }

        override fun onClick(v: View?) {
            this.itemClickListener!!.onCustomClickListener(v!!, adapterPosition)
        }
    }
}


