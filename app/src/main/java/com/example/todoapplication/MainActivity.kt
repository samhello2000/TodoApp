package com.example.todoapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var notesRV: RecyclerView
    private lateinit var notesArrayList: ArrayList<Note>
    private lateinit var addFAB: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("ArrayList", "Inside Main Activity onCreate() method")
        setContentView(R.layout.activity_main)
        Log.i("ArrayList", "MainActivity layout inflated")
        notesRV = findViewById(R.id.idRVNotes)
        Log.i("ArrayList", "RecyclerView Variable inflated.")
        notesRV.layoutManager = LinearLayoutManager(this)
        Log.i("ArrayList", "LayoutManager added to RecyclerView.")
        notesRV.setHasFixedSize(true)

        notesArrayList = arrayListOf()
        Log.i("ArrayList", notesArrayList.size.toString() + " one After notesArrayList declaration")

//        val adapter=NoteRVAdapter(notesArrayList)
//        notesRV.adapter = adapter

        dbref = FirebaseDatabase.getInstance().getReference(getString(R.string.Notes))
        Log.i("ArrayList", "Database reference assigned to variable")
        getNotesData()
        Log.i("ArrayList", notesArrayList.size.toString() + " two After getNotes() get called")

        addFAB = findViewById(R.id.idFABAddNote)
        Log.i("ArrayList", "Floating button variable inflated")
        addFAB.setOnClickListener {
            Log.i("ArrayList", "Inside Floating button onClickListener. Just Clicked!!!")
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivity(intent)
            Log.i("ArrayList", "AddEditNoteActivity started")
            //getNotesData()
            //this.finish()
        }

        notesRV.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                Log.i("ArrayList", "RecyclerView element clicked!!!")
                val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
                intent.putExtra("noteType", "Edit")
                intent.putExtra("noteTitle", notesArrayList[position].title)
                intent.putExtra("noteDescription", notesArrayList[position].description)
                startActivity(intent)
                Log.i("ArrayList", "In AddEditNoteActivity to edit the Note.")
            }
        })
    }


    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }

    private fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                Log.i("ArrayList", "Inside onChildViewDetachedFromWindow() method")
                // view.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    Log.i(
                        "ArrayList",
                        "Inside onChildViewAttachedToWindow method....view.setOnClickListener"
                    )
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }

    private fun getNotesData() {
        dbref = FirebaseDatabase.getInstance().getReference(getString(R.string.Notes))

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    notesArrayList.clear()
                    for (noteSnapshot in snapshot.children) {

                        val note = noteSnapshot.getValue(Note::class.java)
//                        if(notesArrayList.contains(note))
//                        {
//                            continue
//                        }
//                        else
//                        {
                            notesArrayList.add(note!!)
//                        }
                    }
                    Log.i("ArrayList", notesArrayList.size.toString() + " three In getNotes() after for loop")
                    val adapter = NoteRVAdapter(notesArrayList)
                    notesRV.adapter = adapter
                    Log.i(
                        "ArrayList",
                        "Inside getNotesData() method adapter is attached to recyclerView"
                    )
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
