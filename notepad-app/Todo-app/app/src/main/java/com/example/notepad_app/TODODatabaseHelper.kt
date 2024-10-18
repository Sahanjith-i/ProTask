package com.example.notepad_app


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TODODatabaseHelper (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

companion object {
    private const val DATABASE_NAME = "todo.db"
    private const val DATABASE_VERSION = 1
    private const val TABLE_NAME = "todo"
    private const val ID = "id"
    private const val TITLE = "title"
    private const val CONTENT = "content"
    private const val DATE = "date"
    private const val TIME = "time"
    private const val STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase?) {
       val createTable = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $TITLE TEXT, $CONTENT TEXT, $DATE TEXT, $TIME TEXT,$STATUS INTEGER DEFAULT 0)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTable)
        onCreate(db)
    }

    suspend fun insertTodoAsync(todo: Todo): Boolean {
        return withContext(Dispatchers.IO) {
            val db = writableDatabase
            val values = ContentValues().apply {
                put(TITLE, todo.title)
                put(CONTENT, todo.content)
                put(DATE, todo.date)
                put(TIME, todo.time)
                put(STATUS, 0)
            }
            val success = db.insert(TABLE_NAME, null, values)
            db.close()
            success != -1L
        }
    }
    fun getAllTodo(): List<Todo> {
        val list = mutableListOf<Todo>()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                    val content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT))
                    val date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
                    val time = cursor.getString(cursor.getColumnIndexOrThrow(TIME))
                    val status = cursor.getInt(cursor.getColumnIndexOrThrow(STATUS))
                    val todo = Todo (id, title, content, date, time,status)
                    list.add(todo)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return list
    }

    fun updateTodo(todo: Todo){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TITLE, todo.title)
            put(CONTENT, todo.content)
            put(DATE, todo.date)
            put(TIME, todo.time)
        }
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(todo.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()

    }

    fun getTodoById(todoId:Int): Todo {
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $todoId"
        val cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT))
        val date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
        val time = cursor.getString(cursor.getColumnIndexOrThrow(TIME))
        val status = cursor.getInt(cursor.getColumnIndexOrThrow(STATUS))

        cursor.close()
        db.close()
        return Todo (id, title, content, date, time,status)

    }
    fun deleteTodoById(todoId: Int){
        val db = writableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(todoId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
    fun updateTodoStatus(todoId: Int, newStatus: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(STATUS, newStatus)
        }
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(todoId.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }


}


