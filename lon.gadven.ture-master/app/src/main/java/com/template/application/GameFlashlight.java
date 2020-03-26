package com.template.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameFlashlight extends AppCompatActivity {

    private DarkRoom mGameView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGameView = new DarkRoom(this);
        mGameView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(mGameView);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mGameView.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGameView.resume();

    }
}

