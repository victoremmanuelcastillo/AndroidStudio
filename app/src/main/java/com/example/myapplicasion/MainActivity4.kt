package com.example.myapplicasion

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity4 : AppCompatActivity() {

    private lateinit var mainConstraint: ConstraintLayout
    private lateinit var btnBlue: Button
    private lateinit var btnYellow: Button
    private lateinit var radioGroupFill: RadioGroup
    private lateinit var radioFill1: RadioButton
    private lateinit var radioFill2: RadioButton
    private lateinit var radioFill3: RadioButton
    private lateinit var imgDisplay: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        // Initialize views
        mainConstraint = findViewById(R.id.main_constraint)
        btnBlue = findViewById(R.id.btn_blue)
        btnYellow = findViewById(R.id.btn_yellow)
        radioGroupFill = findViewById(R.id.radio_group_fill)
        radioFill1 = findViewById(R.id.radio_fill_1)
        radioFill2 = findViewById(R.id.radio_fill_2)
        radioFill3 = findViewById(R.id.radio_fill_3)
        imgDisplay = findViewById(R.id.img_display)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Blue button - change background to blue
        btnBlue.setOnClickListener {
            mainConstraint.setBackgroundColor(resources.getColor(android.R.color.holo_blue_light))
        }

        // Yellow button - change background to yellow
        btnYellow.setOnClickListener {
            mainConstraint.setBackgroundColor(resources.getColor(android.R.color.holo_orange_light))
        }

        // RadioGroup listener for fill options
        radioGroupFill.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_fill_1 -> showImage(R.mipmap.ego)
                R.id.radio_fill_2 -> showImage(R.mipmap.egob)
                R.id.radio_fill_3 -> showImage(R.mipmap.ejof)
            }
        }
    }

    private fun showImage(imageResource: Int) {
        imgDisplay.setImageResource(imageResource)
        imgDisplay.visibility = android.view.View.VISIBLE
    }
}