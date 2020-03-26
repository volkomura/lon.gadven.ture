package com.template.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class GamePuzzle extends AppCompatActivity {

    private GridAdapter myGridAdapter;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_game);

        Button buttonBack = findViewById(R.id.button_back);
        constraintLayout = findViewById(R.id.game_puzzle_constraint_layout);

        GridView myGridView = findViewById(R.id.game_field);
        myGridView.setNumColumns(3);

        myGridAdapter = new GridAdapter(this, 3, 3);
        myGridView.setAdapter(myGridAdapter);

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                myGridAdapter.rotateCell(i);
                if(myGridAdapter.checkGameWin()){
                    popupWin();
                }
            }

        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamePuzzle.this,TheGameActivity.class);
                startActivity(intent);
            }
        });

    }

    public void popupWin() {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        final View popupView = layoutInflater.inflate(R.layout.popup_activity, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dismissButton = popupView.findViewById(R.id.button_popup);
        TextView textView = popupView.findViewById(R.id.textView_popup);
        textView.setText("Congratulations,claim your reward ");
        dismissButton.setText("Go");
        dismissButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(GamePuzzle.this, GameFlashlight.class);
                startActivity(intent);

            }
        });
        popupWindow.showAtLocation(constraintLayout, Gravity.CENTER, 10, 10);
    }

}
