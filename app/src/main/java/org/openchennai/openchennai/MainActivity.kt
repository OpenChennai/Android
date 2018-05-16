package org.openchennai.openchennai

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import org.openchennai.openchennai.menuFragment.FAQFragment
import org.openchennai.openchennai.menuFragment.LeaderboardFragment
import org.openchennai.openchennai.menuFragment.MapFragment
import org.openchennai.openchennai.menuFragment.ReportIssueFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val OPEN_CHENNAI_LINK = "https://openchennai.org"
    private val TWITTER_LINK = "https://twitter.com/openchennai"
    private val FACEBOOK_LINK = "https://facebook.com/openchennai"
    private val INSTAGRAM_LINK = "https://instagram.com/openchennai"
    private val YOUTUBE_LINK = "https://www.youtube.com/channel/UCZx7M37xyu0s6-7OJ2uverg"
    private val GITHUB_LINK = "https://github.com/openchennai"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
                fragmentTransaction.replace(R.id.content_main, fragment)
                fragmentTransaction.commit()
            }
            R.id.nav_leaderboard -> {
                val fragment = LeaderboardFragment()
                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.content_main, fragment)
                fragmentTransaction.commit()
            }
            R.id.nav_FAQ -> {
                val fragment = FAQFragment()
                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.content_main, fragment)
                fragmentTransaction.commit()
            }
            R.id.nav_map -> {
                val fragment = MapFragment()
                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.content_main, fragment)
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
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$GITHUB_LINK/Roads/issues")))
                } catch (e: Exception) {
                }
            }
            R.id.water -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$GITHUB_LINK/Water-and-Sanitation/issues")))
                } catch (e: Exception) {
                }
            }
            R.id.electricity -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$GITHUB_LINK/Electricity/issues")))
                } catch (e: Exception) {
                }
            }
            R.id.garbage -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$GITHUB_LINK/Garbage/issues")))
                } catch (e: Exception) {
                }
            }
            R.id.public_transport -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$GITHUB_LINK/Public-Transport/issues")))
                } catch (e: Exception) {
                }
            }
            R.id.traffic -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$GITHUB_LINK/Traffic/issues")))
                } catch (e: Exception) {
                }
            }
            R.id.trees -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$GITHUB_LINK/Trees/issues")))
                } catch (e: Exception) {
                }
            }
            R.id.parks -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$GITHUB_LINK/Parks-and-Playgrounds/issues")))
                } catch (e: Exception) {
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
