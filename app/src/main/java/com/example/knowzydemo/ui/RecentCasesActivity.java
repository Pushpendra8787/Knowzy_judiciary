package com.example.knowzydemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowzydemo.R;
import com.example.knowzydemo.data.RecentCase;
import com.example.knowzydemo.data.RecentCaseRepository;

import java.util.List;

public class RecentCasesActivity extends AppCompatActivity {

    private LinearLayout recentCasesContainer;
    private TextView tvNoRecentCases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_cases);

        recentCasesContainer = findViewById(R.id.recentCasesContainer);
        tvNoRecentCases = findViewById(R.id.tvNoRecentCases);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderCases();
    }

    private void renderCases() {
        List<RecentCase> cases = new RecentCaseRepository(this).getAll();
        recentCasesContainer.removeAllViews();
        tvNoRecentCases.setVisibility(cases.isEmpty() ? View.VISIBLE : View.GONE);

        for (RecentCase recentCase : cases) {
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
