package stanic.playmusic.service.model

import java.io.Serializable

class Snippet(
    var channelID: String,
    var title: String,
    var description: String,
    val thumbnails: Thumbnail
) : Serializable