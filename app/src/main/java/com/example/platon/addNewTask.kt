package com.example.platon

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_new_task.*
import java.text.SimpleDateFormat
import java.util.*


class addNewTask : AppCompatActivity() {
    private lateinit var myRef: DatabaseReference
    val database = FirebaseDatabase.getInstance()
    val calender: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_task)

        var realDate:String = ""
        val date =
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calender.set(Calendar.YEAR, year)
                calender.set(Calendar.MONTH, monthOfYear)
                calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd/MM/yy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                realDate = sdf.format(calender.time)
                datedoes.setText(realDate)
            }

        datedoes.setOnClickListener {
            DatePickerDialog(
                this@addNewTask, date, calender
                    .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnSaveTask.setOnClickListener(View.OnClickListener {
            myRef = database.reference.child("PlatonApp").push()
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.ref.child("titledoes")
                        .setValue(titledoes.text.toString())
                    dataSnapshot.ref.child("descdoes")
                        .setValue(descdoes.text.toString())
                    dataSnapshot.ref.child("datedoes")
                        .setValue(realDate)

                    startActivity(Intent(this@addNewTask, MainActivity::class.java))
                    finish()
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        })

        btnCancel.setOnClickListener {
            startActivity(Intent(this@addNewTask, MainActivity::class.java))
            finishAffinity()
        }

    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@addNewTask, MainActivity::class.java))
        finishAffinity()
    }
}