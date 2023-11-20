package com.example.vesanje;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.Random;

public class IntroActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ImageView pvpButton = findViewById(R.id.pvp_button);
        ImageView randomButton = findViewById(R.id.random_button);
        Button guessFruitButton = findViewById(R.id.guessFruitButton);
        Button guessAnimalButton = findViewById(R.id.guessAnimalButton);
        Button guessColorsButton = findViewById(R.id.guessColorsButton);
        Button guessInstrumentButton = findViewById(R.id.guessInstrumentButton);
        Button guessSportButton = findViewById(R.id.guessSportButton);
        Button guessClubButton = findViewById(R.id.guessClubButton);
        Button guessCountryButton = findViewById(R.id.guessCountryButton);
        Button guessFoodButton = findViewById(R.id.guessFoodButton);
        Button guessVegetableButton = findViewById(R.id.guessVegetableButton);
        Button guessMovieButton = findViewById(R.id.guessMovieButton);
        Button guessBrandButton = findViewById(R.id.guessBrandButton);
        Button guessBandButton = findViewById(R.id.guessBandButton);

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
        guessInstrumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("instrument");
            }
        });

        guessSportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("sport");
            }
        });

        guessClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("club");
            }
        });
        guessCountryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("country");
            }
        });

        guessFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("food");
            }
        });

        guessVegetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("vegetable");
            }
        });
        guessMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("movie");
            }
        });

        guessBrandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("brand");
            }
        });

        guessBandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("band");
            }
        });

        pvpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, TwoPlayerGameActivity.class);
                startActivity(intent);
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordRandomizer.randomizeCategoryAndWord(IntroActivity.this);

                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                intent.putExtra("word", WordRandomizer.randomWord);
                intent.putExtra("categoryRand", WordRandomizer.randomCategory);
                startActivity(intent);
            }
        });
    }

    private void startGame(String category) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000); // Change the delay as needed
    }

}
