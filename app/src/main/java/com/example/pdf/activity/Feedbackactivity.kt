package com.example.pdf.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pdf.R
import org.json.JSONException
import org.json.JSONObject

class Feedbackactivity : AppCompatActivity() {

    private var name:EditText?=null
    private var phone:EditText?=null
    private var message:EditText?=null
    private var send:Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedbackactivity)
        name=findViewById(R.id.txtname)
        phone=findViewById(R.id.txtphone)
        message=findViewById(R.id.txtmess)
        send=findViewById(R.id.send)
        send?.setOnClickListener {
            val sharedPref: SharedPreferences =
                applicationContext.getSharedPreferences("PDF", MODE_PRIVATE)
            val token = sharedPref.getString("access_token","")
            if (validate()){
                val names: String = name?.text.toString()
                val phonen: String = phone?.text.toString()
                val mess: String = message?.text.toString()
                val url = "https://serve-android.herokuapp.com/feedback"
                val queue = Volley.newRequestQueue(this)
                val request: StringRequest = object : StringRequest(
                    Request.Method.POST, url,
                    Response.Listener { response ->
                        try {
                            val jsonObject = JSONObject(response)
                            jsonObject.getString("message")
                            Toast.makeText(this,"Phản hồi của bạn đã được gửi đi",Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()

                        }
                    }, Response.ErrorListener { error -> // method to handle errors.
                        Log.e("Error : " ,"${error}")

                        Toast.makeText(this,"Có lỗi trong quá trình gửi phản hồi của bạn",Toast.LENGTH_LONG).show()
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

                    override fun getParams():Map<String, String>? {
                        val params: MutableMap<String, String> = HashMap()
                        params["name"]=names
                        params["phone"]=phonen
                        params["message"]=mess
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
    }
    fun validate(): Boolean {
        var valid = false

        val names: String = name?.text.toString()
        val phonen: String = phone?.text.toString()
        val mess: String = message?.text.toString()
        if (names.isEmpty()) {
            valid = false
            Toast.makeText(this, "Tên Không được để trống", Toast.LENGTH_SHORT).show()
        } else {
            if (names.length > 5) {
                valid = true
            } else {
                valid = false
                Toast.makeText(this, "Tên không hợp lệ", Toast.LENGTH_SHORT).show()
            }
        }

        //Handling validation for Email field
        if (!Patterns.PHONE.matcher(phonen).matches()) {
            valid = false
            Toast.makeText(this, "Hãy nhập số điện thoại", Toast.LENGTH_SHORT).show()
        } else {
            valid = true
        }

        //Handling validation for Password field
        if (mess.isEmpty()) {
            valid = false
            Toast.makeText(this, "Nội dung không được để trống", Toast.LENGTH_SHORT).show()
        } else {
            if (mess.length > 5) {
                valid = true
            } else {
                valid = false
                Toast.makeText(this, "Nội dung quá ngắn", Toast.LENGTH_SHORT).show()
            }
        }
        return valid
    }

}