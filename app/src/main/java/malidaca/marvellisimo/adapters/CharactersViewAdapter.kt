package malidaca.marvellisimo.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import malidaca.marvellisimo.R
import malidaca.marvellisimo.activities.CharacterActivity
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.models.Item

class CharactersViewAdapter(private val characters: Array<Character>, private val context: Context) : RecyclerView.Adapter<CharactersViewAdapter.ViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.series_view, parent, false)
        view.setOnClickListener(this)
        return CharactersViewAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tag = characters[position].id
        holder.myTextView.text = characters[position].name
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onClick(v: View?) {
        val id = v!!.tag as Int
        val intent = Intent(context, CharacterActivity::class.java)
        intent.putExtra("itemId", id)
        context.startActivity(intent)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myTextView = itemView.findViewById(R.id.title) as TextView
    }

}