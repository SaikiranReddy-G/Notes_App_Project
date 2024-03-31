package com.kiran.notesappproject

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var notesAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        dbHelper = DatabaseHelper(this)

        // To refresh list
        refreshNotesList()

        // Onclick listener to open NoteDetailActivity
        listView.setOnItemClickListener { _, _, position, _ ->
            val noteId = dbHelper.getAllNotes()[position].id
            val intent = Intent(this, NoteDetailActivity::class.java)
            intent.putExtra("noteId", noteId)
            startActivity(intent)
        }

        // Long Click listener to delete note
        listView.setOnItemLongClickListener { _, _, position, _ ->
            val noteId = dbHelper.getAllNotes()[position].id
            dbHelper.deleteNoteById(noteId)
            refreshNotesList()
            true
        }

        // To add a new note
        findViewById<FloatingActionButton>(R.id.addNoteButton).setOnClickListener {
            startActivity(Intent(this, NoteDetailActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshNotesList()
    }

    private fun refreshNotesList() {
        val notes = dbHelper.getAllNotes().map { "${it.title} - ${it.content}" }
        notesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)
        listView.adapter = notesAdapter
    }
}

