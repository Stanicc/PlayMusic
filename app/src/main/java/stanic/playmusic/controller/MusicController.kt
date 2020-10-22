package stanic.playmusic.controller

import android.app.Activity
import android.graphics.Color
import android.media.MediaPlayer
import android.view.View
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import stanic.playmusic.adapter.model.MusicModel
import kotlin.coroutines.suspendCoroutine

class MusicController(val activity: Activity) {

    val player = MediaPlayer()
    var musics = ArrayList<MusicModel>()

    var stopped = true
    var playing: MusicModel? = null

    suspend fun <T> play(music: MusicModel) = suspendCoroutine<T> { continuation ->
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

    fun playNext(playingNow: MusicModel) {
        val next = musics.getOrNull(musics.indexOf(playingNow) + 1) ?: musics[0]

        playing!!.holder!!.play.visibility = View.VISIBLE
        playing!!.holder!!.stop.visibility = View.GONE
        playing!!.holder!!.title.setTextColor(Color.parseColor("#FFFFFF"))

        playingNow.holder?.stop?.visibility = View.VISIBLE
        playingNow.holder?.title?.setTextColor(Color.parseColor("#00FF0D"))
        playingNow.holder?.play?.visibility = View.GONE

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