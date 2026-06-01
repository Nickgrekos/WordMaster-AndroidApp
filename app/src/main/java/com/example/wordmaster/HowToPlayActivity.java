package com.example.wordmaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class HowToPlayActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        Button backButton = findViewById(R.id.btnBackHome);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(HowToPlayActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}