package stanic.playmusic.utils

fun convertTime(time: Long): String {
    val varSeconds = (time / 1000L) % 60L
    val varMinutes = (time / 60000L) % 60L
    val varHours = (time / 3600000L) % 24L
    val varDays = time / (60*60*24*1000)

    val seconds = varSeconds.toString().replace("-".toRegex(), "")
    val minutes = varMinutes.toString().replace("-".toRegex(), "")
    val hours = varHours.toString().replace("-".toRegex(), "")
    val days = varDays.toString().replace("-".toRegex(), "")

    if (days == "0" && hours == "0" && minutes == "0") {
        return "0:${if (seconds.length == 1) "0$seconds" else seconds}"
    }
    if (days == "0" && hours == "0") {
        return "$minutes:${if (seconds.length == 1) "0$seconds" else seconds}"
    }
    return if (days == "0") {
        "$hours:$minutes:${if (seconds.length == 1) "0$seconds" else seconds}"
    } else "$days:$hours:$minutes:${if (seconds.length == 1) "0$seconds" else seconds}"
}