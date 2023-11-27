package com.example.vesanje;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private String[] wordArray;
    private String secretWord;
    private StringBuilder displayWord;
    private StringBuilder guessedWord;
    private int remainingGuesses = 6;
    private List<TextView> letterTextViews;
    private AnimatorSet animatorSetWin;
    private int consecutiveWinsCategory = 0;
    private int consecutiveWinsRandom = 0;
    private String categoryNow;

    private Button tryAgainButton;
    private TextView EndTextView;
    private TextView EndWordTextView;
    private TextView categoryTextView;
    private TextView recordTextView;
    private TextView winsTextView;
    private TextView modeTextView;
    private ImageView categoryImageView;
    private GridLayout keyboardGrid;
    private ImageView backImageView;
    private LinearLayout lettersLinearLayout;
    private LinearLayout consecutiveWinsLinearLayout;
    private ImageView drawHangman;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawHangman = findViewById(R.id.drawHangmanImageView);
        tryAgainButton = findViewById(R.id.playAgainButton);
        EndTextView = findViewById(R.id.EndTextView);
        EndWordTextView = findViewById(R.id.EndWordTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        recordTextView = findViewById(R.id.recordTextView);
        winsTextView = findViewById(R.id.winsTextView);
        modeTextView = findViewById(R.id.modeTextView);
        categoryImageView = findViewById(R.id.categoryImageView);
        keyboardGrid = findViewById(R.id.keyboardGrid);
        backImageView = findViewById(R.id.backImageView);
        lettersLinearLayout = findViewById(R.id.lettersLinearLayout);
        consecutiveWinsLinearLayout = findViewById(R.id.consecutiveWinsLinearLayout);

        letterTextViews = new ArrayList<>();

        Intent intent = getIntent();
        if (intent.hasExtra("category")) {
            modeTextView.setVisibility(View.GONE);
            String category = intent.getStringExtra("category");
            categoryNow = category;
            categoryTextView.setText(category);
            categoryImageView.setImageResource(getResources().getIdentifier(category, "drawable", getPackageName()));
            displayWinsCategory(category);
            loadWordList(category);
        }else if(intent.hasExtra("categoryPvp")) {
            consecutiveWinsLinearLayout.setVisibility(View.GONE);
            modeTextView.setText("2 PLAYER MODE");
            String word = intent.getStringExtra("word");
            String categoryPvp = intent.getStringExtra("categoryPvp");
            String category = categoryPvp.replaceAll("\\s+", " ");
            categoryTextView.setText(category);
            int catImg = getResources().getIdentifier(category, "drawable", getPackageName());
            if(catImg != 0){
                categoryImageView.setImageResource(catImg);
            }
            loadWord(word);
        }else if(intent.hasExtra("categoryRand")) {
            modeTextView.setText("RANDOM MODE");
            String word = intent.getStringExtra("word");
            String categoryRand = intent.getStringExtra("categoryRand");
            String category = categoryRand.replaceAll("\\s+", " ");
            categoryTextView.setText(category);
            categoryImageView.setImageResource(getResources().getIdentifier(category, "drawable", getPackageName()));
            displayWinsRandom();
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
        guessedWord = new StringBuilder();
        for(int i = 0; i<secretWord.length();i++){
            if(secretWord.charAt(i)!=' '){
                displayWord.append(secretWord.charAt(i));
                guessedWord.append('_');
            }else{
                displayWord.append(' ');
                guessedWord.append(' ');
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
                guessedWord.setCharAt(i, secretWord.charAt(i));
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

    private void updateWrongBackgrounds() {
        for (int i = 0; i < letterTextViews.size(); i++) {
            TextView letterTextView = letterTextViews.get(i);
            ColorDrawable cd = (ColorDrawable) letterTextView.getBackground();
            int colorCode = cd.getColor();
            if (colorCode == getResources().getColor(R.color.orange)) {

                int targetColor = ContextCompat.getColor(this, R.color.wrong_red);

                ValueAnimator colorAnimator = ValueAnimator.ofArgb(colorCode, targetColor);
                colorAnimator.setDuration(800);

                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        int animatedColor = (int) animator.getAnimatedValue();
                        letterTextView.setBackgroundColor(animatedColor);
                    }
                });

                colorAnimator.start();
            }
        }
    }

    private void updateLetterBackgrounds(String ch) {
        for (int i = 0; i < letterTextViews.size(); i++) {
            TextView letterTextView = letterTextViews.get(i);
            String letter = letterTextView.getText().toString().toUpperCase();
            if (!letter.equals(" ") && Objects.equals(ch, letter)) {
                int currentColor = ContextCompat.getColor(this, R.color.orange);
                int targetColor = ContextCompat.getColor(this, R.color.pale_black);

                ValueAnimator colorAnimator = ValueAnimator.ofArgb(currentColor, targetColor);
                colorAnimator.setDuration(400);

                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        int animatedColor = (int) animator.getAnimatedValue();
                        letterTextView.setBackgroundColor(animatedColor);
                    }
                });

                colorAnimator.start();
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
                65,
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
        letterTextView.setTextSize(28);
        Typeface font = ResourcesCompat.getFont(this, R.font.play_bold);
        letterTextView.setTypeface(font);
        letterTextView.setGravity(Gravity.CENTER);
        return letterTextView;
    }

    private void displayWinsRandom(){
        int record = ConsecutiveWinsManager.getConsecutiveWinsRandom(this);
        if(record>=consecutiveWinsRandom){
            recordTextView.setText("Record: "+record);
            winsTextView.setText("Now: "+consecutiveWinsRandom);
        }else{
            ConsecutiveWinsManager.setConsecutiveWinsRandom(this, consecutiveWinsRandom);
            recordTextView.setText("Record: "+consecutiveWinsRandom);
            winsTextView.setText("Now: "+consecutiveWinsRandom);
            animateTextColor(recordTextView, getResources().getColor(R.color.green), 3000);
        }
    }

    private void displayWinsCategory(String category){
        int record = ConsecutiveWinsManager.getConsecutiveWins(this,category);
        if(record>=consecutiveWinsCategory){
            recordTextView.setText("Record: "+record);
            winsTextView.setText("Now: "+consecutiveWinsCategory);
        }else{
            ConsecutiveWinsManager.setConsecutiveWins(this, category, consecutiveWinsCategory);
            recordTextView.setText("Record: "+consecutiveWinsCategory);
            winsTextView.setText("Now: "+consecutiveWinsCategory);
            animateTextColor(recordTextView, getResources().getColor(R.color.green), 3000);
        }
    }
    public static void animateTextColor(TextView textView, int targetColor, int duration) {
        int startColor = textView.getCurrentTextColor();

        ObjectAnimator colorAnimator = ObjectAnimator.ofObject(
                textView,
                "textColor",
                new ArgbEvaluator(),
                targetColor,
                startColor
        );
        colorAnimator.setDuration(duration);

        colorAnimator.start();
    }


    private void checkGameStatus() {
        Intent intent = getIntent();
        if (guessedWord.toString().equals(secretWord)) {

            if(intent.hasExtra("categoryRand")) {
                consecutiveWinsRandom++;
                displayWinsRandom();
                animateTextColor(winsTextView, getResources().getColor(R.color.green), 3000);

            }else if(intent.hasExtra("category")){
                consecutiveWinsCategory++;
                displayWinsCategory(categoryNow);
                animateTextColor(winsTextView, getResources().getColor(R.color.green), 3000);
            }

            EndTextView.setAlpha(0f);
            tryAgainButton.setAlpha(0f);
            showTryAgainButton();
            tryAgainButton.setBackgroundResource(R.drawable.play_again_green);

            EndTextView.setText("Congratulations!");

            EndWinLoopingAnimation();

            disableKeyboard();
            animateAppear(tryAgainButton);
            animateAppear(EndTextView);

            animateLayoutParamsChange();

        } else if (remainingGuesses == 0) {

            if(intent.hasExtra("categoryRand")) {
                if(consecutiveWinsRandom!=0) {
                    animateTextColor(winsTextView, getResources().getColor(R.color.wrong_red), 3000);
                }
                consecutiveWinsRandom = 0;
                displayWinsRandom();
            }else if(intent.hasExtra("category")){
                if(consecutiveWinsCategory!=0) {
                    animateTextColor(winsTextView, getResources().getColor(R.color.wrong_red), 3000);
                }
                consecutiveWinsCategory = 0;
                displayWinsCategory(categoryNow);
            }
            EndTextView.setAlpha(0f);
            EndWordTextView.setAlpha(0f);
            tryAgainButton.setAlpha(0f);
            showTryAgainButton();
            tryAgainButton.setBackgroundResource(R.drawable.play_again_red);
            updateWrongBackgrounds();

            EndTextView.setText("You ran out of guesses!");
            EndWordTextView.setText("Answer: " + secretWord);

            endLostLoopingAnimation();

            animateAppear(tryAgainButton);
            animateAppear(EndTextView);
            animateAppear(EndWordTextView);
            disableKeyboard();
        }
    }

    private void animateLayoutParamsChange() {
        for (final TextView letterTextView : letterTextViews) {
            final LinearLayout.LayoutParams startParams = (LinearLayout.LayoutParams) letterTextView.getLayoutParams();
            final LinearLayout.LayoutParams endParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            final ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = animation.getAnimatedFraction();

                    int width = (int) (startParams.width + fraction * (endParams.width - startParams.width));
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                    params.setMargins(startParams.leftMargin, 0, startParams.rightMargin, 0);

                    letterTextView.setLayoutParams(params);
                }
            });

            animator.setDuration(300);
            animator.start();
        }
    }

    private void showTryAgainButton() {
        tryAgainButton.setVisibility(View.VISIBLE);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if(intent.hasExtra("categoryPvp")){
                    Intent intent2 = new Intent(MainActivity.this, TwoPlayerGameActivity.class);
                    startActivity(intent2);
                }else if(intent.hasExtra("categoryRand")){
                    try{
                        animatorSetWin.cancel();
                        EndTextView.setTranslationY(0f);
                        EndTextView.setRotation(0f);
                    }catch (Exception e){e.printStackTrace();}

                    resetGameRand();
                }else{
                    try{
                        animatorSetWin.cancel();
                        EndTextView.setTranslationY(0f);
                        EndTextView.setRotation(0f);
                    }catch (Exception e){e.printStackTrace();}

                    resetGame();
                }

            }
        });
    }

    private void animateAppear(View view) {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(800);

        fadeIn.start();
    }

    private void EndWinLoopingAnimation() {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(EndTextView, "translationY", 0f, -40f, 0f);
        translationY.setRepeatMode(ObjectAnimator.REVERSE);
        translationY.setRepeatCount(ObjectAnimator.INFINITE);

        ObjectAnimator rotation = ObjectAnimator.ofFloat(EndTextView, "rotation", 0f, 3f, 0f, -3f, 0f);
        rotation.setRepeatMode(ObjectAnimator.REVERSE);
        rotation.setRepeatCount(ObjectAnimator.INFINITE);

        int startColor = getResources().getColor(R.color.pale_yellow);
        int endColor = getResources().getColor(R.color.green);

        ValueAnimator colorAnimator = ObjectAnimator.ofObject(
                EndTextView,
                "textColor",
                new ArgbEvaluator(),
                startColor,
                endColor
        );

        colorAnimator.setDuration(750);

        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);

        animatorSetWin = new AnimatorSet();
        animatorSetWin.playTogether(translationY, rotation, colorAnimator);

        animatorSetWin.setDuration(1000);

        animatorSetWin.start();
    }

    private void endLostLoopingAnimation() {
        int startColor = getResources().getColor(R.color.pale_yellow);
        int endColor = getResources().getColor(R.color.wrong_red);

        ValueAnimator colorAnimatorLost = ObjectAnimator.ofObject(
                EndTextView,
                "textColor",
                new ArgbEvaluator(),
                startColor,
                endColor
        );

        ValueAnimator colorAnimatorWordLost = ObjectAnimator.ofObject(
                EndWordTextView,
                "textColor",
                new ArgbEvaluator(),
                startColor,
                endColor
        );

        colorAnimatorLost.setDuration(700);
        colorAnimatorWordLost.setDuration(700);

        colorAnimatorLost.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimatorLost.setRepeatCount(ValueAnimator.INFINITE);

        colorAnimatorWordLost.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimatorWordLost.setRepeatCount(ValueAnimator.INFINITE);

        colorAnimatorLost.start();
        colorAnimatorWordLost.start();
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

        String category = WordRandomizer.randomCategory;
        categoryTextView.setText(category);
        categoryImageView.setImageResource(getResources().getIdentifier(category, "drawable", getPackageName()));
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

        switch (incorrectGuesses) {
            case 6:
                drawHangman.setImageResource(R.drawable.stick_hangman_6_construction);
                break;
            case 5:
                drawHangman.setImageResource(R.drawable.stick_hangman_5);
                break;
            case 4:
                drawHangman.setImageResource(R.drawable.stick_hangman_4);
                break;
            case 3:
                drawHangman.setImageResource(R.drawable.stick_hangman_3);
                break;
            case 2:
                drawHangman.setImageResource(R.drawable.stick_hangman_2);
                break;
            case 1:
                drawHangman.setImageResource(R.drawable.stick_hangman_1);
                break;
            case 0:
                drawHangman.setImageResource(R.drawable.stick_hangman_0);
                break;
        }
    }

}