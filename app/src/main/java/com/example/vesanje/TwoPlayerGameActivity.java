package com.example.vesanje;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TwoPlayerGameActivity extends AppCompatActivity {

    private EditText editTextWord, editTextCategory;
    private ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);

        editTextWord = findViewById(R.id.editTextWord);
        editTextCategory = findViewById(R.id.editTextCategory);
        backImageView = findViewById(R.id.backImageView);

        Button buttonStartGame = findViewById(R.id.buttonStartGame);
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextWord.getText().toString().trim().isEmpty()) {
                    startGame();
                } else {
                    editTextWord.setError("Please enter the word.");
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

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("word", word);
        intent.putExtra("categoryPvp", categoryPvp);
        startActivity(intent);
    }
}
