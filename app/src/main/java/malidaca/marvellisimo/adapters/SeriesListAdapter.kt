package malidaca.marvellisimo.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.series_list_view_card.view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.activities.ItemClickListener
import malidaca.marvellisimo.activities.SeriesDetailsActivity
import malidaca.marvellisimo.models.Series

class SeriesListAdapter(private var series: List<Series>, private val context: Context) : RecyclerView.Adapter<ViewHolderTwo>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolderTwo {
        return ViewHolderTwo(LayoutInflater.from(context).inflate(R.layout.series_list_view_card, p0, false))
    }

    override fun getItemCount(): Int {
        return series.size
    }

    override fun onBindViewHolder(holder: ViewHolderTwo, position: Int) {
        createImage(series[position], holder)

        holder.setOnItemClickListener(object : ItemClickListener {
            override fun onCustomClickListener(view: View, pos: Int) {
                val intent = Intent(context, SeriesDetailsActivity::class.java)
                intent.putExtra("id", series[position].id)
                context.startActivity(intent)
            }
        })
    }

    fun createImage(series: Series, holder: ViewHolderTwo) {
        var url = "${series.thumbnail.path}/portrait_fantastic.${series.thumbnail.extension}"
        url = url.replace("http", "https")
        Picasso.get().load(url).fit().into(holder.img)
    }

    fun addItems(newItems: List<Series>) {
        series = series + newItems
        notifyDataSetChanged()
    }
}

class ViewHolderTwo(itemViewSeries: View) : RecyclerView.ViewHolder(itemViewSeries), View.OnClickListener {
    val img = itemViewSeries.series_img!!
    private var itemClickListener: ItemClickListener? = null

    init {
        itemViewSeries.setOnClickListener(this)
    }

    fun setOnItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onClick(v: View?) {
        this.itemClickListener!!.onCustomClickListener(v!!, adapterPosition)
    }

}