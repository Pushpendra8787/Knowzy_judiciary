package com.example.knowzydemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowzydemo.ui.InputActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔥 Direct InputActivity open karo
        startActivity(new Intent(MainActivity.this, InputActivity.class));

        finish(); // 🔥 MainActivity close
    }
}