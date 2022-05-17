package com.example.pdf.helper

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.pdf.Entity.OCRResponse
import com.example.pdf.activity.EditTextActivity
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class UploadUtility(var activity: Activity) {

    private var dialog: ProgressDialog? = null
    private var serverURL: String = "https://intense-atoll-99172.herokuapp.com/"
    private var serverUploadDirectoryPath: String = "https://intense-atoll-99172.herokuapp.com/"
    private val client = OkHttpClient()
     fun uploadFile(sourceFilePath: String,key: String="", uploadedFileName: String? = null) {
         uploadFile(File(sourceFilePath),key, uploadedFileName)
    }

     fun uploadFile(sourceFile: File,key: String = "", uploadedFileName: String? = null){
        Thread {
            val mimeType = getMimeType(sourceFile)
            if (mimeType == null) {
                Log.e("file error", "Not able to get mime type")
                return@Thread
            }
            val fileName: String = uploadedFileName ?: sourceFile.name
            toggleProgressDialog(true)
            try {
                val requestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image", fileName,sourceFile.asRequestBody(mimeType.toMediaTypeOrNull()))
                        .addFormDataPart("key",key)
                        .build()

                val request: Request = Request.Builder().url(serverURL).post(requestBody).build()

                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    Log.d("File upload","success, path: $serverUploadDirectoryPath$fileName")
                    showToast("File OCR successfully at $serverUploadDirectoryPath")

                    val gson= Gson()
                    val res:OCRResponse=gson.fromJson(response.body?.string(), OCRResponse::class.java)

                    var intent= Intent(this.activity, EditTextActivity::class.java)
                    intent.putExtra("text",res.data)
                    startActivity(activity,intent,null)
                }
                else{
                    Log.e("Server Error", "code 500")
                    showToast("File OCR failed please try again")
                }
            } catch (ex: Exception) {
                Log.e("Error ", ex.message.toString() )
                Log.e("File upload", "failed")
                showToast("File OCR failed please try again")
            }
            toggleProgressDialog(false)
        }.start()
    }

    // url = file path or whatever suitable URL you want.
    private fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    private fun showToast(message: String) {
        activity.runOnUiThread {
            Toast.makeText( activity, message, Toast.LENGTH_LONG ).show()
        }
    }

    private fun toggleProgressDialog(show: Boolean) {
        activity.runOnUiThread {
            if (show) {
                dialog = ProgressDialog.show(activity, "", "Waiting to OCR file...", true)
            } else {
                dialog?.dismiss()
            }
        }
    }

}