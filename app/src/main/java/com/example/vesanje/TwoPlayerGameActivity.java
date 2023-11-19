package com.example.vesanje;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class TwoPlayerGameActivity extends AppCompatActivity {

    private EditText editTextWord, editTextCategory;
    private ImageView backImageView, randBtn;
    private String[] currentArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);

        editTextWord = findViewById(R.id.editTextWord);
        editTextCategory = findViewById(R.id.editTextCategory);
        backImageView = findViewById(R.id.backImageView);
        randBtn = findViewById(R.id.randBtn);

        randBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomizeAndSetWord();
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

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("word", word);
        intent.putExtra("categoryPvp", categoryPvp);
        startActivity(intent);
    }
    private static boolean isValidWord(String word) {

        return word.matches("^[a-zA-ZćčđžšĆČĐŽŠ ]+$");
    }
    private void randomizeAndSetWord() {
        ArrayInfo arrayInfo = getRandomArrayInfo();

        String randomWord = getRandomWord(arrayInfo.array);

        editTextWord.setText(randomWord);
        editTextCategory.setText(arrayInfo.arrayName);
    }

    private String getRandomWord(String[] array) {
        int randomIndex = (int) (Math.random() * array.length);

        return array[randomIndex];
    }

    private ArrayInfo getRandomArrayInfo() {
        String[] fruitArray = getResources().getStringArray(R.array.fruit);
        String[] animalArray = getResources().getStringArray(R.array.animal);
        String[] colorArray = getResources().getStringArray(R.array.color);
        String[] instrumentArray = getResources().getStringArray(R.array.instrument);
        String[] sportArray = getResources().getStringArray(R.array.sport);
        String[] footballArray = getResources().getStringArray(R.array.club);
        String[] countryArray = getResources().getStringArray(R.array.country);
        String[] foodArray = getResources().getStringArray(R.array.food);
        String[] vegetableArray = getResources().getStringArray(R.array.vegetable);
        String[] movieArray = getResources().getStringArray(R.array.movie);
        String[] brandArray = getResources().getStringArray(R.array.brand);
        String[] bandArray = getResources().getStringArray(R.array.band);

        Random random = new Random();
        int randomIndex = random.nextInt(12);
        switch (randomIndex) {
            case 0:
                return new ArrayInfo(fruitArray, "fruit");
            case 1:
                return new ArrayInfo(animalArray, "animal");
            case 2:
                return new ArrayInfo(colorArray, "color");
            case 3:
                return new ArrayInfo(instrumentArray, "instrument");
            case 4:
                return new ArrayInfo(sportArray, "sport");
            case 5:
                return new ArrayInfo(footballArray, "club");
            case 6:
                return new ArrayInfo(countryArray, "country");
            case 7:
                return new ArrayInfo(foodArray, "food");
            case 8:
                return new ArrayInfo(vegetableArray, "vegetable");
            case 9:
                return new ArrayInfo(movieArray, "movie");
            case 10:
                return new ArrayInfo(brandArray, "brand");
            case 11:
                return new ArrayInfo(bandArray, "band");
            default:
                return new ArrayInfo(countryArray, "country");
        }
    }

    private static class ArrayInfo {
        String[] array;
        String arrayName;

        ArrayInfo(String[] array, String arrayName) {
            this.array = array;
            this.arrayName = arrayName;
        }
    }
}
