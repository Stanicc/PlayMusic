package stanic.playmusic.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import stanic.playmusic.controller.MusicController
import stanic.playmusic.view.fragment.DownloadFragment
import stanic.playmusic.view.fragment.MusicsFragment

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

        MusicController.INSTANCE = MusicController(this)
        YoutubeDL.getInstance().init(applicationContext)

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

    private fun registerButtonsEvents() {
        menuButton.setOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }

        musicsButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentLayout, MusicsFragment())
            transaction.commit()
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