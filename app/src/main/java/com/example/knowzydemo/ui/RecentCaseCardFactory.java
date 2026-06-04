package com.example.knowzydemo.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.knowzydemo.R;
import com.example.knowzydemo.data.RecentCase;

import java.text.DateFormat;
import java.util.Date;

public class RecentCaseCardFactory {

    public static View create(Context context, RecentCase recentCase, View.OnClickListener listener) {
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(context, 18), dp(context, 16), dp(context, 18), dp(context, 16));
        card.setBackgroundResource(R.drawable.bg_glass_card);
        card.setClickable(true);
        card.setFocusable(true);
        card.setOnClickListener(listener);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, dp(context, 10));
        card.setLayoutParams(cardParams);

        TextView title = new TextView(context);
        title.setText(recentCase.getFileName());
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(15);
        title.setTypeface(null, Typeface.BOLD);
        card.addView(title);

        TextView status = new TextView(context);
        String date = recentCase.getAnalyzedAt() > 0
                ? DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                        .format(new Date(recentCase.getAnalyzedAt()))
                : "";
        status.setText(date.isEmpty() ? "Analyzed" : "Analyzed - " + date);
        status.setTextColor(0xFF94A3B8);
        status.setTextSize(12);
        status.setPadding(0, dp(context, 6), 0, 0);
        card.addView(status);

        return card;
    }

    private static int dp(Context context, int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }
}
