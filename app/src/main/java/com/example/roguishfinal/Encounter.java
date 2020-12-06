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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Encounter extends AppCompatActivity implements SensorEventListener {
    ///// Member Variables /////
    // Player Deck
    private PlayerDeck playerDeck = new PlayerDeck();
    private boolean isSelected = false;
    private Card currentCard =  playerDeck.getCurrent();

    // Enemy Deck
    // TODO: Implement new enemy decks
    private Entity enemy = new Entity(
            "TestEnemy",
            100,
            10,
            2
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter);

        // Accelerometer Setup
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer != null) {
            manager.registerListener(this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI);
        }

        // Setup UI
        this.updateAll();
    }

    ///// On Click Listeners /////
    public void onClickStash(View view) {
        Intent intent = new Intent(getBaseContext(), Stash.class);
        startActivity(intent);
    }

    public void onClickExit(View view) {
        finish();
    }

    public void onClickSelect(View view) {
        this.isSelected = !this.isSelected;
        updateCard();
    }

    public void onClickNext(View view) {
        this.currentCard = this.playerDeck.getNext();
        this.updateCard();
    }

    ///// Mutators /////
    public void updateCard() {
        Card card = this.currentCard;
        TextView title = (TextView) findViewById(R.id.cardTitle);
        title.setText(card.getName());

        TextView desc = (TextView) findViewById(R.id.cardDescription);
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

    public void updateHealth() {
        String playerHealthText = "Player Health: " + this.playerDeck.getOwner().getHealth();
        String enemyHealthText = "Enemy Health: " + this.enemy.getHealth();

        TextView playerHealth = (TextView) findViewById(R.id.playerHealth);
        playerHealth.setText(playerHealthText);

        TextView enemyHealth = (TextView) findViewById(R.id.enemyHealth);
        enemyHealth.setText(enemyHealthText);
    }

    // TODO: Create a status listener and update the table dynamically
    public void updateStatuses() {
        String playerText = "Evasion: " + this.playerDeck.getOwner().getEvasion();
        String enemyText = "Poison: " + this.enemy.getPoison();

        TextView playerHealth = (TextView) findViewById(R.id.playerEffectOne);
        playerHealth.setText(playerText);

        TextView enemyHealth = (TextView) findViewById(R.id.enemyEffectOne);
        enemyHealth.setText(enemyText);
    }

    // Update all UI Elements at once
    public void updateAll() {
        this.updateCard();
        this.updateHealth();
        this.updateStatuses();
    }

    ///// Sensor Listeners /////
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float acc_x = sensorEvent.values[1];
        float acc_y = sensorEvent.values[2];

        // Attempt to play the card
        if (this.playerDeck.getOwner().getHealth() > 0
                && this.isSelected
                && this.enemy.getHealth() > 0
                && acc_x < -0.5
                && acc_y > 5.0) {
            if(this.currentCard.getTarget().equals("self"))
                this.currentCard.playCard.playCard(this.playerDeck.getOwner());
            else if(this.currentCard.getTarget().equals("enemy"))
                this.currentCard.playCard.playCard(enemy);
            this.isSelected = false;

            // Just attack the player for now
            // TODO: Make the enemy take a full move
            if(this.enemy.getHealth() > 0)
                this.enemy.attack(this.playerDeck.getOwner());

            // Upkeep
            this.playerDeck.getOwner().upkeep();
            this.enemy.upkeep();
            this.updateAll();

            // Check for game overs
            if(this.playerDeck.getOwner().getIsFinished()) {
                TextView status = (TextView) findViewById(R.id.encounterTitle);
                status.setText("YOU DIED. Hit Exit.");
            }
            else if(this.enemy.getIsFinished()) {
                TextView status = (TextView) findViewById(R.id.encounterTitle);
                status.setText("YOU WIN! Hit Exit.");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}