package stanic.playmusic.view

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
import stanic.playmusic.service.model.Item
import stanic.playmusic.service.model.Result
import stanic.playmusic.utils.YoutubeConfig
import stanic.playmusic.view.MainActivity.Companion.retrofit

class YoutubeDownloaderActivity : AppCompatActivity() {

    lateinit var resultList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_downloader)

        searchYoutube_button.setOnClickListener {
            val search = searchYoutube_content

            if (search.text.isEmpty()) Toast.makeText(this, "Você deve colocar algo para pesquisar!", Toast.LENGTH_SHORT).show()
            else {
                val youtubeService = retrofit.create(YoutubeService::class.java)

                youtubeService.getResult("snippet", search.text.toString(),  "relevance", YoutubeConfig.apiKey).enqueue(object : Callback<Result> {
                    override fun onResponse(call: Call<Result>, response: Response<Result>) {
                        searchYoutube_resultRecycler.visibility = View.VISIBLE
                        searchYoutube_button.visibility = View.GONE
                        searchYoutube_content.visibility = View.GONE
                        searchYoutube_text.visibility = View.GONE

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

}