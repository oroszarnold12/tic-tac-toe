package com.example.tictactoe

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_game.button00
import kotlinx.android.synthetic.main.activity_game.button01
import kotlinx.android.synthetic.main.activity_game.button02
import kotlinx.android.synthetic.main.activity_game.button10
import kotlinx.android.synthetic.main.activity_game.button11
import kotlinx.android.synthetic.main.activity_game.button12
import kotlinx.android.synthetic.main.activity_game.button20
import kotlinx.android.synthetic.main.activity_game.button21
import kotlinx.android.synthetic.main.activity_game.button22
import kotlinx.android.synthetic.main.activity_game.flashLightCheckBox
import kotlinx.android.synthetic.main.activity_game.menuButton
import kotlinx.android.synthetic.main.activity_game.message
import kotlinx.android.synthetic.main.activity_game.restartButton

class GameActivity : AppCompatActivity() {

    private val functionCaller = FunctionCaller { switchFlashLight() }
    private val thread = Thread(functionCaller)

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private var flashStatus = true
    private val requestPermissionCode = 123

    private lateinit var board: Board

    private lateinit var sharedPreferences: SharedPreferences

    private var resuming: Boolean = false
    private var isAgainstCpu: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        message.rootView.setBackgroundColor(Color.BLACK)

        resuming = intent.getBooleanExtra("resuming", false)
        isAgainstCpu = intent.getBooleanExtra("cpu", false)

        sharedPreferences = getSharedPreferences("gameStatus", Context.MODE_PRIVATE)

        thread.start()

        var cameraAccessGranted = false

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                requestPermissionCode
            )
        } else {
            cameraAccessGranted = true
        }

        val isFlashAvailable =
            applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
        if (!isFlashAvailable) {
            showNoFlashError()
        }
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        val buttons = arrayOf(
            button00, button01, button02,
            button10, button11, button12,
            button20, button21, button22
        )

        board = Board(
            buttons,
            message,
            functionCaller,
            cameraAccessGranted,
            sharedPreferences,
            isAgainstCpu
        )

        button00.setOnClickListener {
            board.onClick(0, true)
        }

        button01.setOnClickListener {
            board.onClick(1, true)
        }

        button02.setOnClickListener {
            board.onClick(2, true)
        }

        button10.setOnClickListener {
            board.onClick(3, true)
        }

        button11.setOnClickListener {
            board.onClick(4, true)
        }

        button12.setOnClickListener {
            board.onClick(5, true)
        }

        button20.setOnClickListener {
            board.onClick(6, true)
        }

        button21.setOnClickListener {
            board.onClick(7, true)
        }

        button22.setOnClickListener {
            board.onClick(8, true)
        }

        restartButton.setOnClickListener {
            board.restart()
        }

        menuButton.setOnClickListener {
            finish()
        }

        flashLightCheckBox.setOnClickListener {
            board.setCameraAccessGranted(flashLightCheckBox.isChecked)
        }

        if (resuming) {
            board.loadState()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestPermissionCode -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    board.setCameraAccessGranted(true)
                }
            }
        }
    }

    private fun showNoFlashError() {
        val alert = AlertDialog.Builder(this)
            .create()
        alert.setTitle("Oops!")
        alert.setMessage("Flash not available in this device...")
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK") { _, _ -> finish() }
        alert.show()
    }

    private fun switchFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, flashStatus)
                flashStatus = flashStatus.not()
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun finish() {
        super.finish()
        board.saveState()
    }

    override fun onDestroy() {
        super.onDestroy()
        board.saveState()
    }
}