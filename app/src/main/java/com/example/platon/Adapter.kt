package com.example.platon

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_adapter.view.*


class Adapter(private val context : MainActivity, private val dataAdapter: ArrayList<Data>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dataAdapter.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(dataAdapter[position])
        val item = dataAdapter[position]

        holder.titlePlan.text = item.titledoes
        holder.descPlan.text = item.descdoes
        holder.datePlan.text = item.datedoes

        holder.itemView.setOnClickListener(View.OnClickListener {

            val aa = Intent(context, UpdateTask::class.java)

            aa.putExtra("titleplan", item.titledoes)
            aa.putExtra("descplan", item.descdoes)
            aa.putExtra("dateplan", item.datedoes)
            aa.putExtra("key", item.key)
//            context.startActivity(aa)

            val keyID = item.key.toString()
            val pDialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)

            val items =
                arrayOf<CharSequence>("Edit", "Hapus", "Cancel")
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Option")
            builder.setItems(items, DialogInterface.OnClickListener { dialog, item ->
                when (item) {
                    0 -> {
                        context.startActivity(aa)
                    }
                    1 -> {


                        val database = FirebaseDatabase.getInstance()
                        database.getReference("PlatonApp").child(keyID).addListenerForSingleValueEvent(object :
                            ValueEventListener {

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.ref.removeValue()
                                pDialog.titleText = "Deleted!"
                                pDialog.contentText = "Success delete your Plan!"
                                pDialog.show()
                                pDialog.findViewById<Button>(R.id.confirm_button)
                                    .setOnClickListener {
                                        pDialog.hide()
                                    }
                            }
                        })

                    }
                    2 -> {
                    }
                }
            })
            val alert: AlertDialog = builder.create()
            alert.show()
        })
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titlePlan = view.titleAdapter
        val descPlan = view.descAdapter
        val datePlan = view.dateAdapter
    }
}
