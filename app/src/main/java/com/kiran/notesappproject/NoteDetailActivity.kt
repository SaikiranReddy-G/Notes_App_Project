package com.kiran.notesappproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var dbHelper: DatabaseHelper
    private var noteId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)
        dbHelper = DatabaseHelper(this)


        noteId = intent.getLongExtra("noteId", -1)


        if (noteId != -1L) {
            val note = dbHelper.getNoteById(noteId)
            titleEditText.setText(note?.title)
            contentEditText.setText(note?.content)
        }


        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                if (noteId != -1L) {

                    val updatedNote = Note(noteId, title, content)
                    dbHelper.updateNote(updatedNote)
                } else {

                    val newNote = Note(0, title, content)
                    dbHelper.addNote(newNote)
                }
                finish()
            }
        }


        deleteButton.setOnClickListener {
            if (noteId != -1L) {
                dbHelper.deleteNoteById(noteId)
                Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}


