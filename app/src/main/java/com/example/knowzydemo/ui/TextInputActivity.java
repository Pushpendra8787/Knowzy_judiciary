package com.example.knowzydemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowzydemo.BuildConfig;
import com.example.knowzydemo.R;
import com.example.knowzydemo.ai.GroqApiService;
import com.example.knowzydemo.ai.PromptBuilder;
import com.example.knowzydemo.ai.RetrofitClient;
import com.example.knowzydemo.ai.models.GroqRequest;
import com.example.knowzydemo.ai.models.GroqResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextInputActivity extends AppCompatActivity {

    public static final String OUT_OF_CONTEXT_MESSAGE =
            "sorry query out of context try with judicary content";

    private EditText etQuestion;
    private Button btnAnalyze;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_input);

        etQuestion = findViewById(R.id.etQuestion);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        btnAnalyze.setOnClickListener(v -> analyzeQuestion());
    }

    private void analyzeQuestion() {
        String question = etQuestion.getText().toString().trim();
        if (question.length() < 3) {
            etQuestion.setError("Please enter a question");
            return;
        }

        hideKeyboard();

        if (isObviousSmallTalk(question)) {
            openAnswer(OUT_OF_CONTEXT_MESSAGE);
            return;
        }

        String apiKey = BuildConfig.GROQ_API_KEY;
        if (apiKey == null
                || apiKey.trim().isEmpty()
                || apiKey.contains("PASTE_YOUR")) {
            Toast.makeText(
                    this,
                    "Missing Groq API key. Set GROQ_API_KEY in local.properties.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        setLoading(true);

        List<GroqRequest.Message> messages = new ArrayList<>();
        messages.add(new GroqRequest.Message(
                "user",
                PromptBuilder.buildTextQuestionPrompt(question)));

        GroqRequest request = new GroqRequest("llama-3.1-8b-instant", messages);
        GroqApiService api = RetrofitClient.getClient().create(GroqApiService.class);

        api.getAIResponse("Bearer " + apiKey, request).enqueue(new Callback<GroqResponse>() {
            @Override
            public void onResponse(
                    Call<GroqResponse> call,
                    Response<GroqResponse> response) {
                setLoading(false);

                if (!response.isSuccessful()
                        || response.body() == null
                        || response.body().getChoices() == null
                        || response.body().getChoices().isEmpty()) {
                    Toast.makeText(
                            TextInputActivity.this,
                            "Could not analyze the question. Please try again.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                String answer = response.body()
                        .getChoices()
                        .get(0)
                        .getMessage()
                        .getContent();

                if (answer == null || answer.trim().isEmpty()) {
                    Toast.makeText(
                            TextInputActivity.this,
                            "AI returned an empty answer.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (answer.contains(PromptBuilder.OUT_OF_CONTEXT_MARKER)) {
                    answer = OUT_OF_CONTEXT_MESSAGE;
                }
                openAnswer(answer.trim());
            }

            @Override
            public void onFailure(Call<GroqResponse> call, Throwable throwable) {
                setLoading(false);
                Toast.makeText(
                        TextInputActivity.this,
                        "Network error: " + throwable.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isObviousSmallTalk(String question) {
        String normalized = question.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z ]", "")
                .trim();

        return normalized.equals("hi")
                || normalized.equals("hello")
                || normalized.equals("hey")
                || normalized.equals("how are you")
                || normalized.equals("hi how are you")
                || normalized.equals("hello how are you");
    }

    private void openAnswer(String answer) {
        Intent intent = new Intent(this, AiAnswerActivity.class);
        intent.putExtra("ANSWER", answer);
        startActivity(intent);
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnAnalyze.setEnabled(!loading);
        etQuestion.setEnabled(!loading);
    }

    private void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (manager != null && focusedView != null) {
            manager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }
}
