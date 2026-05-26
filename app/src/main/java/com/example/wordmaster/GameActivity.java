package com.example.wordmaster;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class GameActivity extends AppCompatActivity {

    private int playerCount = 6;
    private int wordCount = 5;
    private long lastMessageTime = 0;
    private static final int MESSAGE_COOLDOWN = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TextView tvPlayers = findViewById(R.id.tvPlayers);
        TextView tvWords = findViewById(R.id.tvWords);
        Button btnPlayersDown = findViewById(R.id.btnPlayersDown);
        Button btnPlayersUp = findViewById(R.id.btnPlayersUp);
        Button btnWordsDown = findViewById(R.id.btnWordsDown);
        Button btnWordsUp = findViewById(R.id.btnWordsUp);
        Button btnNext = findViewById(R.id.btnNext);

        // Initial UI state
        updateButtonStates(btnPlayersDown, btnPlayersUp, playerCount, 4, 25);
        updateButtonStates(btnWordsDown, btnWordsUp, wordCount, 3, 10);

        // Players Logic
        btnPlayersDown.setOnClickListener(v -> {
            if (playerCount > 4) {
                playerCount--;
                tvPlayers.setText(String.valueOf(playerCount));
                updateButtonStates(btnPlayersDown, btnPlayersUp, playerCount, 4, 25);
            } else {
                showMessage("4 you have hit the limmit");
            }
        });

        btnPlayersUp.setOnClickListener(v -> {
            if (playerCount < 25) {
                playerCount++;
                tvPlayers.setText(String.valueOf(playerCount));
                updateButtonStates(btnPlayersDown, btnPlayersUp, playerCount, 4, 25);
            } else {
                showMessage("25 you have hit the limmit");
            }
        });

        // Words Logic
        btnWordsDown.setOnClickListener(v -> {
            if (wordCount > 3) {
                wordCount--;
                tvWords.setText(String.valueOf(wordCount));
                updateButtonStates(btnWordsDown, btnWordsUp, wordCount, 3, 10);
            } else {
                showMessage("3 you have hit the limmit");
            }
        });

        btnWordsUp.setOnClickListener(v -> {
            if (wordCount < 10) {
                wordCount++;
                tvWords.setText(String.valueOf(wordCount));
                updateButtonStates(btnWordsDown, btnWordsUp, wordCount, 3, 10);
            } else {
                showMessage("10 you have hit the limmit");
            }
        });

        // Next Button
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, WordsCreationActivity.class);
            intent.putExtra("PLAYER_COUNT", playerCount);
            intent.putExtra("WORD_COUNT", wordCount);
            startActivity(intent);
        });
    }

    private void showMessage(String message) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMessageTime > MESSAGE_COOLDOWN) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
            
            // Get the Snackbar view
            View snackbarView = snackbar.getView();
            
            // Set width to WRAP_CONTENT and center it
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
            params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            params.setMargins(0, 0, 0, 150); // Margin from bottom
            snackbarView.setLayoutParams(params);
            
            // Center text inside the Snackbar
            TextView tv = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            if (tv != null) {
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            
            snackbar.show();
            lastMessageTime = currentTime;
        }
    }

    private void updateButtonStates(Button btnDown, Button btnUp, int current, int min, int max) {
        // Update Down button text visibility using textColor
        if (current <= min) {
            btnDown.setTextColor(Color.TRANSPARENT);
        } else {
            btnDown.setTextColor(Color.BLACK);
        }

        // Update Up button text visibility using textColor
        if (current >= max) {
            btnUp.setTextColor(Color.TRANSPARENT);
        } else {
            btnUp.setTextColor(Color.BLACK);
        }
    }
}
