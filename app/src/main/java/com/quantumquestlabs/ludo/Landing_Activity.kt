package com.quantumquestlabs.ludo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.quantumquestlabs.ludo.databinding.ActivityLandingBinding

class Landing_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding // Declare binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        enableEdgeToEdge()
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun startGame(view: View) {
        val player1Name = binding.editTextPlayer1.text.toString()
        val player2Name = binding.editTextPlayer2.text.toString()
        val targetScore = binding.editTextTargetScore.text.toString().toIntOrNull() ?: return

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("PLAYER1_NAME", player1Name)
        intent.putExtra("PLAYER2_NAME", player2Name)
        intent.putExtra("TARGET_SCORE", targetScore)
        startActivity(intent)
    }

    private fun enableEdgeToEdge() {
        window.decorView.systemUiVisibility = (
                window.decorView.systemUiVisibility
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
    }
}
