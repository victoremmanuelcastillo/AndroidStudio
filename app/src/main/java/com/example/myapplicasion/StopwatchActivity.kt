package com.example.myapplicasion

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StopwatchActivity : AppCompatActivity() {
    private lateinit var timerText: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var resetButton: Button
    private lateinit var lapButton: Button
    private lateinit var lapRecyclerView: RecyclerView
    private lateinit var lapAdapter: LapAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var startTime = 0L
    private var timeInMilliseconds = 0L
    private var timeSwapBuff = 0L
    private var updatedTime = 0L
    private var isRunning = false
    private val laps = mutableListOf<Lap>()
    private var lapCounter = 1

    private val updateTimerThread = object : Runnable {
        override fun run() {
            timeInMilliseconds = System.currentTimeMillis() - startTime
            updatedTime = timeSwapBuff + timeInMilliseconds

            val minutes = (updatedTime / 60000).toInt()
            val seconds = ((updatedTime % 60000) / 1000).toInt()
            val milliseconds = ((updatedTime % 1000) / 10).toInt()

            timerText.text = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
            handler.postDelayed(this, 10)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stopwatch)

        timerText = findViewById(R.id.timerText)
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)
        resetButton = findViewById(R.id.resetButton)
        lapButton = findViewById(R.id.lapButton)
        lapRecyclerView = findViewById(R.id.lapRecyclerView)

        lapAdapter = LapAdapter(laps)
        lapRecyclerView.layoutManager = LinearLayoutManager(this)
        lapRecyclerView.adapter = lapAdapter

        startButton.setOnClickListener {
            if (!isRunning) {
                startTime = System.currentTimeMillis()
                handler.postDelayed(updateTimerThread, 0)
                isRunning = true
            }
        }

        pauseButton.setOnClickListener {
            if (isRunning) {
                timeSwapBuff += timeInMilliseconds
                handler.removeCallbacks(updateTimerThread)
                isRunning = false
            }
        }

        resetButton.setOnClickListener {
            handler.removeCallbacks(updateTimerThread)
            isRunning = false
            startTime = 0L
            timeInMilliseconds = 0L
            timeSwapBuff = 0L
            updatedTime = 0L
            timerText.text = "00:00:00"
            laps.clear()
            lapAdapter.notifyDataSetChanged()
            lapCounter = 1
        }

        lapButton.setOnClickListener {
            if (isRunning || updatedTime > 0) {
                val currentTime = timerText.text.toString()
                laps.add(0, Lap(lapCounter, currentTime))
                lapAdapter.notifyItemInserted(0)
                lapRecyclerView.scrollToPosition(0)
                lapCounter++
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimerThread)
    }
}
