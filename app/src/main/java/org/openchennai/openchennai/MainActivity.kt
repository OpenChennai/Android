package org.openchennai.openchennai

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.openchennai.openchennai.menuFragment.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private val OPEN_CHENNAI_LINK = "https://openchennai.org"
    private val TWITTER_LINK = "https://twitter.com/openchennai"
    private val FACEBOOK_LINK = "https://facebook.com/openchennai"
    private val INSTAGRAM_LINK = "https://instagram.com/openchennai"
    private val YOUTUBE_LINK = "https://www.youtube.com/channel/UCZx7M37xyu0s6-7OJ2uverg"
    private val GITHUB_LINK = "https://github.com/openchennai"

    private lateinit var mMap: GoogleMap

    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        drawer_layout.openDrawer(Gravity.LEFT)

        nav_view.setNavigationItemSelectedListener(this)

        val fragment = ReportIssueFragment.newInstance()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.placeHolder, fragment)
        fragmentTransaction.commit()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Are you sure you want to exit?")
            builder.setPositiveButton("EXIT") { _, _ ->
                super.onBackPressed()
            }
            builder.setNegativeButton("NO") { _, _ ->
            }
            builder.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_report_issue -> {
                val fragment = ReportIssueFragment()
                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.placeHolder, fragment)
                fragmentTransaction.commit()
            }
            R.id.nav_leaderboard -> {
                val fragment = LeaderboardFragment()
                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.placeHolder, fragment)
                fragmentTransaction.commit()
            }
            R.id.nav_FAQ -> {
                val fragment = FAQFragment()
                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.placeHolder, fragment)
                fragmentTransaction.commit()
            }
            R.id.nav_map -> {
                mapFragment = SupportMapFragment()
                mapFragment.getMapAsync(this)
                val fragment = mapFragment
                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.placeHolder, fragment)
                fragmentTransaction.commit()
            }
            R.id.nav_website -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(OPEN_CHENNAI_LINK)))
                } catch (e: Exception) {

                }
            }
            R.id.twitter -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TWITTER_LINK)))
                } catch (e: Exception) {

                }
            }
            R.id.facebook -> {
                try {
                    applicationContext.packageManager.getPackageInfo("com.facebook.katana", 0)
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + FACEBOOK_LINK)))
                } catch (e: Exception) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_LINK)))
                }
            }
            R.id.instagram -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(INSTAGRAM_LINK)))
                } catch (e: Exception) {
                }
            }
            R.id.youtube -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_LINK)))
                } catch (e: Exception) {

                }
            }
            R.id.github -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_LINK)))
                } catch (e: Exception) {

                }
            }
            R.id.roads -> {
                loadWebFragment("$GITHUB_LINK/Roads/issues")
            }
            R.id.water -> {
                loadWebFragment("$GITHUB_LINK/Water-and-Sanitation/issues")
            }
            R.id.electricity -> {
                loadWebFragment("$GITHUB_LINK/Electricity/issues")
            }
            R.id.garbage -> {
                loadWebFragment("$GITHUB_LINK/Garbage/issues")
            }
            R.id.public_transport -> {
                loadWebFragment("$GITHUB_LINK/Public-Transport/issues")
            }
            R.id.traffic -> {
                loadWebFragment("$GITHUB_LINK/Traffic/issues")
            }
            R.id.trees -> {
                loadWebFragment("$GITHUB_LINK/Trees/issues")
            }
            R.id.parks -> {
                loadWebFragment("$GITHUB_LINK/Parks-and-Playgrounds/issues")
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadWebFragment(url: String) {
        val fragment = WebFragment.newInstance(url, "")
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.placeHolder, fragment)
        fragmentTransaction.commit()
    }
}
