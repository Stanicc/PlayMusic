package stanic.playmusic

import android.app.Application
import android.media.MediaMetadataRetriever
import android.os.Environment
import com.yausername.youtubedl_android.YoutubeDL
import stanic.playmusic.adapter.model.MusicModel
import stanic.playmusic.controller.MusicController
import stanic.playmusic.controller.getMusicController
import java.io.File

class PlayMusicApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MusicController.INSTANCE = MusicController()
        loadMusics()

        YoutubeDL.getInstance().init(applicationContext)
    }

    private fun loadMusics() {
        val directory = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath, "/PlayMusic")
        if (!directory.exists()) directory.mkdirs()

        val musics = ArrayList<MusicModel>()

        if (directory.listFiles() != null) directory.listFiles()!!.filter { it.name.contains(".mp3") || it.name.contains(".MP3") }.forEach {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(it.absolutePath)

            musics.add(
                MusicModel(
                    it.name.replace(".mp3", ""),
                    mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L,
                    it.absolutePath)
            )
        }

        getMusicController().musics = musics
    }

}