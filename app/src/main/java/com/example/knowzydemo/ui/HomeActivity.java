package com.example.knowzydemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowzydemo.R;
import com.example.knowzydemo.data.RecentCase;
import com.example.knowzydemo.data.RecentCaseRepository;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final int HOME_RECENT_LIMIT = 5;

    private LinearLayout recentCasesContainer;
    private TextView tvNoRecentCases;
    private TextView btnShowMoreCases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btnStart).setOnClickListener(v -> openAnalyzer());
        findViewById(R.id.btnText).setOnClickListener(v ->
                startActivity(new Intent(this, TextInputActivity.class)));
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

        recentCasesContainer = findViewById(R.id.recentCasesContainer);
        tvNoRecentCases = findViewById(R.id.tvNoRecentCases);
        btnShowMoreCases = findViewById(R.id.btnShowMoreCases);
        btnShowMoreCases.setOnClickListener(v ->
                startActivity(new Intent(this, RecentCasesActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderRecentCases();
    }

    private void openAnalyzer() {
        startActivity(new Intent(this, InputActivity.class));
    }

    private void showComingSoon(String feature) {
        Toast.makeText(this, feature + " is coming soon", Toast.LENGTH_SHORT).show();
    }

    private void renderRecentCases() {
        List<RecentCase> cases = new RecentCaseRepository(this).getAll();
        recentCasesContainer.removeAllViews();

        tvNoRecentCases.setVisibility(cases.isEmpty() ? View.VISIBLE : View.GONE);
        btnShowMoreCases.setVisibility(
                cases.size() > HOME_RECENT_LIMIT ? View.VISIBLE : View.GONE);

        int count = Math.min(HOME_RECENT_LIMIT, cases.size());
        for (int i = 0; i < count; i++) {
            RecentCase recentCase = cases.get(i);
            recentCasesContainer.addView(RecentCaseCardFactory.create(
                    this,
                    recentCase,
                    v -> openResult(recentCase)));
        }
    }

    private void openResult(RecentCase recentCase) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("DATA", recentCase.getAnalysis());
        intent.putExtra("FILE_NAME", recentCase.getFileName());
        startActivity(intent);
    }

}
