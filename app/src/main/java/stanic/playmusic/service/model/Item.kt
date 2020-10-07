package stanic.playmusic.service.model

class Item(
    var id: ItemID,
    var snippet: Snippet
) {

    class ItemID(
        var kind: String,
        var videoId: String
    )

}