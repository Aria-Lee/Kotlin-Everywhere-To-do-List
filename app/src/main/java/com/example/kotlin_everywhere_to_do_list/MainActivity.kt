package com.example.kotlin_everywhere_to_do_list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var isAddMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadEvent()
        btn_edit.setOnClickListener {
            if (!isAddMode) editEvent()
            else Toast.makeText(this, "請先新增後才能編輯喔！", Toast.LENGTH_SHORT).show()
        }

    }

    private fun addEvent(){
        val intent = Intent(this, EditActivity::class.java)
        startActivityForResult(intent, 1)
    }

    private fun editEvent(){
        val intent = Intent(this, EditActivity::class.java)
        val event = tv_display.text
        intent.putExtra("event", event)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
//            tv_display.text = data?.getStringExtra("newEvent")
            loadEvent()
            //isAddMode = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        var add = menu?.findItem(R.id.menu_item_add)
        add?.setOnMenuItemClickListener {
            if (isAddMode) addEvent()
            else Toast.makeText(this, "已經新增過了，不能再新增囉。", Toast.LENGTH_SHORT).show()
            true
        }
        return true
        //return super.onCreateOptionsMenu(menu)
    }
    private fun loadEvent(){
        var  loader = getSharedPreferences("MySP", Context.MODE_PRIVATE)
        tv_display.text = loader.getString("myNote","")
        isAddMode =(loader.getString("myNote","").isNullOrEmpty())
    }
}

