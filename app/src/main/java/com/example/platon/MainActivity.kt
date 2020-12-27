package com.example.platon

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    val list = ArrayList<Data>()
    val database = FirebaseDatabase.getInstance()
    private lateinit var dataAdapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnAddNew.setOnClickListener(View.OnClickListener {
            val a = Intent(this@MainActivity, addNewTask::class.java)
            startActivity(a)
        })


        dataAdapter = Adapter(this@MainActivity,list)
        rvPlan.apply {
            layoutManager=LinearLayoutManager(this@MainActivity)
            adapter= dataAdapter
            setHasFixedSize(true)
        }

        val progressBar = findViewById<View>(R.id.spin_kit) as ProgressBar
        val shapeload: Sprite = WanderingCubes()
        progressBar.indeterminateDrawable = shapeload

        // get data from firebase
        val myRef = database.getReference("PlatonApp")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                if (dataSnapshot.exists()) {
                    for (dataSnapshot1 in dataSnapshot.children) {
                        val data : Data = dataSnapshot1.getValue(Data::class.java) as Data
                        data.key=dataSnapshot1.key.toString()

                        list.add(data)
                        progressBar.visibility = View.GONE
                        dataAdapter.notifyDataSetChanged()
                    }
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "No Data", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // set code to show an error
                Toast.makeText(applicationContext, "No Data", Toast.LENGTH_SHORT).show()
            }
        })
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Get Post object and use the values to update the UI
//                val data : Data = dataSnapshot.getValue(Data::class.java) as Data
//                list.add(data)
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
//                Toast.makeText(applicationContext, "No Data", Toast.LENGTH_SHORT).show()
//            }
//        }
//        myRef.addValueEventListener(postListener)

//        dataAdapter.notifyDataSetChanged()


    }

    var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Double click Back to Exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}