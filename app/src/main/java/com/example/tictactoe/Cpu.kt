package com.example.tictactoe

import kotlin.math.max
import kotlin.math.min

class Cpu(private var board: Board) {

    private lateinit var buttons: Array<Int>
    private var round = -1
    private var x = -1
    private lateinit var cpuPlayer: String

    private fun init() {
        round = board.getRound()
        x = -1
        when (round % 2) {
            0 -> {
                cpuPlayer = "X"
            }
            1 -> {
                cpuPlayer = "0"
            }
        }

        buttons = arrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1)

        for (i in 0..8) {
            val player = board.getButtons()[i].tag
            if (player == cpuPlayer) {
                buttons[i] = 1
            } else {
                if (player == "") {
                    buttons[i] = 0
                } else {
                    buttons[i] = -1
                }
            }
        }
    }

    private fun checkWinner(buttons: Array<Int>): Int {

        if (buttons[0] == buttons[4] && buttons[4] == buttons[8] && buttons[8] != 0) {
            return buttons[0]
        }

        if (buttons[2] == buttons[4] && buttons[4] == buttons[6] && buttons[6] != 0) {
            return buttons[2]
        }

        for (i in 0..2) {
            if (buttons[i] == buttons[i + 3] && buttons[i + 3] == buttons[i + 6] && buttons[i + 6] != 0) {
                return buttons[i]
            }

            if (buttons[3 * i] == buttons[3 * i + 1] && buttons[3 * i + 1] == buttons[3 * i + 2] && buttons[3 * i + 2] != 0) {
                return buttons[3 * i]
            }
        }
        for (i in 0..8) {
            if (buttons[i] == 0) {
                return 3
            }
        }
        return 0
    }

    private fun minValue(buttons: Array<Int>): Int {
        var v = 10000
        if (checkWinner(buttons) != 3) {
            return checkWinner(buttons)
        }
        for (i in 0..8) {
            if (buttons[i] == 0) {
                buttons[i] = -1
                v = min(v, maxValue(buttons))
                buttons[i] = 0
            }
        }
        return v
    }

    private fun maxValue(buttons: Array<Int>): Int {
        var v = -10000
        if (checkWinner(buttons) != 3) {
            return checkWinner(buttons)
        }
        for (i in 0..8) {
            if (buttons[i] == 0) {
                buttons[i] = 1
                v = max(v, minValue(buttons))
                buttons[i] = 0
            }
        }
        return v
    }

    private fun minimax(buttons: Array<Int>) {
        var v = -10000
        for (i in 0..8) {
            if (buttons[i] == 0) {
                buttons[i] = 1
                val moveValue = minValue(buttons)
                if (v < moveValue) {
                    v = moveValue
                    x = i
                }
                buttons[i] = 0
            }
        }
    }

    fun makeMove() {
        init()
        minimax(buttons)
        if (x != -1) {
            board.onClick(x, false)
        }
    }
}