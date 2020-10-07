package stanic.playmusic.service.model

class Snippet(
    var channelID: String,
    var title: String,
    var description: String,
    val thumbnails: Thumbnail
)