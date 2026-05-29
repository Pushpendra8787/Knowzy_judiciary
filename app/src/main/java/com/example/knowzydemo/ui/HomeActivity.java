package com.example.knowzydemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowzydemo.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btnStart).setOnClickListener(v -> openAnalyzer());
        findViewById(R.id.btnText).setOnClickListener(v -> openAnalyzer());
        findViewById(R.id.btnVoice).setOnClickListener(v -> showComingSoon("Voice input"));
        findViewById(R.id.btnEmergency).setOnClickListener(v ->
                showComingSoon("Emergency mode"));
        findViewById(R.id.btnMenu).setOnClickListener(v -> showComingSoon("Menu"));
        findViewById(R.id.btnInfo).setOnClickListener(v ->
                showComingSoon("App information"));
        findViewById(R.id.btnKnowRights).setOnClickListener(v ->
                startActivity(new Intent(this, RightsActivity.class)));
        findViewById(R.id.btnLegalHelp).setOnClickListener(v ->
                startActivity(new Intent(this, LegalHelpActivity.class)));
        findViewById(R.id.btnHelpline).setOnClickListener(v ->
                startActivity(new Intent(this, HelplineActivity.class)));
        findViewById(R.id.btnNearby).setOnClickListener(v ->
                startActivity(new Intent(this, NearbyHelpActivity.class)));
    }

    private void openAnalyzer() {
        startActivity(new Intent(this, InputActivity.class));
    }

    private void showComingSoon(String feature) {
        Toast.makeText(this, feature + " is coming soon", Toast.LENGTH_SHORT).show();
    }

}
