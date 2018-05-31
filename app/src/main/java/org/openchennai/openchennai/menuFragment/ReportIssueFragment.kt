package org.openchennai.openchennai.menuFragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.openchennai.openchennai.MainActivity
import org.openchennai.openchennai.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ReportIssueFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ReportIssueFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ReportIssueFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var selectedItem: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var mainView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_report_issue, container, false)
        val values = MainActivity.repositories
        val spinner = v.findViewById(R.id.sectionSpinner) as Spinner
        val adapter = ArrayAdapter(this.activity, android.R.layout.simple_spinner_item, values)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter
        mainView = v
        return v
    }

    private lateinit var selectedSection: String

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner = view!!.findViewById(R.id.sectionSpinner) as Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedSection = MainActivity.repositories[position]
//                Toast.makeText(activity, MainActivity.repositories[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        val reportIssueButton: Button = view.findViewById(R.id.report_issue_button) as Button
        reportIssueButton.setOnClickListener {
            reportIssue()
        }
    }

    private fun reportIssue() {
        Toast.makeText(activity,"Reporting issue...", Toast.LENGTH_SHORT).show()
        val queue = Volley.newRequestQueue(activity)
        val url = "https://api.github.com/repos/OpenChennai/" + selectedSection + "/issues"

        val name = (mainView.findViewById(R.id.name_text) as EditText).text.toString()
        val link = (mainView.findViewById(R.id.link_text) as EditText).text.toString()
        val description = (mainView.findViewById(R.id.description_text) as EditText).text.toString()

        var title: String
        if (description.length >= 80) {
            title = description.substring(0, 80)
        } else {
            title = description
        }

        val body: String = "Reporter: " + name + "\r\n" + "Link: " + link + "\r\n\r\n" + description +
                "\r\n\r\n" + "Sent from Open Chennai Android app"

        val requestBody = JSONObject()
        requestBody.put("title", title)
        requestBody.put("body", body)

        val jsonRequest = CustomVolleyRequest(Request.Method.POST, url, requestBody,
                Response.Listener {
                    Toast.makeText(activity,"Your issue has been reported successfully", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener {
                    Toast.makeText(activity,"We are having some trouble reporting your issue", Toast.LENGTH_SHORT).show()
                })

        queue.add(jsonRequest)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                ReportIssueFragment().apply {
                    arguments = Bundle().apply {
                    }
                }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportIssueFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ReportIssueFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    inner class CustomVolleyRequest(method: Int, url: String, jsonRequest: JSONObject, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener) : JsonObjectRequest(method, url, jsonRequest, listener, errorListener) {
//    inner class CustomVolleyRequest() : JsonRequest<JSONArray>() {

        protected val PROTOCOL_CHARSET = "utf-8"

        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
            headers.put("Content-Type", "application/json")
            headers.put("Authorization", "Bearer 5e64071c50185dfc326d45c9a683dea4bcc20d72")
            return headers
        }

//        override fun parseNetworkResponse(response: NetworkResponse): Response<JSONArray> {
//            try {
//                val jsonString = String(response.data, charset(PROTOCOL_CHARSET))
//                return Response.success(JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response))
//            } catch (e: UnsupportedEncodingException) {
//                return Response.error(ParseError(e))
//            } catch (je: JSONException) {
//                return Response.error(ParseError(je))
//            }
//        }

    }
}
