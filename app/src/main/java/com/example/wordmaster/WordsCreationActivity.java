package com.example.wordmaster;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
public class WordsCreationActivity extends AppCompatActivity {

    int currentPlayer = 1;
    int totalPlayers;
    int wordsPerPlayer;

    EditText etWord1, etWord2, etWord3, etWord4, etWord5;
    TextView tvPlayerTitle;
    Button btnNextPlayer;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_creation);

        totalPlayers = getIntent().getIntExtra("PLAYER_COUNT", 6);
        wordsPerPlayer = getIntent().getIntExtra("WORD_COUNT", 5);

        dbHelper = new DatabaseHelper(this);

        tvPlayerTitle = findViewById(R.id.tvPlayerTitle);
        etWord1 = findViewById(R.id.etWord1);
        etWord2 = findViewById(R.id.etWord2);
        etWord3 = findViewById(R.id.etWord3);
        etWord4 = findViewById(R.id.etWord4);
        etWord5 = findViewById(R.id.etWord5);
        btnNextPlayer = findViewById(R.id.btnNextPlayer);

        updateTitle();

        btnNextPlayer.setOnClickListener(v -> {
            String w1 = etWord1.getText().toString().trim();
            String w2 = etWord2.getText().toString().trim();
            String w3 = etWord3.getText().toString().trim();
            String w4 = etWord4.getText().toString().trim();
            String w5 = etWord5.getText().toString().trim();

            if (w1.isEmpty() || w2.isEmpty() || w3.isEmpty() || w4.isEmpty() || w5.isEmpty()) {
                Toast.makeText(this, "Συμπλήρωσε όλες τις λέξεις!", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.insertWord(currentPlayer, w1);
            dbHelper.insertWord(currentPlayer, w2);
            dbHelper.insertWord(currentPlayer, w3);
            dbHelper.insertWord(currentPlayer, w4);
            dbHelper.insertWord(currentPlayer, w5);

            if (currentPlayer < totalPlayers) {
                currentPlayer++;
                updateTitle();
                clearFields();
            } else {
                Intent intent = new Intent(this, TurnActivity.class);
                intent.putExtra("CURRENT_ROUND", 1);
                intent.putExtra("SCORE1", 0);
                intent.putExtra("SCORE2", 0);
                startActivity(intent);
            }
        });
    }

    void updateTitle() {
        tvPlayerTitle.setText("Player " + currentPlayer + " / " + totalPlayers);
    }

    void clearFields() {
        etWord1.setText("");
        etWord2.setText("");
        etWord3.setText("");
        etWord4.setText("");
        etWord5.setText("");
    }
}