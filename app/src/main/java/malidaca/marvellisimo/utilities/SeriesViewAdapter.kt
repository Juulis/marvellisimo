package malidaca.marvellisimo.utilities
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import malidaca.marvellisimo.R
import malidaca.marvellisimo.models.Series

class SeriesViewAdapter(private val context: Context, private val series: Array<Series>) :
        RecyclerView.Adapter<SeriesViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        private var clickListener: ItemClickListener? = null
        val myTextView: TextView

        init {
            myTextView = itemView.findViewById(R.id.my_text_view)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {

            clickListener?.onItemClick(v, adapterPosition)
        }

        fun setClickListener(itemClickListener: ItemClickListener){
            clickListener = itemClickListener
        }
    }

    interface ItemClickListener{

        fun onItemClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.my_text_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.myTextView.text = series[position].title
    }

    override fun getItemCount() = series.size
}