package com.example.kotlin_everywhere_to_do_list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_note.view.*

class MyAdapter: RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var noteList = mutableListOf<EventData>()
    private var itemClickListenerImp: ItemClickListener? = null

    interface ItemClickListener{
        fun toEdit(event:EventData)
    }

    fun setToEditClickListener(itemClickListenerImp: ItemClickListener){
        this.itemClickListenerImp = itemClickListenerImp
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_note, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.bind(noteList[position])
    }

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvNote = view.tv_note
        val imgToEdit = view.img_to_edit
        fun bind(note: EventData){
            tvNote.text = note.note
            imgToEdit.setOnClickListener {
                itemClickListenerImp?.toEdit(note)
            }
        }
    }

    fun update(newList: MutableList<EventData>){
        noteList = newList
        notifyDataSetChanged()
    }

}