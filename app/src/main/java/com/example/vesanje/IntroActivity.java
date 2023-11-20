package com.example.vesanje;

import android.content.Intent;
import android.content.res.Resources;
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

        TextView countFruitTV = findViewById(R.id.countFruitTV);
        int fruitCount = countCategory("fruit");
        countFruitTV.setText(""+fruitCount);

        TextView countAnimalTV = findViewById(R.id.countAnimalTV);
        int animalCount = countCategory("animal");
        countAnimalTV.setText(""+animalCount);

        TextView countColorTV = findViewById(R.id.countColorTV);
        int colorCount = countCategory("color");
        countColorTV.setText(""+colorCount);

        TextView countInstrumentTV = findViewById(R.id.countInstrumentTV);
        int instrumentCount = countCategory("instrument");
        countInstrumentTV.setText(""+instrumentCount);

        TextView countSportTV = findViewById(R.id.countSportTV);
        int sportCount = countCategory("sport");
        countSportTV.setText(""+sportCount);

        TextView countClubTV = findViewById(R.id.countClubTV);
        int clubCount = countCategory("club");
        countClubTV.setText(""+clubCount);

        TextView countCountryTV = findViewById(R.id.countCountryTV);
        int countryCount = countCategory("country");
        countCountryTV.setText(""+countryCount);

        TextView countFoodTV = findViewById(R.id.countFoodTV);
        int foodCount = countCategory("food");
        countFoodTV.setText(""+foodCount);

        TextView countVegetableTV = findViewById(R.id.countVegetableTV);
        int vegetableCount = countCategory("vegetable");
        countVegetableTV.setText(""+vegetableCount);

        TextView countMovieTV = findViewById(R.id.countMovieTV);
        int movieCount = countCategory("movie");
        countMovieTV.setText(""+movieCount);

        TextView countBrandTV = findViewById(R.id.countBrandTV);
        int brandCount = countCategory("brand");
        countBrandTV.setText(""+brandCount);

        TextView countBandTV = findViewById(R.id.countBandTV);
        int bandCount = countCategory("band");
        countBandTV.setText(""+bandCount);

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

    private int countCategory(String categoryName){
        Resources res = getResources();
        int arrayId = res.getIdentifier(categoryName, "array", getPackageName());

        if (arrayId != 0) {
            int arrayLength = res.getStringArray(arrayId).length;
            return arrayLength;
        } else {
            return 0;
        }
    }

}
