package lon.gadven.ture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * DON'T RENAME THIS ACTIVITY
 * YOU CAN CHANGE BODY IN IT
 */
public class TheGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);
        Button button = findViewById(R.id.button_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentStart = new Intent(TheGameActivity.this, GamePuzzle.class);
                startActivity(intentStart);
            }
        });
    }
}
