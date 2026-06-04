package com.example.knowzydemo.data;

public class RecentCase {

    private final String fileName;
    private final String analysis;
    private final long analyzedAt;

    public RecentCase(String fileName, String analysis, long analyzedAt) {
        this.fileName = fileName;
        this.analysis = analysis;
        this.analyzedAt = analyzedAt;
    }

    public String getFileName() {
        return fileName;
    }

    public String getAnalysis() {
        return analysis;
    }

    public long getAnalyzedAt() {
        return analyzedAt;
    }
}
