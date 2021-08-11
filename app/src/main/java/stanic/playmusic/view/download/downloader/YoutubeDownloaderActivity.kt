package stanic.playmusic.view.download.downloader

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.activity_youtube_downloader.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import stanic.playmusic.R
import stanic.playmusic.adapter.ResultAdapter
import stanic.playmusic.service.YoutubeService
import stanic.playmusic.service.config.YoutubeConfig
import stanic.playmusic.service.model.Item
import stanic.playmusic.service.model.Result
import stanic.playmusic.view.MainActivity.Companion.retrofit
import stanic.playmusic.view.download.viewer.YoutubeViewerActivity

class YoutubeDownloaderActivity : AppCompatActivity() {

    lateinit var resultList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_downloader)

        searchYoutube_button.setOnClickListener { registerButtonListeners(false) }
        searchDownloader_Button.setOnClickListener { registerButtonListeners(true) }
    }

    private fun registerButtonListeners(downloader: Boolean) {
        val search = if (!downloader) searchYoutube_content else searchDownloader_content

        if (search.text.isEmpty()) Toast.makeText(this, "Você deve colocar algo para pesquisar!", Toast.LENGTH_SHORT).show()
        else {
            val youtubeService = retrofit.create(YoutubeService::class.java)

            if (downloader) {
                resultList.Recycler().clear()
                searchYoutube_resultRecycler.visibility = View.GONE
            }

            searchYoutube_button.visibility = View.GONE
            searchYoutube_progressBar.visibility = View.VISIBLE

            youtubeService.getResult("snippet", search.text.toString(),  "relevance", YoutubeConfig.apiKey).enqueue(object : Callback<Result> {
                override fun onResponse(call: Call<Result>, response: Response<Result>) {
                    searchYoutube_resultRecycler.visibility = View.VISIBLE
                    searchDownloader_layout.visibility = View.VISIBLE

                    searchYoutube_content.visibility = View.GONE
                    searchYoutube_button.visibility = View.GONE
                    searchYoutube_text.visibility = View.GONE
                    searchYoutube_progressBar.visibility = View.GONE

                    resultList = searchYoutube_resultRecycler

                    val layoutResultManager = LinearLayoutManager(this@YoutubeDownloaderActivity)
                    val adapter = ResultAdapter(this@YoutubeDownloaderActivity, response.body()!!.items)

                    resultList.layoutManager = layoutResultManager
                    resultList.adapter = AlphaInAnimationAdapter(adapter)

                    adapter.setOnClickListener(object : ResultAdapter.ButtonClickListener {
                        override fun onClick(
                            view: View,
                            item: Item,
                            position: Int,
                            holder: ResultAdapter.ViewHolder
                        ) {
                            val intent = Intent(this@YoutubeDownloaderActivity, YoutubeViewerActivity::class.java)
                            intent.putExtra("item", item)

                            startActivity(intent)
                        }
                    })
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

}