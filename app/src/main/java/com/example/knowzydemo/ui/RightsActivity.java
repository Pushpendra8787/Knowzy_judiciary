package com.example.knowzydemo.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowzydemo.R;

public class RightsActivity extends AppCompatActivity {

    private final String[][] topics = {
            {"P", "If Police Stops You",
                    "Your Rights\n- Ask why you are being stopped.\n- You can ask for officer identification.\n- Police cannot physically abuse or threaten you.\n- You have the right to remain silent except basic identification.\n- Female suspects should generally be questioned by female officers.\n\nWhat To Do\n- Stay calm.\n- Do not argue aggressively.\n- Record details if safe.\n- Ask reason politely.\n- Contact lawyer/family if needed.\n\nEmergency Help\n- Dial 112\n- Contact lawyer"},
            {"L", "Right to Lawyer",
                    "Your Rights\n- You may consult a lawyer before answering detailed questions.\n- Ask for legal help if you are detained or arrested.\n- You can inform a family member.\n\nWhat To Do\n- Clearly request a lawyer.\n- Keep a note of names, time, and location.\n\nEmergency Help\n- Dial 112\n- Contact your local legal aid office"},
            {"F", "FIR Rights",
                    "Your Rights\n- You can report a cognizable offence at a police station.\n- Ask for a free copy of the FIR.\n- Read the statement carefully before signing.\n\nWhat To Do\n- Provide dates, places, and evidence clearly.\n- Keep a copy of the complaint and FIR number."},
            {"D", "Domestic Violence Rights",
                    "Your Rights\n- You have the right to protection from physical, verbal, emotional, and financial abuse.\n- You may seek protection and shelter support.\n\nWhat To Do\n- Move to a safe place if possible.\n- Save evidence and contact a trusted person.\n\nEmergency Help\n- Dial 112\n- Contact the women helpline"},
            {"W", "Workplace Harassment Rights",
                    "Your Rights\n- You can report unwelcome conduct at work.\n- Your complaint should be handled confidentially.\n\nWhat To Do\n- Save messages and note witnesses.\n- Contact your workplace complaints committee."},
            {"W", "Women Safety Rights",
                    "Your Rights\n- You can seek immediate help when you feel unsafe.\n- Ask for a female officer where appropriate.\n\nWhat To Do\n- Move to a public or safe location.\n- Share your location with a trusted person.\n\nEmergency Help\n- Dial 112"},
            {"C", "Child Protection Rights",
                    "Your Rights\n- Children must be protected from abuse, exploitation, and neglect.\n- A child-friendly reporting process should be used.\n\nWhat To Do\n- Contact a trusted adult.\n- Record facts without pressuring the child.\n\nEmergency Help\n- Dial 1098"},
            {"T", "Tenant Rights",
                    "Your Rights\n- Ask for a written rental agreement and receipts.\n- Landlords should follow the legal process for eviction.\n\nWhat To Do\n- Keep payment records and messages.\n- Seek legal help for threats or unlawful eviction."}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rights);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        LinearLayout container = findViewById(R.id.topicsContainer);
        for (String[] topic : topics) {
            container.addView(createTopicCard(topic));
        }
    }

    private View createTopicCard(String[] topic) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(android.view.Gravity.CENTER_VERTICAL);
        card.setPadding(dp(18), dp(18), dp(18), dp(18));
        card.setBackgroundResource(R.drawable.bg_glass_card);
        card.setClickable(true);
        card.setFocusable(true);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, dp(10));
        card.setLayoutParams(cardParams);

        TextView icon = new TextView(this);
        icon.setText(topic[0]);
        icon.setGravity(android.view.Gravity.CENTER);
        icon.setTextColor(0xFFFFFFFF);
        icon.setTextSize(17);
        icon.setBackgroundResource(R.drawable.bg_icon_circle);
        card.addView(icon, new LinearLayout.LayoutParams(dp(52), dp(52)));

        TextView title = new TextView(this);
        title.setText(topic[1]);
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(17);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        titleParams.setMargins(dp(16), 0, dp(8), 0);
        card.addView(title, titleParams);

        TextView arrow = new TextView(this);
        arrow.setText(">");
        arrow.setTextColor(0xFF93A4F7);
        arrow.setTextSize(21);
        arrow.setTypeface(null, android.graphics.Typeface.BOLD);
        card.addView(arrow);

        card.setOnClickListener(v -> showTopic(topic[1], topic[2]));
        return card;
    }

    private void showTopic(String title, String content) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_rights, null);
        dialog.setContentView(view);

        ((TextView) view.findViewById(R.id.tvDialogTitle)).setText(title);
        ((TextView) view.findViewById(R.id.tvDialogContent)).setText(content);
        view.findViewById(R.id.btnClose).setOnClickListener(v -> dialog.dismiss());

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setOnShowListener(unused -> {
            Window shownWindow = dialog.getWindow();
            if (shownWindow != null) {
                shownWindow.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
        dialog.show();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
