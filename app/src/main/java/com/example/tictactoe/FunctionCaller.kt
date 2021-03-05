package com.example.tictactoe

class FunctionCaller(private var function: () -> Unit): Runnable {

    private var activated = false

    fun activate() {
        activated = true
    }

    override fun run() {
        while (true) {
            if (activated) {
                for (i in 0..9) {
                    function()
                    Thread.sleep(50)
                }
                activated = false
            }
        }
    }

}