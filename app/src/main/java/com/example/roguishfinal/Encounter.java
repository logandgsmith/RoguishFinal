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

        // Setup Accelerometer
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer != null) {
            manager.registerListener(this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI);
        }

        // Setup listeners for the card area
        LinearLayout card = (LinearLayout) findViewById(R.id.cardArea);
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

        // Setup Activity Pane
        TextView latestActivity = (TextView) findViewById(R.id.latestActivity);
        latestActivity.setText("Encountered Xythoid Elite!");

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

    ///// Mutators /////
    // Allow the current card to be interacted with
    public void selectCard() {
        this.isSelected = !this.isSelected;
        updateCard();

        if(isSelected)
            updateActivity("Selected " + currentCard.getName() + ".");
        else
            updateActivity("Unselected " + currentCard.getName() + ".");
    }

    // Advance (false) or retreat (true) the current card cursor
    public void changeCard(boolean reverse) {
        this.currentCard = this.playerDeck.getNext(reverse);
        this.updateCard();
    }

    // Update the card image to the current card
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

    // Change health text to current values
    public void updateHealth() {
        String playerHealthText = "Player Health: " + this.playerDeck.getOwner().getHealth();
        String enemyHealthText = "Enemy Health: " + this.enemy.getHealth();

        TextView playerHealth = (TextView) findViewById(R.id.playerHealth);
        playerHealth.setText(playerHealthText);

        TextView enemyHealth = (TextView) findViewById(R.id.enemyHealth);
        enemyHealth.setText(enemyHealthText);
    }

    // TODO: Create a status listener and update the table dynamically
    // Update statuses with current values
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

    public void updateActivity(String newActivity) {
        TextView latestActivity = (TextView) findViewById(R.id.latestActivity);
        latestActivity.setText(newActivity);
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
            if(this.currentCard.getTarget().equals(Target.SELF))
                this.currentCard.playCard.playCard(this.playerDeck.getOwner());
            else if(this.currentCard.getTarget().equals(Target.ENEMY))
                this.currentCard.playCard.playCard(enemy);
            this.isSelected = false;

            // Just attack the player for now
            // TODO: Make the enemy take a full move
            if(this.enemy.getHealth() > 0)
                this.enemy.attack(this.playerDeck.getOwner());

            // Upkeep
            this.playerDeck.getOwner().upkeep();
            this.enemy.upkeep();
            this.playerDeck.drawHand();
            this.updateAll();

            // Check for game overs
            if(this.playerDeck.getOwner().getIsFinished())
                updateActivity("YOU DIED. Please hit exit.");
            else if(this.enemy.getIsFinished())
                updateActivity("YOU WIN! Please hit exit.");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { /* Return */ }
}