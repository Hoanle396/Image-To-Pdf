package com.example.pdf.activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pdf.MainActivity
import com.example.pdf.R


class UserFragment : Fragment() {
     private var logoutbtn:Button?=null
    private var feedbackbtn:Button?=null
    private var history:Button?=null
    private var notification:Button?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref: SharedPreferences =
            requireActivity().getSharedPreferences("PDF", AppCompatActivity.MODE_PRIVATE)
        val name = sharedPref.getString("fullName","")
        val photo = sharedPref.getString("photoUrl","")
        val email = sharedPref.getString("email","")
        var avatar:ImageView=view.findViewById(R.id.avatar)
        var tfemail:TextView=view.findViewById(R.id.email)
        var tfname:TextView=view.findViewById(R.id.name)
        Glide.with(this).load(Uri.parse(photo)).placeholder(R.drawable.image).into(avatar)
        tfname.text=name
        tfemail.text=email

        notification=view.findViewById(R.id.notificationbtn)
        history=view.findViewById(R.id.historybtn)
        feedbackbtn=view.findViewById(R.id.feedbackbtn)
        logoutbtn=view.findViewById(R.id.logoutbtn)
        notification?.setOnClickListener {
            notification()
        }
        history?.setOnClickListener {
            history()
        }
        feedbackbtn?.setOnClickListener {
            feedback()
        }
        logoutbtn?.setOnClickListener {
            logout()
        }
    }
    private fun notification(){
        var intent=Intent(requireContext() ,NotificationActivity::class.java)
        startActivity(intent)
    }
    private fun history(){
      val intent=Intent(requireContext(),HomeActivity::class.java)
        startActivity(intent)
    }
    private fun feedback(){
        val intent=Intent(requireContext(),Feedbackactivity::class.java)
        startActivity(intent)
    }
    private fun logout(){
        var sharedPref: SharedPreferences =requireActivity().getSharedPreferences("PDF", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.remove("access_token")
        editor.remove("key")
        editor.remove("email")
        editor.remove("fullName")
        editor.remove("photoUrl")
        editor.commit()
        var intent=Intent(requireContext(),MainActivity::class.java)
        startActivity(intent)
    }
}