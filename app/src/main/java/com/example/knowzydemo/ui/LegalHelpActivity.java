package com.example.knowzydemo.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.knowzydemo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class LegalHelpActivity extends AppCompatActivity {

    private static final String HCLSC_PDF_ASSET = "hclsc_panel_contacts.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_help);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        LinearLayout container = findViewById(R.id.legalHelpContainer);
        container.addView(createCard(
                "Email Legal Support",
                "nalsa-dla@nic.in",
                () -> openEmail("nalsa-dla@nic.in")));
        container.addView(createCard(
                "HCLSC Panel Contacts (PDF)",
                "Open full advocate panel with reference numbers",
                this::openHclscPdf));
        container.addView(createCard(
                "National Legal Services Authority",
                "Official website: https://nalsa.gov.in",
                () -> openUrl("https://nalsa.gov.in/")));
        container.addView(createCard(
                "eCourts Services",
                "Case status portal: https://services.ecourts.gov.in",
                () -> openUrl("https://services.ecourts.gov.in/")));
        container.addView(createCard(
                "Legal Aid Helpline",
                "Dial 15100",
                () -> openDialer("15100")));
    }

    private LinearLayout createCard(String title, String subtitle, Runnable action) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18), dp(16), dp(18), dp(16));
        card.setBackgroundResource(R.drawable.bg_glass_card);
        card.setClickable(true);
        card.setFocusable(true);
        card.setOnClickListener(v -> action.run());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp(14));
        card.setLayoutParams(params);

        TextView tvTitle = new TextView(this);
        tvTitle.setText(title);
        tvTitle.setTextColor(0xFFFFFFFF);
        tvTitle.setTextSize(16);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        card.addView(tvTitle);

        TextView tvSubtitle = new TextView(this);
        tvSubtitle.setText(subtitle);
        tvSubtitle.setTextColor(0xFF94A3B8);
        tvSubtitle.setTextSize(14);
        tvSubtitle.setPadding(0, dp(5), 0, 0);
        card.addView(tvSubtitle);

        return card;
    }

    private void openEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Legal help request");
        startActivity(Intent.createChooser(intent, "Email legal support"));
    }

    private void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void openDialer(String number) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)));
    }

    private void openHclscPdf() {
        try {
            InputStream inputStream = getAssets().open(HCLSC_PDF_ASSET);
            File pdfFile = new File(getCacheDir(), HCLSC_PDF_ASSET);
            FileOutputStream outputStream = new FileOutputStream(pdfFile);
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();

            Uri uri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Open contact PDF"));
        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Add the contact PDF as app/src/main/assets/" + HCLSC_PDF_ASSET,
                    Toast.LENGTH_LONG).show();
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
