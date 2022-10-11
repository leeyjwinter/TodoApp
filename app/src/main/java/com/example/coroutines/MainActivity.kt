package com.example.coroutines

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var DataList = ArrayList<Data>()
    val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val resultData = it.data?.getStringExtra("result")
        println("result data is : " + resultData.toString())
        DataList+=Data(R.drawable.todo, resultData.toString())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //db store
        val db = DBHelper(this).readableDatabase
        val cursor = db.rawQuery("select * from TODO_TB" , null)
        cursor.run{
            while (moveToNext()){
                DataList+=Data(R.drawable.todo, cursor.getString(1))
            }
        }


        //itemOnclick
        val adapter = MyAdapter(DataList)
        adapter.onItemClickListener = object : MyAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                println("clicked position ${position} :  ${DataList[position].todo}")
//                db.execSQL(
//                    "delete from TODO_TB where todo = ${DataList[position].todo}"
//                )
                DataList-=DataList[position]
                adapter.notifyDataSetChanged()
            }
        }

        main_recyclerView.layoutManager = LinearLayoutManager(this)
        main_recyclerView.adapter = adapter


        //화면 켬/끔 에 따른 브로드캐스트 리시버
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_SCREEN_ON -> println("screen on")
                    Intent.ACTION_SCREEN_OFF -> println("screen off")
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON).apply {
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(receiver, filter)




        //intent
        main_fab.setOnClickListener() {
            val intent: Intent = Intent(this, AddActivity::class.java)
            requestLauncher.launch(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        val setting_btn = menu?.findItem(R.id.menu_settings)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_settings -> {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            true
        }
        else -> true
    }







}