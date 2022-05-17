package com.example.pdf.activity

import DBHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo.Builder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pdf.R
import com.example.pdf.helper.AppNotification
import com.example.pdf.models.NotificationPDF
import com.example.pdf.models.Pdfhistory
import com.example.pdf.models.PickImage
import com.github.barteksc.pdfviewer.PDFView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileOutputStream
import java.util.*


class CreateActivity:AppCompatActivity() {
    private val PATH="/Android/data/com.example.pdf/"
    private var umbralization:Uri?=null
    var listPrivate = ArrayList<PickImage>();
    private var PDFView: PDFView? = null
//    private  var bitmap:Bitmap?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)
        PDFView = findViewById(R.id.pdfView)
//        umbralization = intent.extras?.get("image") as Uri
//        val input = contentResolver?.openInputStream(umbralization!!)
//        bitmap= BitmapFactory.decodeStream(input,null, BitmapFactory.Options())!!
//        createPdf(bitmap)
        val type = TypeToken.getParameterized(ArrayList::class.java, PickImage::class.java).type

        listPrivate=Gson().fromJson(intent.extras?.getString("image"),type)
        createList(listPrivate)
    }
    private fun createList(list: ArrayList<PickImage>){
        val document: PdfDocument = PdfDocument()
        var bitmap:Bitmap
        for (item in list){
            var input = contentResolver.openInputStream(Uri.parse(item.url!!))
            bitmap= BitmapFactory.decodeStream(input,null, BitmapFactory.Options())!!
            val pageInfo: PdfDocument.PageInfo  = Builder(bitmap.width, bitmap.height,1).create()
            val page: PdfDocument.Page  = document.startPage(pageInfo)
            val canvas: Canvas = page.canvas
            canvas.drawBitmap(bitmap, 1f, 1f, null)
            document.finishPage(page)
        }
        try {
            val directoryPath: String  = Environment.getExternalStoragePublicDirectory(PATH).toString()+ "/"+Calendar.getInstance().time+"_"+(1000..9999).random().toString()+".pdf"
            document.writeTo( FileOutputStream(directoryPath ))
            document.close()
            PDFView?.fromUri(Uri.parse(directoryPath))
                ?.defaultPage(0)
                ?.enableSwipe(true)
                ?.load()
            val db = DBHelper(this, null)
            db.addName(Pdfhistory(directoryPath))
            showNotification()
        } catch (e: Exception) {
            Toast.makeText(this,"Failed to create PDF", Toast.LENGTH_SHORT).show()
        }
    }
    private fun  createPdf(bitmap: Bitmap){

        // Create a PdfDocument with a page of the same size as the image
        val document: PdfDocument = PdfDocument()
        val pageInfo: PdfDocument.PageInfo  = Builder(bitmap.width, bitmap.height, 1).create()
        val page: PdfDocument.Page  = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        // Draw the bitmap onto the page

        document.finishPage(page)


        try {
            val directoryPath: String  = Environment.getExternalStoragePublicDirectory(PATH).toString()+ "/"+Calendar.getInstance().time+"_"+(1000..9999).random().toString()+".pdf"
            document.writeTo( FileOutputStream(directoryPath ))
            document.close()
            PDFView?.fromUri(Uri.parse(directoryPath))
                ?.defaultPage(1)
                ?.enableSwipe(true)
                ?.load()
            val db = DBHelper(this, null)
            db.addName(Pdfhistory(directoryPath))
            showNotification()
        } catch (e: Exception) {
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
        val start: Date = Date()
        db.addNoti(NotificationPDF(title, message,start.toString()))
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, builder.build())
    }
}


