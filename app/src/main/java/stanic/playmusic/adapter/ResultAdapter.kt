package stanic.playmusic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.result_schema.view.*
import stanic.playmusic.R
import stanic.playmusic.adapter.model.MusicModel
import stanic.playmusic.service.model.Item

class ResultAdapter(
    var context: Context,
    var items: List<Item>
) : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.result_title
        val thumbnail = view.result_thumbnail
    }

    lateinit var buttonClickListener: ButtonClickListener

    interface ButtonClickListener {
        fun onClick(button: ImageButton, view: View, music: MusicModel, position: Int, holder: MusicsAdapter.ViewHolder)
    }

    fun setOnClickListener(listener: ButtonClickListener) {
        this.buttonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.result_schema, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.title.text = item.snippet.title
        Picasso.get().load(item.snippet.thumbnails.medium.url).into(holder.thumbnail)
    }

}