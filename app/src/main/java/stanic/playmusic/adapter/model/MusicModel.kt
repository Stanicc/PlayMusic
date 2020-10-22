package stanic.playmusic.adapter.model

import stanic.playmusic.adapter.MusicsAdapter

class MusicModel(
    var title: String,
    var duration: Long,
    var location: String
) {

    var holder: MusicsAdapter.ViewHolder? = null

}