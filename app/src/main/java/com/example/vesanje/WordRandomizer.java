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
        String[] categoriesArray = resources.getStringArray(R.array.categories);

        Random random = new Random();
        int randomIndex = random.nextInt(categoriesArray.length);
        String randomCategory = categoriesArray[randomIndex];
        int arrayResourceId = context.getResources().getIdentifier(randomCategory, "array", context.getPackageName());
        String[] currentArray = context.getResources().getStringArray(arrayResourceId);

        return new ArrayInfo(currentArray, randomCategory);
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
