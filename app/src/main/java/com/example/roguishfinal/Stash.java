package com.example.roguishfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Stash extends AppCompatActivity implements SensorEventListener {
    boolean inEncounter;
    boolean isSelected = false;
    int cardSelection = 0;

    Stasher stasher;

    Entity enemy;

    PlayerDeck deck;
    Card currentCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stash);

        // Obtain the deck from the previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            this.enemy = (Entity) extras.getSerializable("enemy");
            this.deck = (PlayerDeck) extras.getSerializable("deck");
            this.inEncounter = extras.getBoolean("encounter");
        }

        // Initialize the stash
        stasher = new Stasher(this);
        stasher.initializeStash();
        displayStash();

        // Initialize Listeners
        LinearLayout card = (LinearLayout) findViewById(R.id.stashCardArea);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCard();
            }
        });
        card.setOnTouchListener(new OnSwipeTouchListener(this) {
            public boolean onSwipeRight() {
                changeCard(true);
                return true;
            }
            public boolean onSwipeLeft() {
                changeCard(false);
                return true;
            }
        });

        // Accelerometer Setup
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer != null) {
            manager.registerListener(this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    // Exit the stash
    public void onClickExit(View view) {
        finish();
    }

    public void onClickClear(View view) {
        this.stasher.clear();
        this.currentCard = this.deck.getCard(0);
        this.updateCard();
    }

    // Allow the current card to be interacted with
    public void selectCard() {
        this.isSelected = !this.isSelected;
        updateCard();
    }

    public void changeCard(boolean reverse) {
        // Find direction to move
        this.cardSelection = reverse ? this.cardSelection - 1 : this.cardSelection + 1;

        // Handle out of bounds
        if(this.cardSelection >= 3)
            this.cardSelection = 0;
        else if(this.cardSelection < 0)
            this.cardSelection = 2;

        this.currentCard = this.deck.getCard(this.stasher.stashedValues[this.cardSelection]);
        updateCard();
    }

    // Update the card image to the current card
    public void updateCard() {
        Card card = this.currentCard;
        TextView title = (TextView) findViewById(R.id.stashCardTitle);
        title.setText(card.getName());

        TextView desc = (TextView) findViewById(R.id.stashCardDescription);
        desc.setText(card.getDescription());

        if(isSelected) {
            title.setBackgroundColor(
                    ContextCompat.getColor(
                            this,
                            R.color.colorSecondary
                    )
            );
        }
        else {
            title.setBackgroundColor(
                    ContextCompat.getColor(
                            this,
                            R.color.colorTertiary
                    )
            );
        }
    }

    // Update information
    public void displayStash() {
        try {
            stasher.fetchData();
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        // Stash titles
        TextView stashTitle = (TextView) findViewById(R.id.stashCardTitle);
        stashTitle.setText(this.deck.getCard(this.stasher.stashedValues[this.cardSelection]).getName());

        // Stash Descriptions
        TextView stashDescription = (TextView) findViewById(R.id.stashCardDescription);
        stashDescription.setText(this.deck.getCard(this.stasher.stashedValues[this.cardSelection]).getDescription());
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float acc_x = sensorEvent.values[1];
        if(this.inEncounter && cardSelection >= 0 && acc_x < -3.0) {
            if(this.currentCard.getTarget().equals(Target.SELF))
                this.currentCard.playCard.playCard(this.deck.getOwner());
            else if(this.currentCard.getTarget().equals(Target.ENEMY))
                this.currentCard.playCard.playCard(enemy);

            this.stasher.put(cardSelection, 0);
            finish();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { /* Return nothing */ }
}