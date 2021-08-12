package stanic.playmusic.view.library

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator
import kotlinx.android.synthetic.main.fragment_musics.view.*
import kotlinx.android.synthetic.main.music_schema.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import stanic.playmusic.R
import stanic.playmusic.adapter.MusicsAdapter
import stanic.playmusic.adapter.MusicsViewHolder
import stanic.playmusic.controller.MusicController
import stanic.playmusic.controller.getMusicController
import stanic.playmusic.model.Track
import stanic.playmusic.view.MainActivity

class MusicsFragment : Fragment() {

    lateinit var controller: MusicController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_musics, container, false).apply {
            menuButtonMusics.setOnClickListener {
                requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
            }

            controller = getMusicController()

            val layoutMusicsAdapter = LinearLayoutManager(requireContext())
            val adapter = MusicsAdapter(controller.tracks)

            musicsList = music_recycler
            musicsList.layoutManager = layoutMusicsAdapter
            musicsList.adapter = AlphaInAnimationAdapter(adapter)
            musicsList.itemAnimator = FadeInDownAnimator()

            setupAdapterClickListener(adapter)
        }
    }

    private fun setupAdapterClickListener(adapter: MusicsAdapter) {
        adapter.setOnClickListener(object : MusicsAdapter.ButtonClickListener {
            override fun onClick(
                button: ImageButton,
                view: View,
                music: Track,
                position: Int,
                holder: MusicsViewHolder
            ) {
                if (button == view.music_playButton) {
                    if (controller.playing != null) {
                        controller.playing?.holder?.run {
                            itemView.music_playButton.visibility = View.VISIBLE
                            itemView.music_stopButton.visibility = View.GONE

                            itemView.music_title.setTextColor(Color.parseColor("#FFFFFF"))
                        }
                    }
                    GlobalScope.launch {
                        controller.play<Track>(music).runCatching {
                            requireActivity().runOnUiThread {
                                view.music_stopButton.visibility = View.VISIBLE
                                button.visibility = View.GONE

                                view.music_title.setTextColor(Color.parseColor("#00FF0D"))
                            }
                        }
                    }
                } else {
                    controller.stop()

                    view.music_stopButton.visibility = View.GONE
                    view.music_playButton.visibility = View.VISIBLE
                    view.music_title.setTextColor(Color.parseColor("#FFFFFF"))

                    controller.playing = null
                }
            }
        })
    }

    companion object {
        lateinit var musicsList: RecyclerView
    }

}