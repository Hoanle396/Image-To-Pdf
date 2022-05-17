import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.pdf.models.NotificationPDF
import com.example.pdf.models.Pdfhistory

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                  NAME_COl + " TEXT" + ")")
        val query2=("CREATE TABLE "+ TABLE_NAME_NOTI+" ("+
                ID_COL+  " INTEGER PRIMARY KEY, " +
                TITLE + " TEXT, "+
                MESSAGE+ " TEXT, "+
                TIME + " TEXT"+")")
        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
        db.execSQL(query2)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTI)
        onCreate(db)
    }

    // This method is for adding data in our database
    fun addName(url: Pdfhistory){

        val values = ContentValues()

        values.put(NAME_COl, url.Url)

        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, values)

        db.close()
    }

    fun addNoti(h:NotificationPDF){
        val values = ContentValues()

        values.put(TITLE,h.title)
        values.put(MESSAGE,h.message)
        values.put(TIME,h.time)

        val db = this.writableDatabase
        db.insert(TABLE_NAME_NOTI, null, values)
        db.close()
    }
    // below method is to get
    // all data from our database
    fun getName(): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM " + TABLE_NAME+" ORDER BY "+ ID_COL+" DESC", null)

    }
    fun getNoti(): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM " + TABLE_NAME_NOTI+" ORDER BY "+ ID_COL+" DESC", null)

    }
    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private val DATABASE_NAME = "LTDD"

        // below is the variable for database version
        private val DATABASE_VERSION = 1

        // below is the variable for table name
        val TABLE_NAME = "table_pdfs"

        val TABLE_NAME_NOTI="table_noti"

        // below is the variable for id column
        val ID_COL = "id"

        val TITLE="title"

        val MESSAGE="message"

        val TIME="times"
        // below is the variable for name column
        val NAME_COl = "url"
    }
}