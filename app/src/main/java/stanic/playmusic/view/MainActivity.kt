package stanic.playmusic.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_navigation.*
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import stanic.playmusic.R
import stanic.playmusic.adapter.model.MusicModel
import stanic.playmusic.controller.MusicController
import stanic.playmusic.controller.getMusicController
import stanic.playmusic.view.fragment.DownloadFragment
import stanic.playmusic.view.fragment.MusicsFragment
import java.io.File

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        //Floating button configuration
        fab.setOnClickListener { view ->
            Snackbar.make(view, "This is to open the playing music tab", Snackbar.LENGTH_LONG)
                .setAction("Later...", null).show()
        }
        fab.visibility = View.GONE

        //NavigationDrawer configuration
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_musics, R.id.nav_download
            ), drawer_layout
        )
        nav_view.setNavigationItemSelectedListener(this)

        //Register the buttons events
        registerButtonsEvents()
        checkPermissions()

        try {
            getMusicController()
        } catch (e: Exception) {
            MusicController.INSTANCE = MusicController(this)
        }
        YoutubeDL.getInstance().init(applicationContext)
        loadMusics()

        retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> startActivity(Intent(applicationContext, MainActivity::class.java))
            R.id.nav_download -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.fragmentLayout, DownloadFragment())
                transaction.commit()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun loadMusics() {
        val directory = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath, "/PlayMusic")
        if (!directory.exists()) directory.mkdirs()

        val musics = ArrayList<MusicModel>()

        if (directory.listFiles() != null) directory.listFiles()!!.filter { it.name.contains(".mp3") || it.name.contains(".MP3") }.forEach {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(it.absolutePath)

            musics.add(
                MusicModel(
                it.name.replace(".mp3", ""),
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong(),
                it.absolutePath)
            )
        }

        getMusicController().musics = musics
    }

    private fun registerButtonsEvents() {
        menuButton.setOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }

        musicsButton.setOnClickListener {
            if (getMusicController().musics.isEmpty()) Toast.makeText(
                this,
                "Você não tem músicas para listar",
                Toast.LENGTH_SHORT
            ).show()
            else {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.fragmentLayout, MusicsFragment())
                transaction.commit()
            }
        }
    }

    private fun checkPermissions() {
        when {
            /*
            READ_EXTERNAL_STORAGE
             */
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }
            /*
            WRITE_EXTERNAL_STORAGE
             */
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
            }
            /*
            INTERNET
             */
            ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.INTERNET),1)
            }
        }
    }

    companion object {
        lateinit var retrofit: Retrofit
    }

}