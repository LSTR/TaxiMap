package com.lester.mytaxi_challenge.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.lester.mytaxi_challenge.R;

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkStatus();
    }

    private void checkStatus() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToHome();
            }
        }, 1500);
    }

    private void goToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}