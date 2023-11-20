package com.example.vesanje;

import android.content.Context;
import android.content.res.Resources;

import java.util.Random;

public class WordRandomizer {

    public static String randomWord;
    public static String randomCategory;

    public static void randomizeCategoryAndWord(Context context) {
        ArrayInfo arrayInfo = getRandomArrayInfo(context);

        randomWord = getRandomWord(arrayInfo.array);
        randomCategory = arrayInfo.arrayName;
    }

    public static String getRandomWord() {
        return randomWord;
    }

    public static String getRandomCategory() {
        return randomCategory;
    }

    private static String getRandomWord(String[] array) {
        int randomIndex = (int) (Math.random() * array.length);
        return array[randomIndex];
    }

    private static ArrayInfo getRandomArrayInfo(Context context) {
        Resources resources = context.getResources();
        String[] fruitArray = resources.getStringArray(R.array.fruit);
        String[] animalArray = resources.getStringArray(R.array.animal);
        String[] colorArray = resources.getStringArray(R.array.color);
        String[] instrumentArray = resources.getStringArray(R.array.instrument);
        String[] sportArray = resources.getStringArray(R.array.sport);
        String[] footballArray = resources.getStringArray(R.array.club);
        String[] countryArray = resources.getStringArray(R.array.country);
        String[] foodArray = resources.getStringArray(R.array.food);
        String[] vegetableArray = resources.getStringArray(R.array.vegetable);
        String[] movieArray = resources.getStringArray(R.array.movie);
        String[] brandArray = resources.getStringArray(R.array.brand);
        String[] bandArray = resources.getStringArray(R.array.band);

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
