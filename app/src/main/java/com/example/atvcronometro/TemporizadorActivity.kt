package com.example.atvcronometro

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class TemporizadorActivity : AppCompatActivity() {

        private lateinit var viewTemporizador: TextView
        private lateinit var buttonStartTemporizador: Button
        private lateinit var buttonResetTemporizador: Button
        private lateinit var buttonCronometro: Button
        // Removi buttonTemporizador pois esta é a activity do Temporizador

        private var countDownTimer: CountDownTimer? = null
        private var timerDurationMillis: Long = 0
        private var timeLeftInMillis: Long = 0 // Para pausar e retomar
        private var running: Boolean = false

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_temporizador) // Certifique-se que o nome do seu layout XML está correto

            viewTemporizador = findViewById(R.id.viewTemporizador)
            buttonStartTemporizador =
                findViewById(R.id.buttonStart) // Use o ID correto do seu botão Start
            buttonResetTemporizador =
                findViewById(R.id.buttonReset) // Use o ID correto do seu botão Reset
            buttonCronometro =
                findViewById(R.id.cronometro) // Use o ID correto do seu botão Cronometro

            // Configurar o clique no TextView para definir o tempo
            viewTemporizador.setOnClickListener {
                if (!running) { // Permite definir o tempo apenas se não estiver rodando
                    showTimePickerDialog()
                }
            }

            buttonStartTemporizador.setOnClickListener {
                onClickStart()
            }

            buttonResetTemporizador.setOnClickListener {
                onClickReset()
            }

            buttonCronometro.setOnClickListener {
                onClickCronometro()
            }

            // Inicializa o display do temporizador
            updateTimerDisplay(timerDurationMillis)
        }

        private fun showTimePickerDialog() {
            val calendar = Calendar.getInstance()
            val initialHour: Int
            val initialMinute: Int

            if (timerDurationMillis > 0) {
                initialHour = (timerDurationMillis / (1000 * 60 * 60) % 24).toInt()
                initialMinute = (timerDurationMillis / (1000 * 60) % 60).toInt()
            } else {
                // Define um valor padrão se for 0, por exemplo, 0 horas e 0 minutos
                // ou pode usar a hora atual como antes, dependendo da preferência
                initialHour = 0
                initialMinute = 0
                // Ou, se preferir a hora atual como ponto de partida para seleção:
                // initialHour = calendar.get(Calendar.HOUR_OF_DAY)
                // initialMinute = calendar.get(Calendar.MINUTE)
            }

            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    timerDurationMillis = (hourOfDay * 60 * 60 * 1000 + minute * 60 * 1000).toLong()
                    timeLeftInMillis =
                        timerDurationMillis // Define o tempo restante para o total ao definir um novo tempo
                    updateTimerDisplay(timeLeftInMillis)
                    // Se o timer estava em pausa e um novo tempo é definido,
                    // pode ser bom resetar o estado dos botões (ex: "START" em vez de "CONTINUAR")
                    buttonStartTemporizador.text = "START"
                },
                initialHour,
                initialMinute,
                true // true para formato 24 horas, false para AM/PM
            )
            // Opcional: Definir um título para o diálogo
            timePickerDialog.setTitle("Definir Duração do Timer")
            timePickerDialog.show()
        }

        private fun onClickStart() {
            if (running) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        private fun startTimer() {
            if (timeLeftInMillis == 0L && timerDurationMillis > 0L) { // Se começando do zero ou após reset com nova duração
                timeLeftInMillis = timerDurationMillis
            }

            if (timeLeftInMillis > 0) {
                countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        timeLeftInMillis = millisUntilFinished
                        updateTimerDisplay(timeLeftInMillis)
                    }

                    override fun onFinish() {
                        running = false
                        timeLeftInMillis = 0 // Garante que zerou
                        updateTimerDisplay(timeLeftInMillis)
                        buttonStartTemporizador.text = "START"
                        // Adicione aqui notificação ou som se desejar
                    }
                }.start()
                running = true
                buttonStartTemporizador.text = "PAUSE" // Ou o texto que você preferir para pausar
            }
        }

        private fun pauseTimer() {
            countDownTimer?.cancel()
            running = false
            buttonStartTemporizador.text =
                "CONTINUAR" // Ou o texto que você preferir para continuar
        }

        private fun onClickReset() {
            countDownTimer?.cancel()
            running = false
            timerDurationMillis = 0 // Reseta a duração definida pelo usuário
            timeLeftInMillis = 0    // Reseta o tempo restante
            updateTimerDisplay(timeLeftInMillis)
            buttonStartTemporizador.text = "START"
        }

        private fun updateTimerDisplay(millis: Long) {
            val hours = (millis / (1000 * 60 * 60)) % 24
            val minutes = (millis / (1000 * 60)) % 60
            val seconds = (millis / 1000) % 60
            val timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            viewTemporizador.text = timeLeftFormatted
        }

        private fun onClickCronometro() {
            val intent = Intent(this, MainActivity::class.java) // Use 'this' como Context
            startActivity(intent)
        }


        override fun onDestroy() {
            super.onDestroy()
            countDownTimer?.cancel()
        }
    }

