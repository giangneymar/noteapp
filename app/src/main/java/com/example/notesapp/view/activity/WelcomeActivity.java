package com.example.notesapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.notesapp.R;
import com.example.notesapp.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {
    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(WelcomeActivity.this, R.layout.activity_welcome);
        setAnim();
        welcomeDelay();
    }

    private void setAnim() {
        binding.lottie.animate().setDuration(2000);
    }

    private void welcomeDelay() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(WelcomeActivity.this, NoteActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }, 3000);
    }
}