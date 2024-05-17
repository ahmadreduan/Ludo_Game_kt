package com.quantumquestlabs.ludo

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.quantumquestlabs.ludo.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Declare binding variable
    private val ROLL_DURATION = 500L
    private val ROLL_INTERVAL = 300L
    private var WIN_SCORE = 20 // Score required to win the game, will be set from intent extra
    private val dices = listOf(
        R.drawable.empty_dice,
        R.drawable.dice_1,
        R.drawable.dice_2,
        R.drawable.dice_3,
        R.drawable.dice_4,
        R.drawable.dice_5,
        R.drawable.dice_6
    )
    private var currentPlayer = 1 // Variable to keep track of the current player
    private var player1Score = 0 // Player 1's score
    private var player2Score = 0 // Player 2's score
    private var currentDiceNum = 0 // Current rolled dice number
    private var isPlayer1Rolling = false // Flag to track if Player 1 is rolling
    private var isPlayer2Rolling = false // Flag to track if Player 2 is rolling
    private var player1Name = "" // Player 1's name
    private var player2Name = "" // Player 2's name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the target score and player names from the intent extras
        intent.extras?.let {
            WIN_SCORE = it.getInt("TARGET_SCORE", 20)
            player1Name = it.getString("PLAYER1_NAME", "") ?: ""
            player2Name = it.getString("PLAYER2_NAME", "") ?: ""
        }

        // Display player names on the scoreboard
        binding.tvPlayer1Score.text = "$player1Name Total Score: $player1Score"
        binding.tvPlayer2Score.text = "$player2Name Total Score: $player2Score"

        binding.btnRollPlayer1.setOnClickListener {
            if (!isPlayer2Rolling) {
                diceRoll(binding.imgDicePlayer1, binding.btnRollPlayer2)
                isPlayer1Rolling = true
                isPlayer2Rolling = false
            }
        }

        binding.btnRollPlayer2.setOnClickListener {
            if (!isPlayer1Rolling) {
                diceRoll(binding.imgDicePlayer1, binding.btnRollPlayer1)
                isPlayer1Rolling = false
                isPlayer2Rolling = true
            }
        }
    }

    private fun diceRoll(imageView: ImageView, opponentButton: Button) {
        val countDownTimer = object : CountDownTimer(ROLL_DURATION, ROLL_INTERVAL) {
            override fun onTick(p0: Long) {
                selectedDice(imageView)
                imageView.isEnabled = false // Disable button during roll
                opponentButton.isEnabled = false // Disable opponent's roll button
            }

            override fun onFinish() {
                imageView.isEnabled = true // Re-enable button after roll
                opponentButton.isEnabled = true // Re-enable opponent's roll button
                updateScore() // Update scoreboard after rolling
                checkWinner() // Check if a player has won the game
                switchPlayer() // Switch to the next player after rolling
            }
        }
        countDownTimer.start()
    }

    private fun selectedDice(imageView: ImageView) {
        val diceNum = Random.nextInt(1, 7) // Generates random number between 1 and 6
        currentDiceNum = diceNum // Update current dice number
        val dice = dices[diceNum]
        imageView.setImageResource(dice)
    }

    private fun updateScore() {
        // Update the score of the current player based on the current dice number
        if (currentPlayer == 1) {
            player1Score += currentDiceNum
            binding.tvPlayer1Score.text = "$player1Name Total Score: $player1Score"
        } else {
            player2Score += currentDiceNum
            binding.tvPlayer2Score.text = "$player2Name Total Score: $player2Score"
        }
    }

    private fun checkWinner() {
        // Check if a player has reached the win score
        if (player1Score >= WIN_SCORE) {
            showWinnerMessage("$player1Name")
        } else if (player2Score >= WIN_SCORE) {
            showWinnerMessage("$player2Name")
        }
    }

    private fun showWinnerMessage(winner: String) {
        // Display a message indicating the winner of the game
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("$winner wins! Do you want to reset the game?")
        alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
            resetGame()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun resetGame() {
        // Reset all game variables and UI elements
        player1Score = 0
        player2Score = 0
        currentPlayer = 1
        isPlayer1Rolling = false
        isPlayer2Rolling = false
        binding.tvPlayer1Score.text = "$player1Name Total Score: 0"
        binding.tvPlayer2Score.text = "$player2Name Total Score: 0"
        // Add any additional reset logic here
    }

    private fun switchPlayer() {
        // Switch to the next player
        currentPlayer = if (currentPlayer == 1) 2 else 1
        isPlayer1Rolling = false
        isPlayer2Rolling = false
    }
}
