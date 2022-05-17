package com.example.pdf.activity

import DBHelper
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.pdf.R
import com.example.pdf.models.Pdfhistory
import com.github.barteksc.pdfviewer.PDFView


class HistoryFragment : Fragment() {
    var list=ArrayList<Pdfhistory>()
    var adapter:PDFAdapter?=null
    var girdView:GridView?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    @SuppressLint("Range")
    override fun onResume() {
        super.onResume()
        val db = DBHelper(requireContext(), null)

        val cursor = db.getName()

        list.clear()
        if(cursor!=null && cursor.moveToFirst()){
            list.add(Pdfhistory(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl))))
            while(cursor.moveToNext()){
                list.add(Pdfhistory(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl))))
            }

            cursor.close()
        }

        adapter=PDFAdapter(requireContext(),list)
        girdView=view?.findViewById(R.id.girdView) as GridView
        girdView?.adapter=adapter
    }
   inner class PDFAdapter:BaseAdapter{
       var list=ArrayList<Pdfhistory>()
       var context:Context?=null
       constructor(context: Context,list: ArrayList<Pdfhistory>){
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
           val food=list[p0]
           var inflater=context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
           var view = inflater.inflate(R.layout.item,null)
           var image:PDFView=view.findViewById(R.id.PdfPreView)
           image.fromUri(Uri.parse(food.Url))
               ?.defaultPage(0)
               ?.load()

           view.setOnClickListener {
               var intent=Intent(context,PDFPreView::class.java)
               intent.putExtra("url",food.Url)
               context!!.startActivity(intent)
           }
           return view
       }

   }
}