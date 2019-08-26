package com.example.kotlin_everywhere_to_do_list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val myAdapter = MyAdapter()
    lateinit var noteList: MutableList<NoteData>
    private var menuItemDelete: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_main.layoutManager = LinearLayoutManager(this)
        rv_main.adapter = myAdapter
        myAdapter.setItemClickListener(object : MyAdapter.ItemClickListener {
            override fun selectNote(note: NoteData) {
                note.isSelected = !note.isSelected
                menuItemDelete?.isEnabled = noteList.find { it.isSelected } != null
            }

            override fun toEdit(note: NoteData) {
                editNote(note)
            }
        })
        loadNote()

    }

    private fun addNote() {
        val intent = Intent(this, EditActivity::class.java)
        startActivityForResult(intent, 1)
    }

    private fun editNote(note: NoteData) {
        val intent = Intent(this, EditActivity::class.java)
        val noteString = Gson().toJson(note)
        intent.putExtra("note", noteString)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            loadNote()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        var add = menu?.findItem(R.id.menu_item_add)
        menuItemDelete = menu?.findItem(R.id.menu_item_delete)
        add?.setOnMenuItemClickListener {
            addNote()
            true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val editor = getSharedPreferences("MySP", Context.MODE_PRIVATE).edit()
        when (item?.itemId) {
            R.id.menu_item_delete -> {
                val readyToDelete = noteList.filter { it.isSelected }
                readyToDelete.forEach { editor.remove(it.key) }
                editor.apply()
                menuItemDelete?.isEnabled = false
                loadNote()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadNote() {
        val loader = getSharedPreferences("MySP", Context.MODE_PRIVATE)
        val keyList = loader.all.keys.sorted()
        noteList = mutableListOf<NoteData>()
        for (key in keyList) {
            val note = NoteData(key, loader.getString(key, "")!!)
            noteList.add(note)
        }
        myAdapter.update(noteList)
    }
}

