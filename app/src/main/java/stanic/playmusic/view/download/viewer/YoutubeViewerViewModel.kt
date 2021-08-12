package stanic.playmusic.view.download.viewer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class YoutubeViewerViewModel : ViewModel() {

    var downloadProgress = MutableLiveData(0.0f)

    fun setDownloadProgress(progress: Float) {
        downloadProgress.postValue(progress)
    }

}