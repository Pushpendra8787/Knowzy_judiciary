package com.example.knowzydemo.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowzydemo.R;

public class HelplineActivity extends AppCompatActivity {

    private final String[][] primaryNumbers = {
            {"112", "112", "Integrated Helpline\n(ERSS)"},
            {"102", "102", "National Ambulance\nService"},
            {"100", "100", "Police Helpline"},
            {"101", "101", "Fire Helpline"}
    };

    private final String[][] otherNumbers = {
            {"1930", "1930", "Cyber Crime Helpline"},
            {"1906", "1906", "LPG Emergency Helpline"},
            {"1070", "1070", "Relief Commissioner\n(Natural Calamities)"},
            {"1073", "1073", "Road Accident Helpline"},
            {"1071", "1071", "Air Accident"},
            {"1072", "1072", "Train Accident"},
            {"1098", "1098", "Child Abuse Helpline"},
            {"181", "181", "Domestic Abuse &\nWomen Helpline"},
            {"1091", "1091", "Women Helpline"},
            {"1092", "1092", "Anti Obscene Calls"},
            {"139", "139", "Railway Security &\nMedical Assistance"},
            {"1363", "1363", "Tourist Helpline"},
            {"14567", "14567", "Senior Citizen Helpline"},
            {"14456", "14456", "Disability Helpline"},
            {"15100", "15100", "Legal Aid Helpline"},
            {"1947", "1947", "Aadhaar Helpline"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        addGrid((LinearLayout) findViewById(R.id.primaryHelplineGrid), primaryNumbers, true);
        addGrid((LinearLayout) findViewById(R.id.otherHelplineGrid), otherNumbers, false);
    }

    private void addGrid(LinearLayout container, String[][] numbers, boolean primary) {
        LinearLayout row = null;
        for (int i = 0; i < numbers.length; i++) {
            if (i % 2 == 0) {
                row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                container.addView(row, new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            LinearLayout card = createNumberCard(numbers[i], primary);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, primary ? dp(132) : dp(124), 1);
            params.setMargins(i % 2 == 0 ? 0 : dp(5), 0, i % 2 == 0 ? dp(5) : 0, dp(10));
            row.addView(card, params);
        }
    }

    private LinearLayout createNumberCard(String[] number, boolean primary) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(20), dp(18), dp(16), dp(12));
        card.setBackgroundResource(R.drawable.bg_glass_card);
        card.setClickable(true);
        card.setFocusable(true);
        card.setOnClickListener(v -> openDialer(number[1]));

        TextView tvNumber = new TextView(this);
        tvNumber.setText(number[0]);
        tvNumber.setTextColor(primary ? 0xFFFFFFFF : 0xFF7CC7FF);
        tvNumber.setTextSize(primary ? 34 : 28);
        tvNumber.setTypeface(null, android.graphics.Typeface.BOLD);
        card.addView(tvNumber);

        TextView tvLabel = new TextView(this);
        tvLabel.setText(number[2]);
        tvLabel.setTextColor(0xFFFFFFFF);
        tvLabel.setTextSize(primary ? 16 : 14);
        tvLabel.setPadding(0, dp(primary ? 16 : 12), 0, 0);
        tvLabel.setTypeface(null, primary ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        card.addView(tvLabel);

        return card;
    }

    private void openDialer(String number) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)));
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
