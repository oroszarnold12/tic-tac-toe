package com.example.tictactoe

import android.content.SharedPreferences
import android.widget.Button
import android.widget.TextView

class Board(
    private val buttons: Array<Button>,
    private val message: TextView,
    private val functionCaller: FunctionCaller,
    private var cameraAccessGranted: Boolean,
    private val sharedPreferences: SharedPreferences,
    private val isAgainstCpu: Boolean
) {

    private var round = 0
    private val cpu: Cpu = Cpu(this)

    init {
        val msg = "Player X's turn"
        message.text = msg
        for (i in 0..8) {
            buttons[i].tag = ""
        }
    }

    private fun disableButtons() {
        for (i in 0..8) {
            buttons[i].isEnabled = false
        }
    }

    private fun checkWinner() {

        if (buttons[0].tag == buttons[4].tag && buttons[4].tag == buttons[8].tag && buttons[8].tag != "") {
            animateButton(buttons[0])
            animateButton(buttons[4])
            animateButton(buttons[8])
            val winner = "Player " + buttons[0].tag + " Wins!"
            message.text = winner
            disableButtons()
            if (cameraAccessGranted) {
                functionCaller.activate()
            }
            return
        }

        if (buttons[2].tag == buttons[4].tag && buttons[4].tag == buttons[6].tag && buttons[6].tag != "") {
            animateButton(buttons[2])
            animateButton(buttons[4])
            animateButton(buttons[6])
            val winner = "Player " + buttons[2].tag + " Wins!"
            message.text = winner
            disableButtons()
            if (cameraAccessGranted) {
                functionCaller.activate()
            }
            return
        }

        for (i in 0..2) {
            if (buttons[i].tag == buttons[i + 3].tag && buttons[i + 3].tag == buttons[i + 6].tag && buttons[i + 6].tag != "") {
                animateButton(buttons[i])
                animateButton(buttons[i + 3])
                animateButton(buttons[i + 6])
                val winner = "Player " + buttons[i].tag + " Wins!"
                message.text = winner
                disableButtons()
                if (cameraAccessGranted) {
                    functionCaller.activate()
                }
                return
            }

            if (buttons[3 * i].tag == buttons[3 * i + 1].tag && buttons[3 * i + 1].tag == buttons[3 * i + 2].tag && buttons[3 * i + 2].tag != "") {
                animateButton(buttons[3 * i])
                animateButton(buttons[3 * i + 1])
                animateButton(buttons[3 * i + 2])
                val winner = "Player " + buttons[3 * i].tag + " Wins!"
                message.text = winner
                disableButtons()
                if (cameraAccessGranted) {
                    functionCaller.activate()
                }
                return
            }
        }

        for (i in 0..8) {
            if (buttons[i].tag == "") {
                return
            }
        }

        val draw = "Draw!"
        message.text = draw
    }

    fun onClick(i: Int, notCpu: Boolean) {
        if (buttons[i].tag == "" && buttons[i].isEnabled) {
            if (round % 2 == 0) {
                val msg = "Player 0's turn"
                message.text = msg
                buttons[i].setBackgroundResource(R.drawable.image_x)
                buttons[i].tag = "X"
            } else {
                val msg = "Player X's turn"
                message.text = msg
                buttons[i].setBackgroundResource(R.drawable.image_0)
                buttons[i].tag = "0"
            }
            round++
            if (notCpu && isAgainstCpu) {
                cpu.makeMove()
            }
            checkWinner()
        }
    }

    fun restart() {
        for (i in 0..8) {
            buttons[i].setBackgroundResource(0)
            buttons[i].tag = ""
            buttons[i].isEnabled = true
            when (round % 2) {
                0 -> {
                    val msg = "Player X's turn"
                    message.text = msg
                }

                else -> {
                    val msg = "Player 0's turn"
                    message.text = msg
                }
            }
        }
    }

    fun setCameraAccessGranted(value: Boolean) {
        cameraAccessGranted = value
    }

    fun saveState() {
        val sharedPreferencesEditor = sharedPreferences.edit()
        sharedPreferencesEditor.clear()
        sharedPreferencesEditor.putInt("round", round)
        sharedPreferencesEditor.putString("message", message.text as String)
        sharedPreferencesEditor.putBoolean("cpu", isAgainstCpu)
        for (i in 0..8) {
            sharedPreferencesEditor.putString("buttontag$i", buttons[i].tag as String)
            sharedPreferencesEditor.putBoolean("buttonenabled$i", buttons[i].isEnabled)
        }
        sharedPreferencesEditor.putBoolean("initialized", true)
        sharedPreferencesEditor.apply()
        sharedPreferencesEditor.commit()
    }

    fun loadState() {
        round = sharedPreferences.getInt("round", 0)
        message.text = sharedPreferences.getString("message", "")
        for (i in 0..8) {
            buttons[i].tag = sharedPreferences.getString("buttontag$i", "")
            buttons[i].isEnabled = sharedPreferences.getBoolean("buttonenabled$i", true)
            when (buttons[i].tag) {
                "X" -> {
                    buttons[i].setBackgroundResource(R.drawable.image_x)
                }

                "0" -> {
                    buttons[i].setBackgroundResource(R.drawable.image_0)
                }
            }
        }
    }

    private fun animateButton(button: Button) {
        button.animate().apply {
            duration = 2000
            rotationYBy(1080f)
        }
    }

    fun getButtons(): Array<Button> {
        return buttons
    }

    fun getRound(): Int {
        return round
    }
}