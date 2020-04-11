package org.battleshipgame.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.battleshipgame.android.GameInstance;
import org.battleshipgame.android.R;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameInstance.create();
    }

    public void startNewGame(View view) {
        Intent intent = new Intent(this, GameModeActivity.class);
        startActivity(intent);
    }

    public void exit(View view) {
        finish();
    }
}
