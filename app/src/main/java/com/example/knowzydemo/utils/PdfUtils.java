package com.example.knowzydemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class PdfUtils {

    private static final String TAG = "PdfUtils";

    public interface OnPdfConverted {
        void onComplete(Bitmap[] bitmaps);
    }

    //  PDF → Bitmap images for OCR
    public static void convertToImages(Context context, Uri uri, OnPdfConverted listener) {

        new Thread(() -> {

            ParcelFileDescriptor fd = null;
            PdfRenderer renderer = null;

            try {
                fd = context.getContentResolver().openFileDescriptor(uri, "r");

                if (fd == null) {
                    Log.e(TAG, "FileDescriptor is null");
                    listener.onComplete(new Bitmap[0]);
                    return;
                }

                renderer = new PdfRenderer(fd);

                int pageCount = renderer.getPageCount();
                Log.d(TAG, "PDF has " + pageCount + " pages");

                if (pageCount == 0) {
                    listener.onComplete(new Bitmap[0]);
                    return;
                }

                Bitmap[] pages = new Bitmap[pageCount];

                for (int i = 0; i < pageCount; i++) {

                    PdfRenderer.Page page = renderer.openPage(i);

                    // 2x scale for better OCR accuracy
                    int width = page.getWidth() * 2;
                    int height = page.getHeight() * 2;

                    Bitmap bitmap = Bitmap.createBitmap(
                            width,
                            height,
                            Bitmap.Config.ARGB_8888
                    );

                    bitmap.eraseColor(Color.WHITE);

                    page.render(
                            bitmap,
                            null,
                            null,
                            PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                    );

                    pages[i] = bitmap;

                    page.close();

                    Log.d(TAG, "Rendered page " + i + " (" + width + "x" + height + ")");
                }

                listener.onComplete(pages);

            } catch (Exception e) {
                Log.e(TAG, "PDF conversion failed: " + e.getMessage());
                e.printStackTrace();
                listener.onComplete(new Bitmap[0]);

            } finally {
                //  Always close renderer and fd to avoid memory leaks
                try {
                    if (renderer != null) renderer.close();
                    if (fd != null) fd.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error closing PDF resources: " + e.getMessage());
                }
            }

        }).start(); // Run on background thread (PdfRenderer is blocking)
    }
}