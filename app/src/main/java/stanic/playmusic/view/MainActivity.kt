package stanic.playmusic.view

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.SparseArray
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_main.*
import stanic.playmusic.R
import stanic.playmusic.view.fragment.MusicsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        //Floating button configuration
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "This is to open the playing music tab", Snackbar.LENGTH_LONG)
                .setAction("Later...", null).show()
        }

        //NavigationDrawer configuration
        val drawerLayout = drawer_layout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_musics
            ), drawerLayout
        )

        //Register the buttons events
        registerButtonsEvents()

        val ytLink = "link"

        println("Starting the download of $ytLink")
        object : YouTubeExtractor(this) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                println("Extract complete")
                if(ytFiles != null) download(videoMeta!!, ytFiles[0])
            }
        }.extract(ytLink, true, false)
    }

    fun download(videoMeta: VideoMeta, ytFile: YtFile) {
        val fileName = "${videoMeta.author} - ${videoMeta.title}.mp3"
        println("Starting the download")

        val uri = Uri.parse(ytFile.url)
        val request = DownloadManager.Request(uri)
        request.setTitle(fileName.replace(".mp3", ""))

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, fileName)

        (getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
        println("Done")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun registerButtonsEvents() {
        menuButton.setOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }

        musicsButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentLayout, MusicsFragment())
            transaction.commit()
        }
    }
}