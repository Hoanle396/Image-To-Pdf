package com.example.pdf.activity

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pdf.R
import com.github.barteksc.pdfviewer.PDFView

class PDFPreView :AppCompatActivity(){
    private var view:PDFView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)
        view = findViewById(R.id.pdfView)
        var url=Uri.parse(intent.extras?.getString("url"))
        showPdf(url)
    }
    fun  showPdf(Uri:Uri){
        view?.fromUri(Uri)
            ?.defaultPage(0)
            ?.enableSwipe(true)
            ?.load()
    }
}