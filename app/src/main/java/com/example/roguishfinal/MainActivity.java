package com.example.roguishfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Routes for buttons
    public void onClickStash(View view) {
        Entity tempPlayer = new Entity(
                "temp",
                100,
                0,
                0
        );
        Entity tempEnemy = new Entity(
                "temp",
                100,
                0,
                0
        );
        PlayerDeck deck = new PlayerDeck();

        Intent intent = new Intent(getBaseContext(), Stash.class);
        intent.putExtra("deck", deck);
        intent.putExtra("encounter", false);
        startActivity(intent);
    }

    public void onClickEncounter(View view) {
        Intent intent = new Intent(getBaseContext(), Encounter.class);
        startActivity(intent);
    }

    public void onClickCredits(View view) {
        Intent intent = new Intent(getBaseContext(), Credits.class);
        startActivity(intent);
    }
}