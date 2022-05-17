package com.example.pdf.activity

import DBHelper
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pdf.R
import com.example.pdf.helper.AppNotification
import com.example.pdf.models.NotificationPDF
import com.example.pdf.models.Pdfhistory
import com.github.barteksc.pdfviewer.PDFView
import java.io.FileOutputStream
import java.util.*

class createPDFactivity:AppCompatActivity() {
    private val CHANNEL_ID="1"
    private val PATH="/Android/data/com.example.pdf/"
    var umbralization: String? = null
    private var PDFView: PDFView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ocrview_activity)
        PDFView = findViewById<View>(R.id.pdfView) as PDFView
        umbralization = intent.extras?.getString("data")
        createPDF()
    }
    fun createPDF(){

        try {

            val document: PdfDocument = PdfDocument()
            val pageInfo: PdfDocument.PageInfo  = PdfDocument.PageInfo.Builder(400, 600, 1).create()
            val page: PdfDocument.Page  = document.startPage(pageInfo)
            val myPaint = Paint()
            var x= 10f
            var y = 25f
            // Draw the bitmap onto the page
            page.canvas.drawText("\n",1f,1f,myPaint)
            for (line in umbralization!!.split("\n")){
                page.canvas.drawText(line, x, y, myPaint);
                y+=myPaint.descent()-myPaint.ascent();
            }
//            page.canvas.drawText(umbralization.toString(),10f,25f,myPaint)
            document.finishPage(page)
            val directoryPath: String  = Environment.getExternalStoragePublicDirectory(PATH).toString()+ "/"+ Calendar.getInstance().time+"_"+(1000..9999).random().toString()+".pdf"
            document.writeTo( FileOutputStream(directoryPath ))
            document.close()
            PDFView?.fromUri(Uri.parse(directoryPath))
                ?.defaultPage(0)
                ?.enableSwipe(true)
                ?.load()
            val db = DBHelper(this, null)
            db.addName(Pdfhistory(directoryPath))
            showNotification()

        }
        catch (e:Exception){
            Toast.makeText(this,"Failed to create PDF", Toast.LENGTH_SHORT).show()
        }

    }
    private  fun  showNotification(){
        var title="PDF Convert"
        var message="You have successfully created a PDF document"
        var builder = NotificationCompat.Builder(this, AppNotification.CHANNEL_1_ID)
            .setSmallIcon(R.drawable.pdflogo)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true);
        val db = DBHelper(this, null)
        val start = Date()
        db.addNoti(NotificationPDF(title, message,start.toString()))
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, builder.build())

    }


}
