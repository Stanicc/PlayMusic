package stanic.playmusic.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.music_schema.view.*
import stanic.playmusic.R
import stanic.playmusic.controller.getMusicController
import stanic.playmusic.model.Track
import stanic.playmusic.utils.convertTime

class MusicsAdapter(
    private var tracks: ArrayList<Track>
) : RecyclerView.Adapter<MusicsViewHolder>()  {

    private lateinit var buttonClickListener: ButtonClickListener

    interface ButtonClickListener {
        fun onClick(button: ImageButton, view: View, music: Track, position: Int, holder: MusicsViewHolder)
    }

    fun setOnClickListener(listener: ButtonClickListener) {
        this.buttonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.music_schema, parent, false)
        return MusicsViewHolder(view)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: MusicsViewHolder, position: Int) {
        val music = tracks[position]
        val controller = getMusicController()

        holder.itemView.apply {
            music.holder = holder

            music_title.text = music.title
            music_duration.text = convertTime(music.duration)
            music_image.setImageBitmap(music.imageBitmap)

            music_playButton.setOnClickListener { buttonClickListener.onClick(music_playButton, this, music, position, holder) }
            music_stopButton.setOnClickListener { buttonClickListener.onClick(music_stopButton, this, music, position, holder) }

            if (controller.playing?.location == music.location) {
                music_stopButton.visibility = View.VISIBLE
                music_playButton.visibility = View.GONE

                music_title.setTextColor(Color.parseColor("#00FF0D"))
                controller.playing!!.holder = holder
            } else music_stopButton.visibility = View.GONE
        }
    }

}