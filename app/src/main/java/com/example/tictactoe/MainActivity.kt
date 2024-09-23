package com.example.tictactoe

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.playerCpuButton
import kotlinx.android.synthetic.main.activity_main.playerPlayerButton
import kotlinx.android.synthetic.main.activity_main.quitButton
import kotlinx.android.synthetic.main.activity_main.resumeButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("gameStatus", Context.MODE_PRIVATE)

        playerPlayerButton.rootView.setBackgroundColor(Color.BLACK)

        playerPlayerButton.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        playerCpuButton.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java).putExtra("cpu", true))
        }

        quitButton.setOnClickListener {
            finish()
        }

        resumeButton.setOnClickListener {
            if (sharedPreferences.contains("initialized")) {
                startActivity(
                    Intent(this, GameActivity::class.java).putExtra("resuming", true)
                        .putExtra("cpu", sharedPreferences.getBoolean("cpu", false))
                )
            } else {
                Toast.makeText(applicationContext, "No game to resume", Toast.LENGTH_SHORT).show()
            }
        }
    }
}