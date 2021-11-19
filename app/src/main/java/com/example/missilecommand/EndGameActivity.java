package com.example.missilecommand;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EndGameActivity extends AppCompatActivity {

    private Intent intent;
    private String missiles;
    private Button btnMissiles;
    private TextView missilesText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        setContentView(R.layout.activity_finish);
        intent = getIntent();

        missilesText = (TextView) findViewById(R.id.missilesText);
        btnMissiles = (Button) findViewById(R.id.btnNewGame);

        if(intent != null){
            missiles = intent.getStringExtra("missiles");
            if(missiles != null){
                missilesText.setText(missiles);
            }
        }
        btnMissiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent endIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(endIntent);
            }
        });
    }

}
