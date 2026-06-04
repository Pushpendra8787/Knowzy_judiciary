package com.example.knowzydemo.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowzydemo.R;

public class AiAnswerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_answer);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        String answer = getIntent().getStringExtra("ANSWER");
        if (answer == null || answer.trim().isEmpty()) {
            Toast.makeText(this, "No answer received", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView tvAnswer = findViewById(R.id.tvAnswer);
        tvAnswer.setText(answer);
    }
}
