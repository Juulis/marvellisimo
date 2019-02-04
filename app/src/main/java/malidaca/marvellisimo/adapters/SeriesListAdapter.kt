package malidaca.marvellisimo.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.series_list_view_card.view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.activities.ItemClickListener
import malidaca.marvellisimo.models.Series

class SeriesListAdapter(private var series: List<Series>, private val context: Context): RecyclerView.Adapter<ViewHolderTwo>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolderTwo {
        return ViewHolderTwo(LayoutInflater.from(context).inflate(R.layout.series_list_view_card, p0, false))
    }

    override fun getItemCount(): Int {
        return series.size
    }

    override fun onBindViewHolder(holder: ViewHolderTwo, position: Int) {
        val url = "${series[position].thumbnail.path}/portrait_fantastic.${series[position].thumbnail.extension}"

        var split1 =  url.subSequence(0,4)
        var split2 =  url.subSequence(4, url.length)
        val newUrl = "${split1}s$split2"

        Picasso.get().load(newUrl).fit().into(holder.img)

        holder.setOnItemClickListener(object : ItemClickListener {
            override fun onCustomClickListener(view: View, pos: Int) {
              /*  val intent = Intent(context, CharacterActivity::class.java)
                intent.putExtra("itemId",series[position].id )
                context.startActivity(intent)*/
            }

        })
    }

    fun addItems(newItems:List<Series>) {
        series = series+newItems
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