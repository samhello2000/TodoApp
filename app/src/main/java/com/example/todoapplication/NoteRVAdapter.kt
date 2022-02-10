package com.example.todoapplication

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class NoteRVAdapter(private val noteList: ArrayList<Note>) : Adapter<NoteRVAdapter.MyViewHolder>()
{
    private lateinit var database: DatabaseReference

    inner class MyViewHolder(itemView: View) : ViewHolder(itemView)
    {
        val noteTitle: TextView = itemView.findViewById(R.id.idTVNoteTitle)
        val noteTimeStamp: TextView = itemView.findViewById(R.id.idTVTimeStamp)
        val delete: ImageView = itemView.findViewById(R.id.idIVDelete)
        val done:ImageView=itemView.findViewById(R.id.idIVDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        Log.i("ArrayList", "Inside onCreateViewHolder() method of NoteRVAdapter")
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_rv_item,
             parent,
            false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor", "SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        Log.i("ArrayList", "Inside onBindViewHolder() method of NoteRVAdapter")
        val currentItem = noteList[position]
        holder.noteTitle.text = currentItem.title
        holder.noteTimeStamp.text = currentItem.timeStamp
        database = FirebaseDatabase.getInstance().getReference("Notes")
        holder.delete.setOnClickListener{
            val title = holder.noteTitle.text.toString()
            database.child(title).removeValue().addOnSuccessListener {

                //Toast.makeText(, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                Log.i("ArrayList","Deleted Successfully")
               // notifyDataSetChanged()

            }.addOnFailureListener {
                //Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                Log.i("ArrayList","Failed")
            }
        }

        holder.done.setOnClickListener {
            val noteTitle = holder.noteTitle.text.toString()
            //val noteDescription = noteDescription.text.toString()
            val sdf = SimpleDateFormat("DD MMM, yyyy - HH:mm")
            val currentDate: String = sdf.format(Date())
            val complete = "yes"

            val note = mapOf(
                "title" to noteTitle,
                "timeStamp" to currentDate,
                "isComplete" to complete
            )
            database.child(noteTitle).updateChildren(note).addOnSuccessListener {
                //Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
                Log.i("ArrayList","Successfully updated")
                holder.done.setBackgroundColor(R.color.purple_200)
//                holder.done.setImageDrawable(ic)
//                holder.cardView.setBackgroundResource(R.color.purple_200)

            }.addOnFailureListener {
                //Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show()
                Log.i("ArrayList","Failed")
            }
        }
    }

    override fun getItemCount(): Int {
        Log.i("ArrayList", "Inside getItemCount() method of NoteRVAdapter")
        return noteList.size
    }
}