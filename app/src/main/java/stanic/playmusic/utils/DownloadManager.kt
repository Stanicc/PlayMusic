package stanic.playmusic.utils

import android.app.Activity
import android.os.Environment
import android.view.View
import android.widget.Toast
import com.yausername.youtubedl_android.DownloadProgressCallback
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_download.view.*
import java.io.File

class DownloadManager(val view: View, val activity: Activity) {

    private val callback = DownloadProgressCallback { progress, _ ->
        activity.runOnUiThread {
            view.download_percent.text = "${progress}%"
            view.progressBar_download.progress = progress.toInt()
            view.progressBar_download_seek.progress = progress.toInt()
        }
    }

    fun downloadMusic(link: String) {
        val directory = File(Environment.getExternalStorageDirectory(), "/PlayMusic")
        val request = YoutubeDLRequest(link)
        request.addOption("-o", directory.absolutePath + "/%(title)s.mp3")

        Observable.fromCallable {
            YoutubeDL.getInstance().execute(request, callback)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.download_percent.text = "0.0%"
                    view.progressBar_download.progress = 0
                    view.progressBar_download_seek.progress = 0

                    view.download_percent.visibility = View.GONE
                    view.progressBar_download_seek.visibility = View.GONE
                    view.progressBar_download.visibility = View.GONE

                    view.download_button.visibility = View.VISIBLE
                    view.download_link.visibility = View.VISIBLE
                    view.download_text.text = "Coloque o link para baixar"

                    Toast.makeText(view.context, "Download completo", Toast.LENGTH_SHORT).show()
                }
            ) {
                view.download_percent.text = "0.0%"
                view.progressBar_download.progress = 0
                view.progressBar_download_seek.progress = 0

                view.download_percent.visibility = View.GONE
                view.progressBar_download_seek.visibility = View.GONE
                view.progressBar_download.visibility = View.GONE

                view.download_button.visibility = View.VISIBLE
                view.download_link.visibility = View.VISIBLE
                view.download_text.text = "Coloque o link para baixar"

                Toast.makeText(view.context, "Ocorreu um erro no download", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    fun downloadVideo() {
    }

}