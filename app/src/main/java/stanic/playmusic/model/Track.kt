package stanic.playmusic.model

import android.graphics.Bitmap
import stanic.playmusic.adapter.MusicsViewHolder

class Track(
    var title: String,
    var duration: Long,
    var imageBitmap: Bitmap?,
    var location: String
) {

    var holder: MusicsViewHolder? = null

}