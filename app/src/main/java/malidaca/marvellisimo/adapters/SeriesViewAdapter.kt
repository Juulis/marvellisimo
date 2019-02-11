package malidaca.marvellisimo.adapters
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.character_list_card.view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.activities.SeriesDetailsActivity
import malidaca.marvellisimo.models.Favorite
import malidaca.marvellisimo.models.Series

class SeriesViewAdapter(private var series: List<Series>, private val context: Context, private var favoriteList: RealmResults<Favorite>): RecyclerView.Adapter<SeriesViewAdapter.ViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.character_list_card, parent, false)
        view.setOnClickListener(this)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tag = series[position]
        holder.title.text = series[position].title
        createImage(series[position], holder)
        if (checkIfIsFavorite(series[position].id)){
            Picasso.get().load(R.drawable.thumbs_up_yes).into(holder.seriesFavoriteBtn)
        } else {holder.seriesFavoriteBtn.setImageDrawable(null)}
    }

    override fun getItemCount() = series.size

    fun addItems(list:List<Series>){
        series = list
        notifyDataSetChanged()
    }

    fun resetList(){
        series = emptyList()
        notifyDataSetChanged()
    }

    fun addFavorites(data: RealmResults<Favorite>) {
        favoriteList = data
        notifyDataSetChanged()
    }

   private fun createImage(series: Series, holder: ViewHolder){
        var url = "${series.thumbnail.path}/landscape_large.${series.thumbnail.extension}"
        url = url.replace("http", "https")
        Picasso.get().load(url).into(holder.img)
    }

    private fun checkIfIsFavorite(thisId: Int): Boolean {
        val result = favoriteList.where().equalTo("itemId", thisId).findFirst()
        if (result != null) {
            return true
        }
        return false
    }

    override fun onClick(v: View?) {
        val selected = v!!.tag as Series
        val intent = Intent(context, SeriesDetailsActivity::class.java)
        intent.putExtra("id", selected.id)
        context.startActivity(intent)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val title = itemView.name!!
        val img = itemView.img!!
        val seriesFavoriteBtn = itemView.c_l_c_favorite!!
    }
}