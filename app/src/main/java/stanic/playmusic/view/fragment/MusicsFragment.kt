package stanic.playmusic.view.fragment

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_musics.view.*
import kotlinx.coroutines.*
import stanic.playmusic.R
import stanic.playmusic.adapter.MusicsAdapter
import stanic.playmusic.model.MusicModel
import java.io.File

class MusicsFragment : Fragment() {

    private val musics = ArrayList<MusicModel>()
    private val player = MediaPlayer()

    lateinit var musicsList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_musics, container, false).apply {
            loadMusics()
            menuButtonMusics.setOnClickListener { findViewById<DrawerLayout>(R.id.drawer_layout).openDrawer(GravityCompat.START) }

            musicsList = music_recycler

            val layoutMusicsAdapter = LinearLayoutManager(requireContext())
            val adapter = MusicsAdapter(requireContext(), musics)

            musicsList.layoutManager = layoutMusicsAdapter
            musicsList.adapter = adapter

            adapter.setOnClickListener(object : MusicsAdapter.ButtonClickListener {
                override fun onClick(
                    button: ImageButton,
                    view: View,
                    music: MusicModel,
                    position: Int,
                    holder: MusicsAdapter.ViewHolder
                ) {
                    if (!player.isPlaying || button == holder.play) {
                        val runnable = Runnable {
                            player.reset()
                            player.setDataSource(music.location)
                            player.prepareAsync()
                            player.setOnPreparedListener {
                                it.start()
                                GlobalScope.launch { thread() }
                            }
                        }

                        handler.postDelayed(runnable, 100)
                        holder.stop.visibility = View.VISIBLE
                        button.visibility = View.GONE
                    } else {
                        player.stop()
                        player.reset()
                        holder.stop.visibility = View.GONE
                        holder.play.visibility = View.VISIBLE
                    }
                }
            })
        }
    }

    suspend fun thread() = coroutineScope {
        while (player.isPlaying) {
            delay(1000)
            println(player.duration)
        }

        player.reset()
    }

    fun loadMusics() {
        val directory = File(Environment.getExternalStorageDirectory(), "/PlayMusic")
        if (!directory.exists()) directory.mkdirs()

        if (directory.listFiles() != null) directory.listFiles()!!.filter { it.name.contains(".mp3") || it.name.contains(".MP3") }.forEach {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(it.absolutePath)

            musics.add(MusicModel(
                it.name.replace(".mp3", ""),
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong(),
                it.absolutePath)
            )
        }
    }

}