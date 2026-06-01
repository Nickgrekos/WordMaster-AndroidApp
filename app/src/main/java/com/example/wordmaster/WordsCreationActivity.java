package com.example.wordmaster;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class WordsCreationActivity extends BaseActivity {

    private int currentPlayer = 1;
    private int totalPlayers;
    private int wordsPerPlayer;

    private TextView tvPlayerTitle;
    private Button btnNextPlayer;
    private LinearLayout wordsContainer;
    private DatabaseHelper dbHelper;
    private List<EditText> wordInputFields = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_creation);

        totalPlayers = getIntent().getIntExtra("PLAYER_COUNT", 6);
        wordsPerPlayer = getIntent().getIntExtra("WORD_COUNT", 5);

        dbHelper = new DatabaseHelper(this);

        tvPlayerTitle = findViewById(R.id.tvPlayerTitle);
        wordsContainer = findViewById(R.id.wordsContainer);
        btnNextPlayer = findViewById(R.id.btnNextPlayer);
        btnNextPlayer.setText("Επόμενος Παίκτης");

        updateTitle();
        generateWordFields();

        btnNextPlayer.setOnClickListener(v -> {
            List<String> words = new ArrayList<>();
            for (EditText et : wordInputFields) {
                String w = et.getText().toString().trim();
                if (w.isEmpty()) {
                    Toast.makeText(this, "Συμπλήρωσε όλες τις λέξεις!", Toast.LENGTH_SHORT).show();
                    return;
                }
                words.add(w);
            }

            for (String w : words) {
                dbHelper.insertWord(currentPlayer, w);
            }

            if (currentPlayer < totalPlayers) {
                currentPlayer++;
                updateTitle();
                clearFields();
            } else {
                startTurnActivity();
            }
        });
    }

    private void generateWordFields() {
        wordsContainer.removeAllViews();
        wordInputFields.clear();
        float density = getResources().getDisplayMetrics().density;

        for (int i = 1; i <= wordsPerPlayer; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            rowParams.setMargins(0, 0, 0, (int) (12 * density));
            row.setLayoutParams(rowParams);

            TextView tvIndex = new TextView(this);
            tvIndex.setText(i + ".");
            tvIndex.setTextSize(16);
            tvIndex.setTextColor(Color.parseColor("#1B5E20"));
            tvIndex.setTypeface(null, android.graphics.Typeface.BOLD);
            LinearLayout.LayoutParams indexParams = new LinearLayout.LayoutParams(
                    (int) (32 * density),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            tvIndex.setLayoutParams(indexParams);

            EditText etWord = new EditText(this);
            etWord.setHint("Γράψε μια λέξη...");
            etWord.setTextColor(Color.parseColor("#212121")); // Dark text
            etWord.setHintTextColor(Color.parseColor("#9E9E9E")); // Lighter hint
            etWord.setPadding((int) (16 * density), 0, 0, 0);
            etWord.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
            etWord.setBackground(ContextCompat.getDrawable(this, R.drawable.input_bg));
            
            LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(
                    0,
                    (int) (52 * density),
                    1.0f
            );
            etWord.setLayoutParams(etParams);

            row.addView(tvIndex);
            row.addView(etWord);
            wordsContainer.addView(row);
            wordInputFields.add(etWord);
        }
    }

    private void updateTitle() {
        tvPlayerTitle.setText("Player " + currentPlayer + " / " + totalPlayers);
        if (currentPlayer == totalPlayers) {
            btnNextPlayer.setText("Έναρξη Παιχνιδιού");
        }
    }

    private void clearFields() {
        for (EditText et : wordInputFields) {
            et.setText("");
        }

        if (!wordInputFields.isEmpty()) {
            wordInputFields.get(0).requestFocus();
        }
    }

    private void startTurnActivity() {
        Intent intent = new Intent(this, TurnActivity.class);
        int startingTeam = new java.util.Random().nextInt(2);
        intent.putExtra("CURRENT_ROUND", 1);
        intent.putExtra("SCORE1", 0);
        intent.putExtra("SCORE2", 0);
        intent.putExtra("CURRENT_TEAM", startingTeam);
        startActivity(intent);
    }
}