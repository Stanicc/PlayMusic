package stanic.playmusic.view

import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import stanic.playmusic.R

class YoutubeViewerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    lateinit var youtubePlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_viewer)

        //youtubePlayerView = youtubePlayer
        //youtubePlayerView.initialize(youtubeAPIKey, this)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider,
        player: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        Toast.makeText(this, "Deu bomkkkkkk", Toast.LENGTH_SHORT).show()
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        result: YouTubeInitializationResult?
    ) {
        Toast.makeText(this, "Deu ruimkkkkkk", Toast.LENGTH_SHORT).show()
    }

}