package stanic.playmusic.view.fragment

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_musics.view.*
import kotlinx.coroutines.*
import stanic.playmusic.R
import stanic.playmusic.adapter.MusicsAdapter
import stanic.playmusic.model.MusicModel

class MusicsFragment : Fragment() {

    private val musics = ArrayList<MusicModel>()
    private val player = MediaPlayer()
    private val handler = Handler()

    lateinit var musicsList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_musics, container, false).apply {
            menuButtonMusics.setOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }

            musicsList = music_recycler

            loadMusics()

            val layoutMusicsAdapter = LinearLayoutManager(context!!)
            val adapter = MusicsAdapter(context!!, musics)

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

    @SuppressLint("InlinedApi")
    fun loadMusics() {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC
        val cursor = context!!.contentResolver.query(uri, null, selection, null, null) ?: return

        while (cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
            val author = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
            val location = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.path))

            musics.add(MusicModel(title, author, duration.toLong(), location))
        }

        cursor.close()
    }

}