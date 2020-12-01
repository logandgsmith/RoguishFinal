package com.example.roguishfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Encounter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter);
    }

    public void onClickStash(View view) {
        Intent intent = new Intent(getBaseContext(), Stash.class);
        startActivity(intent);
    }

    public void onClickExit(View view) {
        finish();
    }
}