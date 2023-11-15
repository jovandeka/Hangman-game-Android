package com.example.vesanje;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String secretWord;
    private StringBuilder displayWord;
    private int remainingGuesses = 6;

    private TextView wordTextView;
    private TextView EndTextView;
    private TextView EndWordTextView;
    private TextView guessesTextView;
    private GridLayout keyboardGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordTextView = findViewById(R.id.wordTextView);
        EndTextView = findViewById(R.id.EndTextView);
        EndWordTextView = findViewById(R.id.EndWordTextView);
        guessesTextView = findViewById(R.id.guessesTextView);
        keyboardGrid = findViewById(R.id.keyboardGrid);

        String[] hangmanWords = getResources().getStringArray(R.array.hangman_words);
        secretWord = getRandomWord(hangmanWords);

        displayWord = new StringBuilder("_".repeat(secretWord.length()));

        updateWordDisplay();
        displayGuesses();

        setupKeyboard();
    }

    private String getRandomWord(String[] words) {
        Random random = new Random();
        int index = random.nextInt(words.length);
        return words[index];
    }

    private void setupKeyboard() {
        for (int i = 0; i < keyboardGrid.getChildCount(); i++) {
            View child = keyboardGrid.getChildAt(i);

            if (child instanceof TextView) {
                final TextView textView = (TextView) child;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleGuess(textView.getText().toString());
                        textView.setEnabled(false);
                    }
                });
            }
        }
    }

    private void handleGuess(String guess) {
        boolean letterGuessed = false;
        guess = guess.toUpperCase();

        for (int i = 0; i < secretWord.length(); i++) {
            if (Character.toUpperCase(secretWord.charAt(i)) == guess.charAt(0)) {
                displayWord.setCharAt(i, secretWord.charAt(i));
                letterGuessed = true;
            }
        }

        if (!letterGuessed) {
            remainingGuesses--;
        }

        displayGuesses();
        updateWordDisplay();
        checkGameStatus();
    }

    private void updateWordDisplay() {
        wordTextView.setText(displayWord.toString());
    }

    private void displayGuesses() {
        guessesTextView.setText("Guesses left: " + remainingGuesses);
    }

    private void checkGameStatus() {
        if (displayWord.toString().equals(secretWord)) {
            // Player wins
            EndTextView.setText("Congratulations!");
            EndWordTextView.setText("You guessed the word: " + secretWord);
            disableKeyboard();
            showTryAgainButton();
        } else if (remainingGuesses == 0) {
            // Player loses
            EndTextView.setText("Sorry, you ran out of guesses.");
            EndWordTextView.setText("The word was: " + secretWord);
            disableKeyboard();
            showTryAgainButton();
        }
    }

    private void showTryAgainButton() {
        Button tryAgainButton = findViewById(R.id.playAgainButton);
        tryAgainButton.setVisibility(View.VISIBLE);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset the game when "Try Again" is clicked
                resetGame();
            }
        });
    }

    private void resetGame() {
        String[] hangmanWords = getResources().getStringArray(R.array.hangman_words);
        secretWord = getRandomWord(hangmanWords);

        displayWord = new StringBuilder("_".repeat(secretWord.length()));
        remainingGuesses = 6;
        
        EndTextView.setText("");
        EndWordTextView.setText("");

        updateWordDisplay();
        displayGuesses();

        Button tryAgainButton = findViewById(R.id.playAgainButton);
        tryAgainButton.setVisibility(View.GONE);

        enableKeyboard();
    }

    private void enableKeyboard() {
        for (int i = 0; i < keyboardGrid.getChildCount(); i++) {
            View child = keyboardGrid.getChildAt(i);
            if (child instanceof TextView) {
                child.setEnabled(true);
            }
        }
    }

    private void disableKeyboard() {
        for (int i = 0; i < keyboardGrid.getChildCount(); i++) {
            View child = keyboardGrid.getChildAt(i);
            if (child instanceof TextView) {
                child.setEnabled(false);
            }
        }
    }
}