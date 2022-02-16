package com.example.todoapplication

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todoapplication.R.color.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class NoteRVAdapter(private val noteList: ArrayList<Note>) : Adapter<NoteRVAdapter.MyViewHolder>() {
    private lateinit var database: DatabaseReference

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val noteTitle: TextView = itemView.findViewById(R.id.idTVNoteTitle)
        val noteTimeStamp: TextView = itemView.findViewById(R.id.idTVTimeStamp)
        val delete: ImageView = itemView.findViewById(R.id.idIVDelete)
        val done: ImageView = itemView.findViewById(R.id.idIVDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        Log.i("ArrayList", "Inside onCreateViewHolder() method of NoteRVAdapter")
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.note_rv_item,
            parent,
            false
        )
        return MyViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor", "SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        Log.i("ArrayList", "Inside onBindViewHolder() method of NoteRVAdapter")
        val currentItem = noteList[position]
        holder.noteTitle.text = currentItem.title
        holder.noteTimeStamp.text = currentItem.timeStamp
        database = FirebaseDatabase.getInstance().getReference("Notes")
        holder.delete.setOnClickListener {
            val title = holder.noteTitle.text.toString()
            database.child(title).removeValue().addOnSuccessListener {

//                Toast.makeText(, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                Log.i("ArrayList", "Deleted Successfully")
//                notifyDataSetChanged()

            }.addOnFailureListener {
//                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                Log.i("ArrayList", "Failed")
            }
        }


        Log.i("ArrayList",noteList[position].toString()+" Adapter")
        val IsDone= noteList.get(position).Completed
        Log.i("ArrayList",IsDone.toString())

        holder.done.setOnClickListener {
            Log.i("ArrayList","$IsDone OnClickListener")
            val titleNote = noteList[position].title
            if (titleNote != null) {
                if (IsDone != null) {

                    database.child(titleNote).child("Completed").setValue("yes")

                }
            }

        }
        if(IsDone=="yes")
        {
            holder.cardView.setBackgroundResource(green)
            holder.done.setImageResource(R.drawable.ic_done_all)
        }

//        holder.done.setOnClickListener {
//            val noteTitle = holder.noteTitle.text.toString()
//            val sdf = SimpleDateFormat("DD MMM, yyyy - HH:mm")
//            val currentDate: String = sdf.format(Date())
//            val complete = "yes"
//
//            val note = mapOf(
//                Static.dataBasTitle to noteTitle,
//                Static.dataBaseTimeStamp to currentDate,
//                Static.dataBaseIsComplete to complete
//            )
//            database.child(noteTitle).updateChildren(note).addOnSuccessListener {
////                Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
//                Log.i("ArrayList", "Successfully updated")
////               holder.cardView.setBackgroundColor(R.color.purple_200)
//
//            }.addOnFailureListener {
////                Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show()
//                Log.i("ArrayList", "Failed")
//            }
//        }
//        Log.i("ArrayList",noteList[position].Completed.toString()+"CardView")
//        if(noteList[position].Completed.equals("yes") ){
//            holder.cardView.setCardBackgroundColor(green)
//        }
    }

    override fun getItemCount(): Int {
//        Log.i("ArrayList", "Inside getItemCount() method of NoteRVAdapter")
        return noteList.size
    }
}


//Log.i("ArrayList",noteList[position].toString()+" Adapter")
//        val IsDone= noteList.get(position).isCompleted
//        Log.i("ArrayList",IsDone.toString())
//
//        holder.done.setOnClickListener {
//            Log.i("ArrayList","$IsDone OnClickListener")
//            val titleNote = noteList[position].title
//            if (titleNote != null) {
//                if (IsDone != null) {
//
//                    database.child(titleNote).child("isCompleted").setValue("done")
//
//                }
//            }
//
//        }
//        if(IsDone=="done")
//        {
//            holder.cardView.setBackgroundResource(R.color.teal_700)
//        }