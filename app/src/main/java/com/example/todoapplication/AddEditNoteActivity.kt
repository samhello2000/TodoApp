package com.example.todoapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class AddEditNoteActivity : AppCompatActivity() {
    private lateinit var noteTitleEdt: EditText
    private lateinit var noteDescriptionEdt: EditText
    private lateinit var addUpdateBtn: Button
    private lateinit var database: DatabaseReference

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        noteTitleEdt = findViewById(R.id.idEditNoteTitle)
        noteDescriptionEdt = findViewById(R.id.idEditNoteDescription)
        addUpdateBtn = findViewById(R.id.idBtnAddUpdate)
        database = FirebaseDatabase.getInstance().getReference(getString(R.string.Notes))


        val noteType = intent.getStringExtra(Static.noteType)
        if (noteType.equals(getString(R.string.edit_note))) {
            val noteTitle = intent.getStringExtra(Static.noteTitle)
            val noteDesc = intent.getStringExtra(Static.noteDescription)

            addUpdateBtn.text = getString(R.string.update_note)
            noteTitleEdt.setText(noteTitle)
            noteDescriptionEdt.setText(noteDesc)
        } else {
            addUpdateBtn.text = getString(R.string.save_note)
        }

        addUpdateBtn.setOnClickListener {

            val noteTitle = noteTitleEdt.text.toString()
            val noteDescription = noteDescriptionEdt.text.toString()
            val sdf = SimpleDateFormat("DD MMM, yyyy - HH:mm")
            val currentDate: String = sdf.format(Date())
            val complete = "no"

            val note = Note(noteTitle, noteDescription, currentDate, complete)
            if (addUpdateBtn.text.equals(R.string.save_note)) {
                database.child(noteTitle).setValue(note).addOnSuccessListener {
                    noteTitleEdt.text.clear()
                    noteDescriptionEdt.text.clear()

                    Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show()
                }
            } else {
                val note1 = mapOf(
                    Static.dataBasTitle to noteTitle,
                    Static.dataBaseDescription to noteDescription,
                    Static.dataBaseTimeStamp to currentDate,
                    Static.dataBaseIsComplete to complete
                )

                database.child(noteTitle).updateChildren(note1).addOnSuccessListener {
                    noteTitleEdt.text.clear()
                    noteDescriptionEdt.text.clear()
                    Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}