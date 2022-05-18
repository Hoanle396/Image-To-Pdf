package com.example.pdf


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pdf.activity.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ckeck()
        Thread.sleep(2000)
        setContentView(R.layout.activity_login)
        overridePendingTransition(0, 0)
        val relativeLayout: View = findViewById(R.id.container)
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        relativeLayout.startAnimation(animation)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_google))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }


    fun login(view: View){
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, RC_SIGN_IN
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            // Signed in successfully
            val googleId = account?.id ?: ""
            Log.i("Google ID",googleId)

            val googleFirstName = account?.givenName ?: ""
            Log.i("Google First Name", googleFirstName)

            val googleLastName = account?.familyName ?: ""
            Log.i("Google Last Name", googleLastName)

            val googleEmail = account?.email ?: ""
            Log.i("Google Email", googleEmail)

            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)

            val googleIdToken = account?.idToken ?: ""
            Log.i("Google ID Token", googleIdToken)

            apiCall(googleFirstName,googleLastName, googleEmail,googleProfilePicURL,googleIdToken)

        } catch (e: ApiException) {
            Log.e("failed : ",e.statusCode.toString())
        }
    }
    private fun apiCall(
        givenName:String,
        familyName:String,
        email: String,
        photoUrl:String,
        idToken:String
    ) {

        val url = "https://serve-android.herokuapp.com/user/login"

        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(this@MainActivity)

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        val request: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val sharedPref: SharedPreferences =
                        applicationContext.getSharedPreferences("PDF", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("access_token", jsonObject.getString("access_token"))
                    editor.putString("key", jsonObject.getString("key"))
                    Log.e("key : ",jsonObject.getString("access_token"))
                    editor.commit()
                    val intentH = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intentH)
                    finish()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error -> // method to handle errors.
               Log.e("Error : " ,"${error}")
            }) {
            override fun getBodyContentType(): String {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            override fun getParams(): Map<String, String>? {

                // below line we are creating a map for storing
                // our values in key and value pair.
                val params: MutableMap<String, String> = HashMap()

                // on below line we are passing our
                // key and value pair to our parameters.
                params["givenName"] = givenName
                params["familyName"]=familyName
                params["email"]=email
                params["photoUrl"]=photoUrl
                params["idToken"]=idToken
                return params
            }

        }
        request.retryPolicy= DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        // below line is to make
        // a json object request.
        queue.add(request)
    }
    private fun check(){
        val sharedPref: SharedPreferences =
            applicationContext.getSharedPreferences("PDF", MODE_PRIVATE)
        val token = sharedPref.getString("access_token","")
        if (token!=""){
            val intentH = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intentH)
            finish()
        }
    }
    private fun ckeck(){
        val sharedPref: SharedPreferences =
            applicationContext.getSharedPreferences("PDF", MODE_PRIVATE)
        val token = sharedPref.getString("access_token","")
        if (token!=""){
            val url = "https://serve-android.herokuapp.com/user/profile"
            val queue = Volley.newRequestQueue(this@MainActivity)
            val request: StringRequest = object : StringRequest(
                Request.Method.GET, url,
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        val sharedPref: SharedPreferences =this.getSharedPreferences("PDF", MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putString("fullName", jsonObject.getString("fullName"))
                        editor.putString("email", jsonObject.getString("email"))
                        editor.putString("photoUrl", jsonObject.getString("photoUrl"))
                        editor.commit()
                        val intentH = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intentH)
                        finish()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        val sharedPref: SharedPreferences =this.getSharedPreferences("PDF", MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.remove("access_token")
                        editor.commit()
                    }
                }, Response.ErrorListener { error -> // method to handle errors.
                    Log.e("Error : " ,"${error}")
                }) {
                override fun getBodyContentType(): String {
                    // as we are passing data in the form of url encoded
                    // so we are passing the content type below
                    return "application/x-www-form-urlencoded; charset=UTF-8"
                }

                override fun getHeaders(): Map<String, String>? {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"]= "Bearer $token"
                    headers["Accept"]="application/json"
                    return headers
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
}