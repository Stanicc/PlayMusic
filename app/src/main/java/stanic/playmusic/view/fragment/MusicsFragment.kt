package stanic.playmusic.view.fragment

import android.content.Intent
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
import stanic.playmusic.R
import stanic.playmusic.adapter.MusicsAdapter
import stanic.playmusic.adapter.model.MusicModel
import stanic.playmusic.controller.getMusicController
import stanic.playmusic.view.MainActivity

class MusicsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_musics, container, false).apply {
            menuButtonMusics.setOnClickListener {
                requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
            }

            val controller = getMusicController()

            requireActivity().runOnUiThread {
                musicsList = music_recycler

                val layoutMusicsAdapter = LinearLayoutManager(requireContext())
                val adapter = MusicsAdapter(requireContext(), getMusicController().musics)

                musicsList.layoutManager = layoutMusicsAdapter
                musicsList.adapter = AlphaInAnimationAdapter(adapter)
                musicsList.itemAnimator = FadeInDownAnimator()

                adapter.setOnClickListener(object : MusicsAdapter.ButtonClickListener {
                    override fun onClick(
                        button: ImageButton,
                        view: View,
                        music: MusicModel,
                        position: Int,
                        holder: MusicsAdapter.ViewHolder
                    ) {
                        if (button == holder.play) {
                            if (controller.playing != null) {
                                controller.playing!!.first.play.visibility = View.VISIBLE
                                controller.playing!!.first.stop.visibility = View.GONE
                            }
                            controller.play(music)
                            controller.playing = holder to music

                            holder.stop.visibility = View.VISIBLE
                            button.visibility = View.GONE
                        } else {
                            controller.stop()

                            holder.stop.visibility = View.GONE
                            holder.play.visibility = View.VISIBLE
                        }
                    }
                })
            }
        }
    }

    companion object {
        lateinit var musicsList: RecyclerView
    }

}