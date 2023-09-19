package com.jaresi.wordle

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var guessCount = 0
    var wordToGuess = FourLetterWordList.getRandomFourLetterWord()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var guessField = findViewById<EditText>(R.id.guessField)
        var submitButton = findViewById<Button>(R.id.submitButton)
        val restartButton = findViewById<Button>(R.id.restartButton)
        val correctWord = findViewById<TextView>(R.id.correctWord)
        correctWord.text = wordToGuess
        Log.i("TAG", wordToGuess)

        submitButton.setOnClickListener {
            val guessedWord: TextView
            val checkField: TextView
            val guessNum: LinearLayout
            val checkGroup: LinearLayout

            if (guessCount == 0){
                guessNum = findViewById<LinearLayout>(R.id.firstGuess)
                checkGroup = findViewById<LinearLayout>(R.id.firstCheck)
                guessedWord = findViewById<TextView>(R.id.guess1)
                checkField = findViewById<TextView>(R.id.check1)
           }
            else if (guessCount == 1) {
                guessNum = findViewById<LinearLayout>(R.id.secondGuess)
                checkGroup = findViewById<LinearLayout>(R.id.secondCheck)
                guessedWord = findViewById<TextView>(R.id.guess2)
                checkField = findViewById<TextView>(R.id.check2)
            }
            else {
                guessNum = findViewById<LinearLayout>(R.id.thirdGuess)
                checkGroup = findViewById<LinearLayout>(R.id.thirdCheck)
                guessedWord = findViewById<TextView>(R.id.guess3)
                checkField = findViewById<TextView>(R.id.check3)
                submitButton.isEnabled = false
                submitButton.isClickable = false
            }

            val gWord = guessField.text.toString().uppercase()
            guessedWord.text = gWord

            checkField.text = checkGuess(gWord)
            guessNum.visibility = View.VISIBLE
            checkGroup.visibility = View.VISIBLE
            guessedWord.visibility = View.VISIBLE
            checkField.visibility = View.VISIBLE
            guessField.text.clear()

            closeKeyboard()

            if (checkGuess(gWord) == "OOOO"){
                findViewById<pl.droidsonroids.gif.GifImageView>(R.id.celebrateGif).visibility = View.VISIBLE
                findViewById<TextView>(R.id.header).visibility = View.VISIBLE
                submitButton.visibility = View.GONE
                restartButton.visibility = View.VISIBLE
                //correctWord.visibility = View.GONE
                //findViewById<TextView>(R.id.correctWord).visibility = View.VISIBLE
            }
            else if (guessCount == 2){
                findViewById<pl.droidsonroids.gif.GifImageView>(R.id.sadGif).visibility = View.VISIBLE
                findViewById<TextView>(R.id.wrongHeader).visibility = View.VISIBLE
                submitButton.visibility = View.GONE
                restartButton.visibility = View.VISIBLE
                correctWord.visibility = View.VISIBLE
            }

            guessCount += 1
        }

        restartButton.setOnClickListener {
            wordToGuess = FourLetterWordList.getRandomFourLetterWord()
            correctWord.text = wordToGuess
            guessCount = 0
            findViewById<TextView>(R.id.guess1).text = ""
            findViewById<LinearLayout>(R.id.firstCheck).visibility = View.GONE
            findViewById<LinearLayout>(R.id.secondGuess).visibility = View.GONE
            findViewById<LinearLayout>(R.id.secondCheck).visibility = View.GONE
            findViewById<LinearLayout>(R.id.thirdGuess).visibility = View.GONE
            findViewById<LinearLayout>(R.id.thirdCheck).visibility = View.GONE
            findViewById<pl.droidsonroids.gif.GifImageView>(R.id.celebrateGif).visibility = View.GONE
            findViewById<pl.droidsonroids.gif.GifImageView>(R.id.sadGif).visibility = View.GONE
            findViewById<TextView>(R.id.header).visibility = View.GONE
            findViewById<LinearLayout>(R.id.guessGroup).visibility = View.VISIBLE
            findViewById<TextView>(R.id.wrongHeader).visibility = View.GONE
            restartButton.visibility = View.GONE
            submitButton.isEnabled = true
            submitButton.isClickable = true
            submitButton.visibility = View.VISIBLE
            Log.i("TAG", wordToGuess)
        }
    }

    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *
     * Returns a String of 'O', '+', and 'X', where:
     *   'O' represents the right letter in the right place
     *   '+' represents the right letter in the wrong place
     *   'X' represents a letter not in the target word
     */
    private fun checkGuess(guess: String) : String {
        var result = ""
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                result += "O"
            }
            else if (guess[i] in wordToGuess) {
                result += "+"
            }
            else {
                result += "X"
            }
        }
        return result
    }

    private fun closeKeyboard() {
        // this will give us the view
        // which is currently focus
        // in this layout
        val view = this.currentFocus

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            val manager = getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager
                .hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }
}