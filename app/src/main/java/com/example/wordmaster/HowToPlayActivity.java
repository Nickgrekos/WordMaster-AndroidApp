package com.example.wordmaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HowToPlayActivity extends AppCompatActivity {

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        backButton = findViewById(R.id.btnBackHome);

        backButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HowToPlayActivity.this,
                    MainActivity.class
            );

            startActivity(intent);

            finish();
        });
    }
}