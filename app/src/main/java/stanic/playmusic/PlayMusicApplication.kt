package stanic.playmusic

import android.app.Application
import android.media.MediaMetadataRetriever
import android.os.Environment
import com.yausername.youtubedl_android.YoutubeDL
import stanic.playmusic.controller.MusicController
import stanic.playmusic.controller.getMusicController
import stanic.playmusic.model.Track
import java.io.File
import java.io.FileFilter

class PlayMusicApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MusicController.INSTANCE = MusicController()
        loadContent()

        YoutubeDL.getInstance().init(applicationContext)
    }

    private fun loadContent() {
        val tracks = ArrayList<Track>()
        val directory = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath, "/tracks")
        if (!directory.exists()) directory.mkdirs()

        directory.listFiles(FileFilter { it.name.contains(".mp3") || it.name.contains(".MP3") })?.toList()?.forEach {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(it.absolutePath)

            tracks.add(
                Track(
                    it.nameWithoutExtension,
                    mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L,
                    mediaMetadataRetriever.frameAtTime,
                    it.absolutePath)
            )
        }

        getMusicController().tracks = tracks
    }

}