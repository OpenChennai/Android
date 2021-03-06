package org.openchennai.openchennai

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.openchennai.openchennai.menuFragment.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    class Reporter {
        var name: String = "";
        var link: String = "";
        var count: Int = 0;
        var rank: Int = 0;

        constructor() {}

        constructor(name: String, link: String, count: Int, rank: Int) {
            this.name = name;
            this.link = link;
            this.count = count;
            this.rank = rank;
        }
    }

    class MapIssue(var title: String, var url: String, var latLong: String) {

    }

    private val OPEN_CHENNAI_LINK = "https://openchennai.org"
    private val TWITTER_LINK = "https://twitter.com/openchennai"
    private val FACEBOOK_LINK = "https://facebook.com/openchennai"
    private val INSTAGRAM_LINK = "https://instagram.com/openchennai"
    private val YOUTUBE_LINK = "https://www.youtube.com/channel/UCZx7M37xyu0s6-7OJ2uverg"

    private val GITHUB_LINK = "https://github.com/openchennai"

    private lateinit var gMap: GoogleMap

    private lateinit var mapFragment: SupportMapFragment
    var data: JSONArray = JSONArray()
    var responsesReceived: Int = 0;

    val unsortedReps = MutableList(0) { Reporter() }

    public var reporters = MutableList(0) { Reporter() }

    val issues: MutableList<MapIssue> = ArrayList()

    companion object {
        @JvmStatic
        val repositories = arrayOf(
                "Roads",
                "Water-and-Sanitation",
                "Electricity",
                "Garbage",
                "Public-Transport",
                "Traffic",
                "Parks-and-Playgrounds",
                "Trees"
                // 'Community",
                // 'openchennai.github.io'
        );
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        drawer_layout.openDrawer(Gravity.LEFT)

        nav_view.setNavigationItemSelectedListener(this)

        val queue = Volley.newRequestQueue(this)
        val url = "https://api.github.com/repos/OpenChennai"


        for (repo in repositories) {
            val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url + "/" + repo + "/issues?state=all", null,
                    Response.Listener<JSONArray> { data ->
                        this.data = data;
                        this.processData();
                        this.responsesReceived++;
                        this.checkAndPrepareTable();

                        mapFragment = SupportMapFragment()
                        mapFragment.getMapAsync(this)
                        val fragment = mapFragment
                        val fragmentManager: FragmentManager = supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.placeHolder, fragment)
                        fragmentTransaction.commit()
                    },
                    Response.ErrorListener {
                        Toast.makeText(this, "We are some trouble fetching details", Toast.LENGTH_SHORT).show()
                    }
            )
            queue.add(jsonArrayRequest)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.setMinZoomPreference(12.0F)

        for (i in 0..issues.size - 1) {
            val splitLatLong = issues[i].latLong.split(',')
            val lat: Double = splitLatLong[0].toDouble()
            val long: Double = splitLatLong[1].toDouble()
            val latLng: LatLng = LatLng(lat, long)
            gMap.addMarker(MarkerOptions().position(latLng).title(issues[i].title).snippet(issues[i].url))
        }
        gMap.setOnInfoWindowClickListener {
            loadWebFragment(it.snippet)
        }
        gMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(13.045, 80.2200)))
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
//            R.id.nav_report_issue -> {
//                val fragment = ReportIssueFragment()
//                val fragmentManager: FragmentManager = supportFragmentManager
//                val fragmentTransaction = fragmentManager.beginTransaction()
//                fragmentTransaction.replace(R.id.placeHolder, fragment)
//                fragmentTransaction.commit()
//            }
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
//        val fragment = WebFragment.newInstance(url, "")
//        val fragmentManager: FragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.placeHolder, fragment)
//        fragmentTransaction.commit()

        val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        val customTabsIntent: CustomTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }


    fun processData() {
        for (i in 0..this.data.length() - 1) {
            val content = this.data.getJSONObject(i)
            val body: String = content.getString("body")
            val issueTitle: String = content.getString("title")
            val issueUrl: String = content.getString("html_url")
            val lines = body.split("\r\n");
            this.setLeaderboardDetails(lines);
            this.setLocations(lines, issueTitle, issueUrl)
        }
    }

    fun setLeaderboardDetails(lines: List<String>) {
        var name = "";
        var link = "";

        if (lines.size > 0) {
            if (lines[0].startsWith("Reporter: ")) {
                val reporterArr = lines[0].split("Reporter: ")
                name = reporterArr[1]
            }
        }

        if (lines.size > 1) {
            if (lines[1].startsWith("Link: ")) {
                val linkArr = lines[1].split("Link: ");
                link = linkArr[1];
            }
        }

        if (name != "") {
            var userExists = false;
            for (i in 0..this.unsortedReps.size - 1) {
                if (this.unsortedReps[i].name == name && this.unsortedReps[i].link == link) {
                    this.unsortedReps[i].count++;
                    userExists = true;
                    break;
                }
            }


            if (!userExists) {
                val reporter: Reporter = Reporter(name, link, 1, 0);
                this.unsortedReps.add(reporter);
            }
        }
    }

    fun setLocations(lines: List<String>, title: String, url: String) {
        for (i in 0..lines.size - 1) {
            if (lines[i].startsWith("Location: ")) {
                val extractedArray = lines[i].split("Location: ");
                val latLong = extractedArray[1];
                val issue: MapIssue = MapIssue(title, url, latLong)
                this.issues.add(issue);
            }
        }
    }

    fun checkAndPrepareTable() {
        if (this.responsesReceived == repositories.size) {
            this.sortResults();
        }
    }

    fun sortResults() {
        val sortedReps: MutableList<Reporter> = this.unsortedReps
        sortedReps.sortBy { -it.count }

        var rank = 1;
        for (i in 0..sortedReps.size - 1) {
            if (i > 0 && sortedReps[i].count < sortedReps[i - 1].count) {
                rank++;
            }
            sortedReps[i].rank = rank;
        }

        this.reporters = sortedReps;
    }

    class LeaderboardAdapter(private val myDataset: MutableList<Reporter>) : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {
        class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardAdapter.ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_card, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val rankTextView = holder.view.findViewById(R.id.leaderboard_rank) as TextView
            val nameTextView = holder.view.findViewById(R.id.leaderboard_name) as TextView
            val countTextView = holder.view.findViewById(R.id.leaderboard_count) as TextView

            rankTextView.text = myDataset[position].rank.toString()
            nameTextView.text = myDataset[position].name
            countTextView.text = myDataset[position].count.toString()

            holder.view.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    try {
                        view.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(myDataset[position].link)))
                    } catch (e: Exception) {
                    }
                }
            })
        }

        override fun getItemCount() = myDataset.size
    }
}
