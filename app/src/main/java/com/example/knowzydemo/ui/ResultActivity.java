package com.example.knowzydemo.ui;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowzydemo.R;
import com.example.knowzydemo.ai.ResponseParser;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String data = getIntent().getStringExtra("DATA");

        if (data == null || data.trim().isEmpty()) {
            Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show();
            return;
        }

        LinkedHashMap<String, String> parsed = ResponseParser.parseJudiciaryResponse(data);
        LinearLayout container = findViewById(R.id.resultContainer);

        if (parsed.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText(data);
            tv.setTextSize(15f);
            tv.setTextColor(Color.BLACK);
            container.addView(tv);
            return;
        }

        for (Map.Entry<String, String> entry : parsed.entrySet()) {

            // 🟦 CARD
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setBackgroundResource(R.drawable.card_bg);
            card.setPadding(25, 25, 25, 25);

            LinearLayout.LayoutParams cardParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(0, 25, 0, 0);
            card.setLayoutParams(cardParams);

            // 🟪 HEADING
            TextView heading = new TextView(this);
            heading.setText(entry.getKey());
            heading.setTextSize(17f);
            heading.setTypeface(null, Typeface.BOLD);
            heading.setTextColor(Color.parseColor("#1a237e"));

            // 🟫 CONTENT (Formatted 🔥)
            String formatted = formatText(entry.getValue());

            TextView content = new TextView(this);
            content.setText(
                    android.text.Html.fromHtml(
                            formatted,
                            android.text.Html.FROM_HTML_MODE_LEGACY
                    )
            );
            content.setTextSize(14f);
            content.setTextColor(Color.parseColor("#212121"));
            content.setPadding(0, 10, 0, 0);

            // 🟩 ADD
            card.addView(heading);
            card.addView(content);
            container.addView(card);
        }
    }

    //  TEXT FORMAT FIX FUNCTION
    private String formatText(String text) {

        if (text == null) return "";

        // Remove ** stars
        text = text.replace("**", "");

        //  Line break for numbering
        text = text.replaceAll("(\\d+\\. )", "<br><br>$1");

        // Convert dash to bullet
        text = text.replaceAll("- ", "<br>• ");

        return text.trim();
    }
}