package com.example.vesanje;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String[] wordArray;
    private String secretWord;
    private StringBuilder displayWord;
    private int remainingGuesses = 6;

    private TextView wordTextView;
    private TextView EndTextView;
    private TextView EndWordTextView;
    private TextView guessesTextView;
    private TextView categoryTextView;
    private GridLayout keyboardGrid;
    private ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EndTextView = findViewById(R.id.EndTextView);
        EndWordTextView = findViewById(R.id.EndWordTextView);
        guessesTextView = findViewById(R.id.guessesTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        keyboardGrid = findViewById(R.id.keyboardGrid);
        backImageView = findViewById(R.id.backImageView);

        Intent intent = getIntent();
        if (intent.hasExtra("category")) {
            String category = intent.getStringExtra("category");
            categoryTextView.setText("(" + category + ")");
            loadWordList(category);
        }else if(intent.hasExtra("categoryPvp")) {
            String word = intent.getStringExtra("word");
            String categoryPvp = intent.getStringExtra("categoryPvp");
            String category = categoryPvp.replaceAll("\\s+", " ");
            categoryTextView.setText("(" + category + ")");
            loadWord(word);
        }else if(intent.hasExtra("categoryRand")) {
            String word = intent.getStringExtra("word");
            String categoryRand = intent.getStringExtra("categoryRand");
            String category = categoryRand.replaceAll("\\s+", " ");
            categoryTextView.setText("(" + category + ")");
            loadWord(word);
        }else {
            loadWordList("country");
        }

        updateWordDisplay();
        displayHangman(remainingGuesses);

        setupKeyboard();

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void loadWord(String word) {
        String input = word;
        secretWord = input.replaceAll("\\s+", " ");
        int len = word.length();
        displayWord = new StringBuilder();
        for(int i = 0; i<len;i++){
            if(word.charAt(i)!=' '){
                displayWord.append('_');
            }else if(word.charAt(i)==' ' && word.charAt(i-1)!=' '){
                displayWord.append(' ');
            }
        }

    }

    private void loadWordList(String category) {
        int wordListResourceId = getResources().getIdentifier(category, "array", getPackageName());
        wordArray = getResources().getStringArray(wordListResourceId);

        int randomIndex = (int) (Math.random() * wordArray.length);
        String input = wordArray[randomIndex];
        secretWord = input.replaceAll("\\s+", " ");

        displayWord = new StringBuilder();
        for(int i = 0; i<secretWord.length();i++){
            if(secretWord.charAt(i)!=' '){
                displayWord.append('_');
            }else if(secretWord.charAt(i)==' '){
                displayWord.append(' ');
            }
        }
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

        displayHangman(remainingGuesses);
        updateWordDisplay();
        checkGameStatus();
    }

    private void updateWordDisplay() {
        LinearLayout lettersLinearLayout = findViewById(R.id.lettersLinearLayout);
        lettersLinearLayout.removeAllViews(); // Clear existing letters

        for (int i = 0; i < displayWord.length(); i++) {
            char letter = displayWord.charAt(i);
            if (letter != ' ') {
                TextView letterTextView = createLetterTextView(letter);
                lettersLinearLayout.addView(letterTextView);
            } else {
                TextView spaceTextView = createLetterTextView(' ');
                lettersLinearLayout.addView(spaceTextView);
            }
        }
    }

    private TextView createLetterTextView(char letter) {
        TextView letterTextView = new TextView(this);
        letterTextView.setText(String.valueOf(letter));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                50,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 6, 0);

        letterTextView.setLayoutParams(layoutParams);
        letterTextView.setTextColor(getResources().getColor(R.color.white));
        letterTextView.setBackgroundColor(getResources().getColor(R.color.orange));
        letterTextView.setPadding(8, 8, 8, 8);
        letterTextView.setTextSize(20);
        letterTextView.setTypeface(null, Typeface.BOLD);
        letterTextView.setGravity(Gravity.CENTER);
        return letterTextView;
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
                Intent intent = getIntent();
                if(intent.hasExtra("categoryPvp")){
                    Intent intent2 = new Intent(MainActivity.this, TwoPlayerGameActivity.class);
                    startActivity(intent2);
                }else if(intent.hasExtra("categoryRand")){
                    resetGameRand();
                }else{
                    resetGame();
                }

            }
        });
    }

    private void resetGame() {
        remainingGuesses = 6;

        if (wordArray != null && wordArray.length > 0) {
            int randomIndex = (int) (Math.random() * wordArray.length);
            String input = wordArray[randomIndex];
            secretWord = input.replaceAll("\\s+", " ");

            displayWord = new StringBuilder();
            for(int i = 0; i<secretWord.length();i++){
                if(secretWord.charAt(i)!=' '){
                    displayWord.append('_');
                }else if(secretWord.charAt(i)==' '){
                    displayWord.append(' ');
                }
            }
        } else {
            secretWord = "DEFAULT";
            displayWord = new StringBuilder("_".repeat(secretWord.length()));
        }

        EndTextView.setText("");
        EndWordTextView.setText("");

        updateWordDisplay();
        displayHangman(remainingGuesses);
        enableKeyboard();

        Button tryAgainButton = findViewById(R.id.playAgainButton);
        tryAgainButton.setVisibility(View.GONE);
    }

    private void resetGameRand() {
        remainingGuesses = 6;

        WordRandomizer.randomizeCategoryAndWord(MainActivity.this);

        secretWord = WordRandomizer.randomWord;

        displayWord = new StringBuilder();
        for(int i = 0; i<secretWord.length();i++){
            if(secretWord.charAt(i)!=' '){
                displayWord.append('_');
            }else if(secretWord.charAt(i)==' '){
                displayWord.append(' ');
            }
        }

        EndTextView.setText("");
        EndWordTextView.setText("");

        categoryTextView.setText(WordRandomizer.randomCategory);
        updateWordDisplay();
        displayHangman(remainingGuesses);
        enableKeyboard();

        Button tryAgainButton = findViewById(R.id.playAgainButton);
        tryAgainButton.setVisibility(View.GONE);
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
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(MainActivity.this, IntroActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void displayHangman(int incorrectGuesses) {
        StringBuilder hangmanBuilder = new StringBuilder();

        switch (incorrectGuesses) {
            case 6:
                hangmanBuilder.append(" +------+         \n");
                hangmanBuilder.append(" ||           |   \n");
                hangmanBuilder.append(" ||               \n");
                hangmanBuilder.append(" ||               \n");
                hangmanBuilder.append(" ||               \n");
                hangmanBuilder.append(" ||_____________  \n");
                break;
            case 5:
                hangmanBuilder.append(" +------+         \n");
                hangmanBuilder.append(" ||           |   \n");
                hangmanBuilder.append(" ||          O    \n");
                hangmanBuilder.append(" ||               \n");
                hangmanBuilder.append(" ||               \n");
                hangmanBuilder.append(" ||_____________  \n");
                break;
            case 4:
                hangmanBuilder.append(" +------+         \n");
                hangmanBuilder.append(" ||           |   \n");
                hangmanBuilder.append(" ||          O    \n");
                hangmanBuilder.append(" ||           |   \n");
                hangmanBuilder.append(" ||               \n");
                hangmanBuilder.append(" ||_____________  \n");
                break;
            case 3:
                hangmanBuilder.append(" +------+         \n");
                hangmanBuilder.append(" ||           |   \n");
                hangmanBuilder.append(" ||          O    \n");
                hangmanBuilder.append(" ||         /|    \n");
                hangmanBuilder.append(" ||               \n");
                hangmanBuilder.append(" ||_____________  \n");
                break;
            case 2:
                hangmanBuilder.append(" +------+         \n");
                hangmanBuilder.append(" ||           |   \n");
                hangmanBuilder.append(" ||          O    \n");
                hangmanBuilder.append(" ||         /|\\  \n");
                hangmanBuilder.append(" ||               \n");
                hangmanBuilder.append(" ||_____________  \n");
                break;
            case 1:
                hangmanBuilder.append(" +------+         \n");
                hangmanBuilder.append(" ||           |   \n");
                hangmanBuilder.append(" ||          O    \n");
                hangmanBuilder.append(" ||         /|\\  \n");
                hangmanBuilder.append(" ||         /     \n");
                hangmanBuilder.append(" ||_____________  \n");
                break;
            case 0:
                hangmanBuilder.append(" +------+         \n");
                hangmanBuilder.append(" ||           |   \n");
                hangmanBuilder.append(" ||          O    \n");
                hangmanBuilder.append(" ||         /|\\  \n");
                hangmanBuilder.append(" ||         / \\  \n");
                hangmanBuilder.append(" ||_____________  \n");
                break;
        }

        guessesTextView.setText(hangmanBuilder.toString());
    }
}