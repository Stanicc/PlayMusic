package stanic.playmusic.service.model

class Thumbnail(
    var default: ThumbnailImpl,
    var medium: ThumbnailImpl,
    var high: ThumbnailImpl
) {

    class ThumbnailImpl(
        var url: String
    )

}