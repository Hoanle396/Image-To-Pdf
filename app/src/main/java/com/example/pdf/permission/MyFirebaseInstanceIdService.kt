package com.example.pdf.permission
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {

    val TAG = "PushNotifService"
    lateinit var name: String

    override fun onTokenRefresh() {
        val token = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Token perangkat ini: ${token}")
       sendtoServer(token)
    }
    fun sendtoServer(token:String?){
        val url = "https://serve-android.herokuapp.com/feedback/insert"
        val queue = Volley.newRequestQueue(this)
        val request: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                Log.e("Response: " ,"${response}")
            }, Response.ErrorListener { error -> // method to handle errors.
                Log.e("Error : " ,"${error}")
            }) {
            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["token"]= token.toString()
                return params
            }
        }
        request.retryPolicy= DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(request)
    }
}