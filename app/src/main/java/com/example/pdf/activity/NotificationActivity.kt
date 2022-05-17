package com.example.pdf.activity

import DBHelper
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pdf.R
import com.example.pdf.models.NotificationPDF

class NotificationActivity : AppCompatActivity() {
    var list=ArrayList<NotificationPDF>()
    var adapter: NotificationAdapter?=null
    var list_item: ListView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
    }
    @SuppressLint("Range")
    override fun onResume() {
        super.onResume()
        val db = DBHelper(this, null)

        val cursor = db.getNoti()

        list.clear()
        if(cursor!=null && cursor.moveToFirst()){
            list.add(
                NotificationPDF(
                    cursor.getString(cursor.getColumnIndex(DBHelper.TITLE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.MESSAGE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.TIME))
                    ))
            while(cursor.moveToNext()){
                list.add(
                    NotificationPDF(
                    cursor.getString(cursor.getColumnIndex(DBHelper.TITLE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.MESSAGE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.TIME))
                ))
            }

            cursor.close()
        }

        adapter=NotificationAdapter(applicationContext,list)
        list_item=findViewById(R.id.list_item)
        list_item?.adapter=adapter
    }
    inner class NotificationAdapter:BaseAdapter{
        var list=ArrayList<NotificationPDF>()
        var context: Context?=null
        constructor(context: Context, list: ArrayList<NotificationPDF>){
            this.context=context
            this.list=list
        }

        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(p0: Int): Any {
            return list[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val item=list[p0]
            var inflater=context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var view = inflater.inflate(R.layout.item_notification,null)
            var txttime:TextView=view.findViewById(R.id.txttime)
            var txttitle:TextView=view.findViewById(R.id.txttitle)
            var txtmessage:TextView=view.findViewById(R.id.txtmessage)
            var img:ImageView=view.findViewById(R.id.img)
            txttitle.text=item.title
            txtmessage.text=item.message
            img.setImageDrawable(getDrawable(R.drawable.logo))
            var arr= item.time?.split(" ")
           txttime.text= arr!!.get(3)+" "+arr!!.get(2)+" "+arr!!.get(1)
            return view
        }
    }
}