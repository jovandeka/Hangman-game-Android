package com.example.vesanje;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String[] wordArray;
    private String secretWord;
    private StringBuilder displayWord;
    private int remainingGuesses = 6;
    private List<TextView> letterTextViews;

    private TextView EndTextView;
    private TextView EndWordTextView;
    private TextView guessesTextView;
    private TextView categoryTextView;
    private GridLayout keyboardGrid;
    private ImageView backImageView;
    private LinearLayout lettersLinearLayout;

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
        lettersLinearLayout = findViewById(R.id.lettersLinearLayout);

        letterTextViews = new ArrayList<>();

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

        displayWord = new StringBuilder();
        for(int i = 0; i<secretWord.length();i++){
            if(secretWord.charAt(i)!=' '){
                displayWord.append(secretWord.charAt(i));
            }else{
                displayWord.append(' ');
            }
        }

        for (int i = 0; i < displayWord.length(); i++) {
            char letter = displayWord.charAt(i);
            TextView letterTextView = createLetterTextView(letter);
            letterTextViews.add(letterTextView);
        }
    }

    private void loadWordList(String category) {
        int wordListResourceId = getResources().getIdentifier(category, "array", getPackageName());
        wordArray = getResources().getStringArray(wordListResourceId);

        int randomIndex = (int) (Math.random() * wordArray.length);
        String input = wordArray[randomIndex];
        loadWord(input);
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
                letterGuessed = true;
            }
        }

        if (!letterGuessed) {
            remainingGuesses--;
        }else{
            updateLetterBackgrounds(guess);
        }

        displayHangman(remainingGuesses);
        checkGameStatus();
    }

    private void updateLetterBackgrounds(String ch) {
        for (int i = 0; i < letterTextViews.size(); i++) {
            TextView letterTextView = letterTextViews.get(i);
            String letter = letterTextView.getText().toString().toUpperCase();
            if (!letter.equals(" ") && Objects.equals(ch, letter)) {
                letterTextView.setBackgroundResource(R.color.pale_black);
            }
        }
    }

    private void updateWordDisplay() {
        lettersLinearLayout.removeAllViews();

        for (TextView letterTextView : letterTextViews) {
            lettersLinearLayout.addView(letterTextView);
        }
    }

    private TextView createLetterTextView(char letter) {
        TextView letterTextView = new TextView(this);
        letterTextView.setText(String.valueOf(letter));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                60,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams layoutParamsSpace = new LinearLayout.LayoutParams(
                30,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 3, 0);

        if (letter != ' ') {
            letterTextView.setLayoutParams(layoutParams);
            letterTextView.setBackgroundColor(getResources().getColor(R.color.orange));
        } else {
            letterTextView.setLayoutParams(layoutParamsSpace);
            letterTextView.setBackgroundColor(getResources().getColor(R.color.pale_black));
        }
        letterTextView.setTextColor(getResources().getColor(R.color.orange));
        letterTextView.setTextSize(25);
        letterTextView.setTypeface(null, Typeface.BOLD);
        letterTextView.setGravity(Gravity.CENTER);
        return letterTextView;
    }

    private void checkGameStatus() {
        boolean allLettersGuessed = true;

        for (int i = 0; i < letterTextViews.size(); i++) {
            TextView letterTextView = letterTextViews.get(i);
            ColorDrawable drawable = (ColorDrawable) letterTextView.getBackground();
            int backgroundColor = drawable.getColor();

            if (backgroundColor == ContextCompat.getColor(this, R.color.orange)) {
                allLettersGuessed = false;
                break;
            }
        }

        if (allLettersGuessed) {
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
        letterTextViews.clear();

        if (wordArray != null && wordArray.length > 0) {
            int randomIndex = (int) (Math.random() * wordArray.length);
            String input = wordArray[randomIndex];
            loadWord(input);
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
        letterTextViews.clear();

        WordRandomizer.randomizeCategoryAndWord(MainActivity.this);

        categoryTextView.setText("("+WordRandomizer.randomCategory+")");
        loadWord(WordRandomizer.randomWord);

        EndTextView.setText("");
        EndWordTextView.setText("");

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