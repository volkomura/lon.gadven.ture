package com.template.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * DON'T MODIFY AND RENAME THIS ACTIVITY.
 * START THE GAME IN TheGameActivity
 */
public class MainActivity extends AppCompatActivity
{
    Loader loader = new Loader(this);

    public static final short TRY_TIME = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loader.load();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (loader.isFinish())
                {
                    Intent intent = new Intent(MainActivity.this, TheGameActivity.class);
                    startActivity(intent);
                }
                else
                {
                    new Handler().postDelayed(this, TRY_TIME);
                }
            }
        },  TRY_TIME);
    }
}
