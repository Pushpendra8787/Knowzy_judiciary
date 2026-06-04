package com.example.knowzydemo.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.database.Cursor;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowzydemo.BuildConfig;
import com.example.knowzydemo.R;
import com.example.knowzydemo.ai.GroqApiService;
import com.example.knowzydemo.ai.PromptBuilder;
import com.example.knowzydemo.ai.RetrofitClient;
import com.example.knowzydemo.ai.models.GroqRequest;
import com.example.knowzydemo.ai.models.GroqResponse;
import com.example.knowzydemo.data.RecentCaseRepository;
import com.example.knowzydemo.utils.OcrUtils;
import com.example.knowzydemo.utils.PdfUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputActivity extends AppCompatActivity {

    private static final int PICK_PDF = 1;
    private static final String TAG = "InputActivity";
    private static final String API_KEY = BuildConfig.GROQ_API_KEY;

    // UI elements
    private Button btnUpload;
    private ProgressBar progressBar;
    private TextView tvStatus;
    private TextView tvPageCount;
    private String selectedFileName = "Analyzed document.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        btnUpload    = findViewById(R.id.btnUpload);
        progressBar  = findViewById(R.id.progressBar);
        tvStatus     = findViewById(R.id.tvStatus);
        tvPageCount  = findViewById(R.id.tvPageCount);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Initially hide progress UI
        progressBar.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);
        tvPageCount.setVisibility(View.GONE);

        btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, PICK_PDF);
        });
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (req == PICK_PDF && res == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                selectedFileName = getDisplayName(uri);
                processPDF(uri);
            }
            else Toast.makeText(this, "Invalid file", Toast.LENGTH_SHORT).show();
        }
    }

    private void processPDF(Uri uri) {

        // Show progress UI, disable button
        runOnUiThread(() -> {
            btnUpload.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            tvStatus.setVisibility(View.VISIBLE);
            tvPageCount.setVisibility(View.VISIBLE);
            tvStatus.setText("📄 Reading PDF...");
            tvPageCount.setText("");
        });

        PdfUtils.convertToImages(this, uri, bitmaps -> {

            int totalPages = bitmaps.length;
            Log.d(TAG, "PDF pages: " + totalPages);

            if (totalPages == 0) {
                showError("PDF conversion failed");
                resetUI();
                return;
            }

            // Limit to first 10 pages for speed

            int pagesToProcess = Math.min(totalPages,totalPages);

            runOnUiThread(() -> {
                progressBar.setIndeterminate(false);
                progressBar.setMax(pagesToProcess);
                progressBar.setProgress(0);
                tvStatus.setText("🔍 Running OCR...");
                tvPageCount.setText("Page 0 / " + pagesToProcess
                        + (totalPages > 10 ? "  (showing first 10 of "
                        + totalPages + " pages)" : ""));
            });

            // Trim to pagesToProcess pages
            android.graphics.Bitmap[] trimmed =
                    new android.graphics.Bitmap[pagesToProcess];
            System.arraycopy(bitmaps, 0, trimmed, 0, pagesToProcess);

            OcrUtils.runOCRWithProgress(trimmed, (currentPage, totalPgs) -> {

                // Called after each page completes
                runOnUiThread(() -> {
                    progressBar.setProgress(currentPage);
                    tvPageCount.setText("Page " + currentPage
                            + " / " + totalPgs);

                    int percent = (currentPage * 100) / totalPgs;
                    tvStatus.setText("🔍 OCR in progress... " + percent + "%");
                });

            }, extractedText -> {

                if (extractedText != null && extractedText.trim().length() > 50) {

                    runOnUiThread(() -> {
                        progressBar.setIndeterminate(true);
                        tvStatus.setText("🤖 Analyzing with AI...");
                        tvPageCount.setText("Please wait...");
                    });

                    sendToAI(extractedText);

                } else {
                    showError("OCR failed: Could not extract text");
                    resetUI();
                }
            });
        });
    }

    private void sendToAI(String text) {
        if (API_KEY == null || API_KEY.trim().isEmpty()) {
            showError("Missing Groq API key. Please set GROQ_API_KEY in local.properties.");
            resetUI();
            return;
        }

        if (text.length() > 3000) text = text.substring(0, 3000);

        String prompt = PromptBuilder.buildJudiciaryPrompt(text);
        List<GroqRequest.Message> messages = new ArrayList<>();
        messages.add(new GroqRequest.Message("user", prompt));

        GroqRequest request = new GroqRequest("llama-3.1-8b-instant", messages);
        GroqApiService api = RetrofitClient.getClient().create(GroqApiService.class);

        api.getAIResponse("Bearer " + API_KEY, request)
                .enqueue(new Callback<GroqResponse>() {

                    @Override
                    public void onResponse(Call<GroqResponse> call,
                                           Response<GroqResponse> response) {
                        resetUI();

                        try {
                            if (response.isSuccessful()
                                    && response.body() != null
                                    && response.body().getChoices() != null
                                    && !response.body().getChoices().isEmpty()) {

                                String result = response.body()
                                        .getChoices().get(0)
                                        .getMessage().getContent();

                                if (result != null && !result.trim().isEmpty()) {
                                    if (result.contains(PromptBuilder.OUT_OF_CONTEXT_MARKER)) {
                                        showError("This file is outside judiciary context and was not saved.");
                                        return;
                                    }

                                    new RecentCaseRepository(InputActivity.this)
                                            .saveOrUpdate(selectedFileName, result);

                                    Intent i = new Intent(InputActivity.this,
                                            ResultActivity.class);
                                    i.putExtra("DATA", result);
                                    i.putExtra("FILE_NAME", selectedFileName);
                                    startActivity(i);
                                } else {
                                    showError("AI returned empty response");
                                }

                            } else {
                                String errMsg = "";
                                try {
                                    if (response.errorBody() != null)
                                        errMsg = response.errorBody().string();
                                } catch (Exception ignored) {}
                                showError("API Error " + response.code()
                                        + ": " + errMsg);
                            }
                        } catch (Exception e) {
                            showError("Error: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<GroqResponse> call, Throwable t) {
                        resetUI();
                        showError("Network error: " + t.getMessage());
                    }
                });
    }

    private void resetUI() {
        runOnUiThread(() -> {
            btnUpload.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            tvStatus.setVisibility(View.GONE);
            tvPageCount.setVisibility(View.GONE);
        });
    }

    private void showError(String msg) {
        runOnUiThread(() ->
                Toast.makeText(InputActivity.this, msg, Toast.LENGTH_LONG).show());
    }

    private String getDisplayName(Uri uri) {
        String name = null;
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    uri,
                    new String[]{OpenableColumns.DISPLAY_NAME},
                    null,
                    null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index >= 0) {
                    name = cursor.getString(index);
                }
            }
        } catch (Exception ignored) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (name == null || name.trim().isEmpty()) {
            String lastSegment = uri.getLastPathSegment();
            name = lastSegment == null ? "Analyzed document.pdf" : lastSegment;
        }
        return name;
    }
}
