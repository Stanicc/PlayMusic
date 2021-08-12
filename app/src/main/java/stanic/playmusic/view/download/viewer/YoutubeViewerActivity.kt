package stanic.playmusic.view.download.viewer

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_youtube_viewer.*
import stanic.playmusic.R
import stanic.playmusic.service.config.YoutubeConfig
import stanic.playmusic.service.download.YoutubeDownloadService
import stanic.playmusic.service.model.Item
import java.io.File

class YoutubeViewerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener, LifecycleOwner {

    private lateinit var lifecycleRegistry: LifecycleRegistry
    private lateinit var youtubePlayerView: YouTubePlayerView
    private lateinit var youtubeViewerViewModel: YoutubeViewerViewModel
    lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_viewer)

        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.currentState = Lifecycle.State.CREATED

        item = intent.getSerializableExtra("item") as Item? ?: return
        youtubeViewerViewModel = ViewModelProvider.NewInstanceFactory().create(YoutubeViewerViewModel::class.java)

        youtubePlayerView = youtubePlayer
        youtubePlayerView.initialize(YoutubeConfig.apiKey, this)

        item.snippet.title = item.snippet.title.replace("&quot;", "").replace("&amp;", "")
        viewer_title.text = item.snippet.title
        download_button.setOnClickListener {
            downloadButtonClickListener(item)
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    private fun downloadButtonClickListener(item: Item) {
        download_button.visibility = View.GONE

        Toast.makeText(this, "Iniciando o download", Toast.LENGTH_SHORT).show()
        playAnimation()

        youtubeViewerViewModel.downloadProgress.observe(this, {
            progressBar_download.progress = it.toInt()
            progressBar_download_seek.progress = it.toInt()
            download_percent.text = "$it%"
        })

        YoutubeDownloadService(window.decorView.rootView, youtubeViewerViewModel).downloadMusic(File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath, "/tracks"), item)
    }

    private fun playAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        progressBar_download.visibility = View.VISIBLE
        progressBar_download_seek.visibility = View.VISIBLE
        download_percent.visibility = View.VISIBLE

        progressBar_download.startAnimation(animation)
        progressBar_download_seek.startAnimation(animation)
        download_percent.startAnimation(animation)
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