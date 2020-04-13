package stanic.playmusic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.music_schema.view.*
import stanic.playmusic.R
import stanic.playmusic.model.MusicModel

class MusicsAdapter(
    var context: Context,
    var musics: ArrayList<MusicModel>
) : RecyclerView.Adapter<MusicsAdapter.ViewHolder>()  {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.music_image

        val title = view.music_title
        val author = view.music_author
        val duration = view.music_duration

        val play = view.music_playButton
        val stop = view.music_stopButton
    }

    lateinit var buttonClickListener: ButtonClickListener

    interface ButtonClickListener {
        fun onClick(button: ImageButton, view: View, music: MusicModel, position: Int, holder: ViewHolder)
    }

    fun setOnClickListener(listener: ButtonClickListener) {
        this.buttonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.music_schema, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = musics.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = musics[position]
        holder.title.text = music.title
        holder.author.text = music.author
        holder.duration.text = music.duration.toString()

        holder.play.setOnClickListener { buttonClickListener.onClick(holder.play, it, music, position, holder) }
        holder.stop.setOnClickListener { buttonClickListener.onClick(holder.stop, it, music, position, holder) }

        holder.stop.visibility = View.GONE
    }

}