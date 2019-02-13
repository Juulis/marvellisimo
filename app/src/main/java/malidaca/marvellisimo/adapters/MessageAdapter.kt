package malidaca.marvellisimo.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.message_view.view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.activities.CharacterActivity
import malidaca.marvellisimo.activities.SeriesDetailsActivity
import malidaca.marvellisimo.models.Message
import malidaca.marvellisimo.utilities.ActivityHelper

class MessageAdapter( private var messages: List<Message>, private val context: Context) : RecyclerView.Adapter<MessageAdapter.ViewHolder>(){

    private val activityHelper = ActivityHelper()

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        holder.senderName.text = messages[position].sender
        holder.itemName.text = messages[position].itemName
        holder.itemName.setOnClickListener {

            val type = messages[position].itemType
            val id = messages[position].itemId
            when(type){
                context.resources.getString(R.string.menu_characters) ->
                    activityHelper.changeActivityWithExtras(context, CharacterActivity::class.java, "itemId", id)
                context.resources.getString(R.string.menu_series) ->
                    activityHelper.changeActivityWithExtras(context, SeriesDetailsActivity::class.java, "id", id)
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val senderName = itemView.senderName!!
        val itemName = itemView.itemName!!
    }

}