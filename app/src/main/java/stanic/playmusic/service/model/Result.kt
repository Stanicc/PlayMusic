package stanic.playmusic.service.model

class Result(
    var regionCode: String,
    var pageInfo: Pair<String, String>,
    var items: List<Item>
)