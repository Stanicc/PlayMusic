package stanic.playmusic.controller

import android.app.Activity
import android.media.MediaPlayer
import kotlinx.coroutines.*
import stanic.playmusic.adapter.model.MusicModel

class MusicController(val activity: Activity) {

    val player = MediaPlayer()
    var musics = ArrayList<MusicModel>()

    var stopped = false
    var playing: MusicModel? = null

    fun play(music: MusicModel) {
        stopped = false

        player.reset()
        player.setDataSource(music.location)
        player.prepareAsync()
        player.setOnPreparedListener {
            it.setOnCompletionListener {
                if (playing != null && !stopped) playNext(playing!!)
            }

            it.start()
            GlobalScope.launch { thread() }
        }

        playing = music
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

    fun playNext(playing: MusicModel) {
        val next = musics.getOrNull(musics.indexOf(playing) + 1) ?: musics[0]

        stop()
        play(next)
    }

    private suspend fun thread() = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            while (player.isPlaying) {
                delay(1000)
                println(player.duration)
            }

            cancel()
        }
    }

    companion object {
        lateinit var INSTANCE: MusicController
    }

}

fun getMusicController() = MusicController.INSTANCE