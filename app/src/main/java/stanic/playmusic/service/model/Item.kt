package stanic.playmusic.service.model

import java.io.Serializable

class Item(
    var id: ItemID,
    var snippet: Snippet
) : Serializable {

    class ItemID(
        var kind: String,
        var videoId: String
    ) : Serializable

}