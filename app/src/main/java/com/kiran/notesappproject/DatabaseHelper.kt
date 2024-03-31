package com.kiran.notesappproject

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_TABLE)
        onCreate(db)
    }

    fun addNote(note: Note): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getNoteById(id: Long): Note? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME, null,
            "$COLUMN_ID = ?", arrayOf(id.toString()), null, null, null
        )
        return if (cursor.moveToFirst()) {
            val note = Note(
                cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
            )
            cursor.close()
            note
        } else {
            cursor.close()
            null
        }
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val note = Note(
                    cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
                )
                notes.add(note)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return notes
    }

    fun updateNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(note.id.toString()))
    }

    fun deleteNoteById(id: Long) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    companion object {
        private const val DATABASE_NAME = "NotesDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME " +
                "($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT)"
        private const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}

