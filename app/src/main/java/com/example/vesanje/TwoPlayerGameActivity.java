package com.example.vesanje;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class TwoPlayerGameActivity extends AppCompatActivity {

    private EditText editTextWord, editTextCategory;
    private ImageView backImageView, randBtn, hangmanImageView, clearBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);

        editTextWord = findViewById(R.id.editTextWord);
        editTextCategory = findViewById(R.id.editTextCategory);
        backImageView = findViewById(R.id.backImageView);
        randBtn = findViewById(R.id.randBtn);
        clearBtn = findViewById(R.id.clearBtn);
        hangmanImageView = findViewById(R.id.hangmanImageView);

        randBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordRandomizer.randomizeCategoryAndWord(TwoPlayerGameActivity.this);

                editTextWord.setText(WordRandomizer.randomWord);
                editTextCategory.setText(WordRandomizer.randomCategory);

                randBtn.setEnabled(false);
                moveImage();
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextWord.setText("");
                editTextCategory.setText("");
            }
        });

        Button buttonStartGame = findViewById(R.id.buttonStartGame);
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextWord.getText().toString().trim().isEmpty()) {
                    editTextWord.setError("Please enter the word.");
                }else if(!isValidWord(editTextWord.getText().toString().trim())){
                    editTextWord.setError("Please don't use any special characters.");
                }else {
                    startGame();
                }
            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwoPlayerGameActivity.this, IntroActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void startGame() {
        String word = editTextWord.getText().toString().trim();
        String categoryPvp = editTextCategory.getText().toString().trim();

        if(categoryPvp.isEmpty()){
            categoryPvp = "guess";
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("word", word);
        intent.putExtra("categoryPvp", categoryPvp);
        startActivity(intent);
    }
    private static boolean isValidWord(String word) {

        return word.matches("^[a-zA-ZćčđžšĆČĐŽŠ ]+$");
    }

    private void moveImage() {
        float startY = hangmanImageView.getTranslationY();
        float endY = startY - 60;

        ObjectAnimator moveUpAnimator = ObjectAnimator.ofFloat(hangmanImageView, "translationY", startY, endY);
        moveUpAnimator.setDuration(50);

        ObjectAnimator moveDownAnimator = ObjectAnimator.ofFloat(hangmanImageView, "translationY", endY, startY);
        moveDownAnimator.setDuration(150);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playSequentially(moveUpAnimator, moveDownAnimator);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Animation start logic, if needed
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                randBtn.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Animation cancel logic, if needed
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Animation repeat logic, if needed
            }
        });

        animatorSet.start();
    }
}
