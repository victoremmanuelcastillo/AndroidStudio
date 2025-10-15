package com.example.myapplicasion

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PreferenciasActivity : AppCompatActivity() {

    lateinit var edt1: EditText
    lateinit var edt2: EditText
    lateinit var txt1: TextView
    lateinit var txt2: TextView
    lateinit var btn1: Button
    lateinit var btn2: Button
    var conector: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferencias)

        edt1 = findViewById(R.id.editText1)
        edt2 = findViewById(R.id.editText2)
        txt1 = findViewById(R.id.textView1)
        txt2 = findViewById(R.id.textView2)
        btn1 = findViewById(R.id.button1)
        btn2 = findViewById(R.id.button2)

        btn1.setOnClickListener {
            conector = getSharedPreferences("datos", MODE_PRIVATE)
            editor = conector?.edit()

            editor?.putString("matricula", edt1.text.toString())
            editor?.putString("nombre", edt2.text.toString())

            editor?.commit()

            txt1.setText(edt1.text.toString())
            txt2.setText(edt2.text.toString())
        }

        btn2.setOnClickListener {
            conector = getSharedPreferences("datos", MODE_PRIVATE)

            val matricula = conector?.getString("matricula", "")
            val nombre = conector?.getString("nombre", "")

            txt1.setText(matricula)
            txt2.setText(nombre)
        }
    }
}
