package stanic.playmusic.service.model

import java.io.Serializable

class Thumbnail(
    var default: ThumbnailImpl,
    var medium: ThumbnailImpl,
    var high: ThumbnailImpl
) : Serializable {

    class ThumbnailImpl(
        var url: String
    ) : Serializable

}