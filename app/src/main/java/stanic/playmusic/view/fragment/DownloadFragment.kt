package stanic.playmusic.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_download.view.*
import stanic.playmusic.R
import stanic.playmusic.view.YoutubeDownloaderActivity

class DownloadFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_download, container, false).apply {
            youtube_download.setOnClickListener {
                val intent = Intent(this@DownloadFragment.context, YoutubeDownloaderActivity::class.java)
                startActivity(intent)
            }
        }
    }

}