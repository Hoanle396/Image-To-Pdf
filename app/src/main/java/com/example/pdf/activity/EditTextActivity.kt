package com.example.pdf.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.pdf.R


class EditTextActivity:AppCompatActivity() {
    private lateinit var editText:EditText
    private var textEdit:String?=null
    var nextStep:Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edittext_activity)
        editText=findViewById(R.id.editTextPdf)
        textEdit=intent.extras?.getString("text")
        editText.setText(textEdit)
        nextStep=findViewById(R.id.nextStep)
        nextStep!!.setOnClickListener {
            startCreate()
        }
    }
    private fun startCreate(){
        var intent=Intent(this,createPDFactivity::class.java)
        intent.putExtra("data",editText.text.toString())
        startActivity(intent)
    }
}