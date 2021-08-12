package stanic.playmusic

import android.app.Application
import android.media.MediaMetadataRetriever
import android.os.Environment
import com.yausername.youtubedl_android.YoutubeDL
import stanic.playmusic.adapter.model.MusicModel
import stanic.playmusic.controller.MusicController
import stanic.playmusic.controller.getMusicController
import java.io.File
import java.io.FileFilter

class PlayMusicApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MusicController.INSTANCE = MusicController()
        loadMusics()

        YoutubeDL.getInstance().init(applicationContext)
    }

    private fun loadMusics() {
        val musics = ArrayList<MusicModel>()
        val directory = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath, "/tracks")
        if (!directory.exists()) directory.mkdirs()

        directory.listFiles(FileFilter { it.name.contains(".mp3") || it.name.contains(".MP3") })?.toList()?.forEach {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(it.absolutePath)

            musics.add(
                MusicModel(
                    it.nameWithoutExtension,
                    mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L,
                    it.absolutePath)
            )
        }

        getMusicController().musics = musics
    }

}