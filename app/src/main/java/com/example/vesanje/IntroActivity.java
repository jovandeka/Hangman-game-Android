package com.example.vesanje;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button guessFruitButton = findViewById(R.id.guessFruitButton);
        Button guessAnimalButton = findViewById(R.id.guessAnimalButton);
        Button guessColorsButton = findViewById(R.id.guessColorsButton);
        Button guessRandomButton = findViewById(R.id.guessRandomButton);

        guessFruitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("fruit");
            }
        });

        guessAnimalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("animal");
            }
        });

        guessColorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("color");
            }
        });

        guessRandomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("random");
            }
        });
    }

    private void startGame(String category) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
