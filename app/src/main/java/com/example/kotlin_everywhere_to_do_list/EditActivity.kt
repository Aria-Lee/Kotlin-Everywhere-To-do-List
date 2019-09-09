package com.example.kotlin_everywhere_to_do_list

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {

    var isEditMode = false
    var editNoteKey = ""
    val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        title = "編輯模式"

        if (!intent.getStringExtra("note").isNullOrEmpty()) {
            isEditMode = true
            val noteString = intent.getStringExtra("note")
            val note = Gson().fromJson(noteString, NoteData::class.java)
            editNoteKey = note.key
            edtText_Title.setText(note.title)
            edtText_Content.setText(note.content)
            tv_choosed_date.text = note.date
        }

        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(year, month, day)
            val sdf = SimpleDateFormat("yyyy / MM / dd", Locale.TAIWAN)
            tv_choosed_date.text = sdf.format(calendar.time)
        }

        btn_choose_date.setOnClickListener {
            DatePickerDialog(this,
                dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        btn_confirm.setOnClickListener {
            if (edtText_Title.text.isNullOrEmpty()) Toast.makeText(this, "請輸入待辦事項", Toast.LENGTH_SHORT).show()
            else confirm()
        }

        btn_cancel.setOnClickListener {
            this.finish()
        }

    }

    private fun confirm() {
        saveNote()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun saveNote() {
        val preference = getSharedPreferences("MySP", Context.MODE_PRIVATE)
        val editor = preference.edit()
        if(isEditMode){
            val note = NoteData(editNoteKey, "${edtText_Title.text}","${edtText_Content.text}", "${tv_choosed_date.text}")
            val noteString = Gson().toJson(note)
            editor.putString(editNoteKey, noteString).apply()
        }else{
            var index = 0
            while (!preference.getString("myNote-$index", "").isNullOrEmpty()) index++
            val note = NoteData("myNote-$index","${edtText_Title.text}", "${edtText_Content.text}", "${tv_choosed_date.text}")
            val noteString = Gson().toJson(note)
            editor.putString("myNote-$index", noteString).apply()
        }
    }
}
