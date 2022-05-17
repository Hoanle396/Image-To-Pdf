package com.example.pdf.models

class NotificationPDF {
    var title:String?=null
    var message:String?=null
    var time:String?=null
    constructor(title:String,message:String,time: String){
        this.title=title
        this.message=message
        this.time=time
    }

}