package com.example.knowzydemo.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class OcrUtils {

    private static final String TAG = "OcrUtils";

    public interface OnTextReady {
        void onComplete(String text);
    }

    // New: progress callback
    public interface OnPageProgress {
        void onPageDone(int currentPage, int totalPages);
    }

    // ── Original method (kept for compatibility) ─────────────────────
    public static void runOCR(Bitmap[] images, OnTextReady listener) {
        runOCRWithProgress(images, null, listener);
    }

    // ── New method with progress ──────────────────────────────────────
    public static void runOCRWithProgress(Bitmap[] images,
                                          OnPageProgress progressCallback,
                                          OnTextReady listener) {
        if (images == null || images.length == 0) {
            listener.onComplete("");
            return;
        }

        StringBuilder finalText = new StringBuilder();
        TextRecognizer recognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        Log.d(TAG, "Starting OCR on " + images.length + " pages");
        processPage(images, 0, finalText, recognizer, progressCallback, listener);
    }

    private static void processPage(Bitmap[] images,
                                    int index,
                                    StringBuilder finalText,
                                    TextRecognizer recognizer,
                                    OnPageProgress progressCallback,
                                    OnTextReady listener) {

        if (index >= images.length) {
            recognizer.close();
            String result = finalText.toString().trim();
            Log.d(TAG, "OCR done. Total chars: " + result.length());
            listener.onComplete(result);
            return;
        }

        Bitmap bitmap = images[index];

        if (bitmap == null || bitmap.isRecycled()) {
            if (progressCallback != null)
                progressCallback.onPageDone(index + 1, images.length);
            processPage(images, index + 1, finalText, recognizer,
                    progressCallback, listener);
            return;
        }

        InputImage image = InputImage.fromBitmap(bitmap, 0);

        recognizer.process(image)
                .addOnSuccessListener(result -> {
                    String pageText = result.getText();
                    if (pageText != null && !pageText.isEmpty()) {
                        finalText.append(pageText).append("\n");
                        Log.d(TAG, "Page " + index + " extracted "
                                + pageText.length() + " chars");
                    }
                    bitmap.recycle();

                    //  Report progress
                    if (progressCallback != null)
                        progressCallback.onPageDone(index + 1, images.length);

                    processPage(images, index + 1, finalText, recognizer,
                            progressCallback, listener);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "OCR failed page " + index + ": " + e.getMessage());
                    bitmap.recycle();

                    if (progressCallback != null)
                        progressCallback.onPageDone(index + 1, images.length);

                    processPage(images, index + 1, finalText, recognizer,
                            progressCallback, listener);
                });
    }
}