package stanic.playmusic.view

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_youtube_viewer.*
import stanic.playmusic.R
import stanic.playmusic.service.model.Item
import stanic.playmusic.utils.DownloadManager
import stanic.playmusic.utils.YoutubeConfig

class YoutubeViewerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private lateinit var youtubePlayerView: YouTubePlayerView
    lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_viewer)

        item = intent.getSerializableExtra("item") as Item? ?: return
        youtubePlayerView = youtubePlayer
        youtubePlayerView.initialize(YoutubeConfig.apiKey, this)

        viewer_title.text = item.snippet.title.replace("&quot;", "").replace("&amp;", "")

        download_button.setOnClickListener {
            val link = "https://youtu.be/${item.id.videoId}"

            download_button.visibility = View.GONE

            Toast.makeText(this, "Iniciando o download", Toast.LENGTH_SHORT).show()

            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

            progressBar_download.visibility = View.VISIBLE
            progressBar_download_seek.visibility = View.VISIBLE
            download_percent.visibility = View.VISIBLE

            progressBar_download.startAnimation(animation)
            progressBar_download_seek.startAnimation(animation)
            download_percent.startAnimation(animation)

            DownloadManager(window.decorView.rootView, this).downloadMusic(getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS
            )!!.absolutePath, link)
        }
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider,
        player: YouTubePlayer,
        wasRestored: Boolean
    ) {
        player.setFullscreen(false)
        player.setShowFullscreenButton(false)
        player.loadVideo(item.id.videoId)
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        result: YouTubeInitializationResult?
    ) {
        Toast.makeText(this, "Não foi possível carregar o vídeo.", Toast.LENGTH_SHORT).show()
    }

}