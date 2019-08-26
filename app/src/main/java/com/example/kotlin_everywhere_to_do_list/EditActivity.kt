package com.example.kotlin_everywhere_to_do_list

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    var isEditMode = false
    var editNoteKey = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        title = "編輯模式"

        if (!intent.getStringExtra("note").isNullOrEmpty()) {
            isEditMode = true
            val noteString = intent.getStringExtra("note")
            val note = Gson().fromJson(noteString, NoteData::class.java)
            editNoteKey = note.key
            edtText_edit.setText(note.title)
        }

        btn_confirm.setOnClickListener {
            if (edtText_edit.text.isNullOrEmpty()) Toast.makeText(this, "請輸入待辦事項", Toast.LENGTH_SHORT).show()
            else confirm()
        }

        btn_cancel.setOnClickListener {
            this.finish()
        }

    }

    private fun confirm() {
        val newNote = edtText_edit.text.toString()
        saveNote(newNote)
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun saveNote(note: String) {
        val preference = getSharedPreferences("MySP", Context.MODE_PRIVATE)
        val editor = preference.edit()
        if(isEditMode){
            editor.putString(editNoteKey, note).apply()
        }else{
            var index = 0
            while (!preference.getString("myNote-$index", "").isNullOrEmpty()) index++
            editor.putString("myNote-$index", note).apply()
        }
    }
}
