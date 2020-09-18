package stanic.playmusic.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_download.view.*
import stanic.playmusic.R
import stanic.playmusic.view.YoutubeViewerActivity

class DownloadFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_download, container, false).apply {
            youtube_download.setOnClickListener {
                val intent = Intent(this@DownloadFragment.context, YoutubeViewerActivity::class.java)
                startActivity(intent)
            }

            /**
            progressBar_download.visibility = View.GONE
            progressBar_download_seek.visibility = View.GONE
            download_percent.visibility = View.GONE

            download_button.setOnClickListener {
                val link = download_link

                if (link.text.isEmpty() || !link.text.toString().startsWith("http")) Toast.makeText(requireContext(), "Você deve colocar um link válido", Toast.LENGTH_SHORT).show()
                else {
                    download_text.text = "Download em andamento"
                    download_button.visibility = View.GONE
                    download_link.visibility = View.GONE

                    progressBar_download.visibility = View.VISIBLE
                    progressBar_download_seek.visibility = View.VISIBLE
                    download_percent.visibility = View.VISIBLE

                    DownloadManager(this, requireActivity()).downloadMusic(link.text.toString())
                }
            }
            **/
        }
    }

}