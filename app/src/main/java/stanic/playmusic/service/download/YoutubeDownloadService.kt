package stanic.playmusic.service.download

import android.media.MediaMetadataRetriever
import android.view.View
import android.widget.Toast
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_youtube_viewer.view.*
import stanic.playmusic.adapter.model.MusicModel
import stanic.playmusic.controller.getMusicController
import stanic.playmusic.service.model.Item
import stanic.playmusic.view.download.viewer.YoutubeViewerViewModel
import java.io.File

class YoutubeDownloadService(
    private val view: View,
    private val youtubeViewerViewModel: YoutubeViewerViewModel
) {

    fun downloadMusic(directory: File, item: Item) {
        val request = YoutubeDLRequest("https://www.youtube.com/watch?v=${item.id.videoId}")
        request.addOption("-o", directory.absolutePath + "/%(title)s.mp3")

        Observable.fromCallable {
            YoutubeDL.getInstance().execute(request) { progress, _ ->
                youtubeViewerViewModel.setDownloadProgress(progress)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    resetComponents()

                    File(directory.absolutePath + "/${item.snippet.title}.mp3").run {
                        val mediaMetadataRetriever = MediaMetadataRetriever()
                        mediaMetadataRetriever.setDataSource(absolutePath)

                        getMusicController().musics.add(
                            MusicModel(
                                nameWithoutExtension,
                                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L,
                                absolutePath)
                        )
                    }

                    Toast.makeText(view.context, "Download completo", Toast.LENGTH_SHORT).show()
                }
            ) {
                resetComponents()

                Toast.makeText(view.context, "Ocorreu um erro ao fazer o download", Toast.LENGTH_SHORT)
                    .show()
                println(it.message)
                it.printStackTrace()
            }
    }

    private fun resetComponents() {
        youtubeViewerViewModel.setDownloadProgress(0.0f)

        view.download_percent.visibility = View.GONE
        view.progressBar_download_seek.visibility = View.GONE
        view.progressBar_download.visibility = View.GONE

        view.download_button.visibility = View.VISIBLE
    }

}