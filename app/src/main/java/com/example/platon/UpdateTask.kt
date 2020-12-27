package com.example.platon

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_new_task.*
import kotlinx.android.synthetic.main.activity_update_task.*
import kotlinx.android.synthetic.main.activity_update_task.datedoes
import kotlinx.android.synthetic.main.activity_update_task.descdoes
import java.text.SimpleDateFormat
import java.util.*


class UpdateTask : AppCompatActivity() {

    private lateinit var myRef: DatabaseReference
    val database = FirebaseDatabase.getInstance()
    val calender: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_task)

        var realDate: String = intent.getStringExtra("dateplan")
        val keyID = intent.getStringExtra("key")
        titleupdate.setText(intent.getStringExtra("titleplan"))
        descdoes.setText(intent.getStringExtra("descplan"))
        datedoes.setText(realDate)
//        Toast.makeText(applicationContext, keyID, Toast.LENGTH_SHORT).show()


        val date =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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
                this, date, calender
                    .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        val pDialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        btnSaveUpdate.setOnClickListener {
            myRef = database.getReference("PlatonApp").child(keyID)
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.ref.child("titledoes")
                        .setValue(titleupdate.text.toString())
                    dataSnapshot.ref.child("descdoes")
                        .setValue(descdoes.text.toString())
                        dataSnapshot.ref.child("datedoes")
                            .setValue(realDate)


                    pDialog.titleText = "Updated!"
                    pDialog.contentText = "Success Update your Plan!"
                    pDialog.show()
                    pDialog.findViewById<Button>(R.id.confirm_button).setOnClickListener {
                        startActivity(Intent(this@UpdateTask, MainActivity::class.java))
                        pDialog.hide()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

//        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
//        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
//        pDialog.titleText = "Loading"
//        pDialog.setCancelable(false)
//        pDialog.show()
//        val pDialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
//
//        btnDeletePlan.setOnClickListener {
//            database.getReference("PlatonApp").child(keyID).removeValue().addOnCompleteListener {
//                if(it.isSuccessful){
//                    pDialog.titleText = "Deleted!"
//                    pDialog.contentText = "Success delete your Plan!"
//                    pDialog.show()
//                    pDialog.findViewById<Button>(R.id.confirm_button).setOnClickListener {
//                        startActivity(Intent(this@UpdateTask, MainActivity::class.java))
//                        pDialog.hide()
//                    }
//                }else if(it.isCanceled){
//                    Toast.makeText(applicationContext, "Failed to delete Plan", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//
        btnCancelUpdate.setOnClickListener {
            startActivity(Intent(this@UpdateTask, MainActivity::class.java))
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@UpdateTask, MainActivity::class.java))
    }
}
