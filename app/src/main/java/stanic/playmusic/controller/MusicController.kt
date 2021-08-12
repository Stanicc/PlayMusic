package stanic.playmusic.controller

import android.app.Activity
import android.graphics.Color
import android.media.MediaPlayer
import android.view.View
import kotlinx.android.synthetic.main.music_schema.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import stanic.playmusic.model.Track
import kotlin.coroutines.suspendCoroutine

class MusicController(val activity: Activity? = null) {

    val player = MediaPlayer()
    var tracks = ArrayList<Track>()

    var stopped = true
    var playing: Track? = null

    suspend fun <T> play(music: Track) = suspendCoroutine<T> { continuation ->
        stop()
        stopped = false
        playing = music

        player.setDataSource(music.location)
        player.prepareAsync()
        player.setOnPreparedListener {
            it.setOnCompletionListener {
                if (playing != null && !stopped) playNext(playing!!)
            }

            thread()
            it.start()

            continuation.resumeWith(Result.success(music as T))
        }
    }

    fun stop() {
        stopped = true

        player.stop()
        player.reset()
    }

    fun pause() {
        player.pause()
    }

    fun resume() {
        player.start()
        GlobalScope.launch { thread() }
    }

    fun addToQueue() {

    }

    fun removeFromQueue() {

    }

    fun playNext(playingNow: Track) {
        val next = tracks.getOrNull(tracks.indexOf(playingNow) + 1) ?: tracks[0]

        playing?.holder?.itemView?.run {
            music_playButton.visibility = View.VISIBLE
            music_stopButton.visibility = View.GONE
            music_title.setTextColor(Color.parseColor("#FFFFFF"))
        }
        playingNow.holder?.itemView?.run {
            music_playButton.visibility = View.VISIBLE
            music_stopButton.visibility = View.GONE
            music_title.setTextColor(Color.parseColor("#FFFFFF"))
        }

        stop()
        GlobalScope.launch { play(next) }
    }

    private fun thread() = GlobalScope.launch {
        while (player.isPlaying) delay(1000)

        cancel()
    }

    companion object {
        lateinit var INSTANCE: MusicController
    }

}

fun getMusicController() = MusicController.INSTANCE