package com.example.vesanje;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private List<CategoryItem> categoriesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoriesList = new ArrayList<>();

        String[] categoriesNames = getResources().getStringArray(R.array.categories);
        for(int i = 0; i < categoriesNames.length; i++){
            String name = categoriesNames[i];
            int img = getResources().getIdentifier(name, "drawable", getPackageName());
            int count = countCategory(name);

            CategoryItem category = new CategoryItem(name, img, count);

            categoriesList.add(category);
        }

        CategoryAdapter adapter = new CategoryAdapter(categoriesList);
        recyclerView.setAdapter(adapter);

        ImageView pvpButton = findViewById(R.id.pvp_button);
        ImageView randomButton = findViewById(R.id.random_button);

        adapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CategoryItem item) {
                String categoryName = item.getCategoryName();
                startGame(categoryName);
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
