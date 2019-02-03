package malidaca.marvellisimo.adapters
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import malidaca.marvellisimo.R
import malidaca.marvellisimo.models.Series

class SeriesViewAdapter(private val series: Array<Series>): RecyclerView.Adapter<SeriesViewAdapter.ViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.series_view, parent, false)
        view.setOnClickListener(this)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tag = series[position]
        holder.myTextView.text = series[position].title
    }

    override fun getItemCount() = series.size

    override fun onClick(v: View?) {
        val selected = v!!.tag as Series
        println("title: ${selected.title}")
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val myTextView = itemView.findViewById(R.id.title) as TextView
    }
}