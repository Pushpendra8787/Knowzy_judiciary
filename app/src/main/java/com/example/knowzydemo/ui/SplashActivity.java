package com.example.knowzydemo.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowzydemo.R;

public class SplashActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logoFull);
        View blurOverlay = findViewById(R.id.blurOverlay);

        ValueAnimator background = ValueAnimator.ofObject(
                new ArgbEvaluator(), 0xFF0A2A43, 0xFF0F6F73, 0xFF0A2A43);
        background.setDuration(2500);
        background.addUpdateListener(animation ->
                findViewById(android.R.id.content)
                        .setBackgroundColor((int) animation.getAnimatedValue()));
        background.start();

        ValueAnimator zoom = ValueAnimator.ofFloat(1.2f, 0.8f);
        zoom.setDuration(1600);
        zoom.setInterpolator(new DecelerateInterpolator(1.4f));
        zoom.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            logo.setScaleX(value);
            logo.setScaleY(value);
            blurOverlay.setAlpha(((value - 0.8f) / 0.4f) * 0.4f);
        });
        zoom.start();

        handler.postDelayed(() -> findViewById(android.R.id.content)
                .animate()
                .alpha(0f)
                .setDuration(400)
                .withEndAction(() -> {
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                })
                .start(), 2600);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
