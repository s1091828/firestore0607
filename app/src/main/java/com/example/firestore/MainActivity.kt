package com.example.firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.firestore.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    var user: MutableMap<String, Any> = HashMap()
    //lateinit var binding: ActivityMainBinding
    lateinit var btnUpdate: Button
    lateinit var edtName:EditText
    lateinit var edtWeight:EditText

    lateinit var btnQuery:Button
    lateinit var txv:TextView

    lateinit var btnDelete:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        btnUpdate = findViewById(R.id.btnUpdate)
        edtName = findViewById(R.id.edtName)
        edtWeight = findViewById(R.id.edtWeight)

        btnUpdate.setOnClickListener({
            user["名字"] = edtName.text.toString()
            user["出生體重"] = edtWeight.text.toString().toInt()
            db.collection("Users")
                //.document(binding.edtName.text.toString())
                //.set(user)
                .add(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "新增/異動資料成功",
                        Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "新增/異動資料失敗：" + e.toString(),
                        Toast.LENGTH_LONG).show()
                }
        })

        btnQuery = findViewById(R.id.btnQuery)
        txv = findViewById(R.id.txv)

        btnQuery.setOnClickListener({
            db.collection("Users")
                //.whereEqualTo("名字", binding.edtName.text.toString())
                //.whereLessThan("出生體重", binding.edtWeight.text.toString().toInt())
                .orderBy("出生體重", Query.Direction.DESCENDING)
                .limit(5)
            .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var msg: String = ""
                        for (document in task.result!!) {
                            msg += "文件id：" + document.id + ":\n名字：" + document.data["名字"] +
                                    "\n出生體重：" + document.data["出生體重"].toString() + "\n\n"
                        }
                        if (msg != "") {
                            txv.text = msg
                        } else {
                            txv.text = "查無資料"
                        }
                    }
                }
        })

        btnDelete = findViewById(R.id.btnDelete)

        //流水號的資料無法刪除
        btnDelete.setOnClickListener({
            db.collection("Users")
                .document(edtName.text.toString())
                .delete()
            Toast.makeText(this, "刪除資料", Toast.LENGTH_LONG).show()
        })

    }
}