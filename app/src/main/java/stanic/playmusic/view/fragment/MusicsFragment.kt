package stanic.playmusic.view.fragment

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_musics.view.*
import stanic.playmusic.R
import stanic.playmusic.adapter.MusicsAdapter
import stanic.playmusic.adapter.model.MusicModel
import stanic.playmusic.controller.getMusicController
import stanic.playmusic.view.MainActivity
import java.io.File

class MusicsFragment : Fragment() {

    lateinit var musicsList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_musics, container, false).apply {
            loadMusics()

            menuButtonMusics.setOnClickListener {
                requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
            }

            musicsList = music_recycler

            val layoutMusicsAdapter = LinearLayoutManager(requireContext())
            val adapter = MusicsAdapter(requireContext(), getMusicController().musics)

            musicsList.layoutManager = layoutMusicsAdapter
            musicsList.adapter = adapter

            val controller = getMusicController()

            adapter.setOnClickListener(object : MusicsAdapter.ButtonClickListener {
                override fun onClick(
                    button: ImageButton,
                    view: View,
                    music: MusicModel,
                    position: Int,
                    holder: MusicsAdapter.ViewHolder
                ) {
                    if (!controller.player.isPlaying || button == holder.play) {
                        controller.play(music)

                        holder.stop.visibility = View.VISIBLE
                        button.visibility = View.GONE
                    } else {
                        controller.stop()

                        holder.stop.visibility = View.GONE
                        holder.play.visibility = View.VISIBLE
                    }
                }
            })
        }
    }

    private fun loadMusics() {
        val directory = File(Environment.getExternalStorageDirectory(), "/PlayMusic")
        if (!directory.exists()) directory.mkdirs()

        if (directory.listFiles() != null) directory.listFiles()!!.filter { it.name.contains(".mp3") || it.name.contains(".MP3") }.forEach {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(it.absolutePath)

            val musics = ArrayList<MusicModel>()

            musics.add(MusicModel(
                it.name.replace(".mp3", ""),
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong(),
                it.absolutePath)
            )

            getMusicController().musics = musics
        }
    }

}