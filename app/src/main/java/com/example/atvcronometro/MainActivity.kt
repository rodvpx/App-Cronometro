package com.example.atvcronometro

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var startTime = 0L
    private var elapsedBeforePause = 0L
    private var execution = false
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        runTimer()
    }


    fun onClickStart(view: View) {
        val button = view as Button

        if (!execution) {
            execution = true
            startTime = System.currentTimeMillis()
            button.text = "Pause"
        } else {
            elapsedBeforePause += System.currentTimeMillis() - startTime
            execution = false
            button.text = "Resume"
        }
    }

    fun onClickReset(view: View) {
        execution = false
        startTime = 0L
        elapsedBeforePause = 0L
        updateTimerUI(0)
    }

    fun runTimer() {
        job = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                if (execution) {
                    val elapsed = elapsedBeforePause + (System.currentTimeMillis() - startTime)
                    updateTimerUI(elapsed)
                } else {
                    updateTimerUI(elapsedBeforePause)
                }
                delay(50L)
            }
        }

    }

    fun updateTimerUI(elapsed: Long) {

        val hours = (elapsed / 3600000)
        val minutes = (elapsed % 3600000) / 60000
        val secs = (elapsed % 60000) / 1000
        val millis = (elapsed % 1000) / 10

        val time = String.format("%02d:%02d:%02d:%02d", hours, minutes, secs, millis)
        val timeView = findViewById<TextView>(R.id.viewCronometro)
        timeView.text = time
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
