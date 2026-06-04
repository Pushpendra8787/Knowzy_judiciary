package com.example.knowzydemo.data;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecentCaseRepository {

    private static final String PREFS_NAME = "recent_case_history";
    private static final String KEY_CASES = "cases";
    private static final int MAX_STORED_CASES = 100;

    private final SharedPreferences preferences;

    public RecentCaseRepository(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public synchronized List<RecentCase> getAll() {
        List<RecentCase> cases = new ArrayList<>();
        String json = preferences.getString(KEY_CASES, "[]");

        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                cases.add(new RecentCase(
                        item.optString("fileName", "Unknown file"),
                        item.optString("analysis", ""),
                        item.optLong("analyzedAt", 0L)));
            }
        } catch (Exception ignored) {
        }
        return cases;
    }

    public synchronized void saveOrUpdate(String fileName, String analysis) {
        if (fileName == null || fileName.trim().isEmpty()
                || analysis == null || analysis.trim().isEmpty()) {
            return;
        }

        String normalizedName = normalize(fileName);
        List<RecentCase> current = getAll();
        JSONArray updated = new JSONArray();

        putCase(updated, new RecentCase(fileName.trim(), analysis.trim(), System.currentTimeMillis()));

        for (RecentCase recentCase : current) {
            if (normalize(recentCase.getFileName()).equals(normalizedName)) {
                continue;
            }
            if (updated.length() >= MAX_STORED_CASES) {
                break;
            }
            putCase(updated, recentCase);
        }

        preferences.edit().putString(KEY_CASES, updated.toString()).apply();
    }

    private void putCase(JSONArray array, RecentCase recentCase) {
        try {
            JSONObject item = new JSONObject();
            item.put("fileName", recentCase.getFileName());
            item.put("analysis", recentCase.getAnalysis());
            item.put("analyzedAt", recentCase.getAnalyzedAt());
            array.put(item);
        } catch (Exception ignored) {
        }
    }

    private String normalize(String fileName) {
        return fileName.trim().toLowerCase(Locale.ROOT);
    }
}
