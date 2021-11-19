package com.example.missilecommand;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Board board;
    private Toast msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Elimina la barra superior de la app
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        // Pone la orientación del celular en ladscape y FullScreen por default
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        // Cambia de color el fondo de la actividad
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);

        newGame();
        setContentView(board);
    }

    private void newGame(){
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        board = new Board(this);
        board.setDimensions(0f,0f,displayMetrics.widthPixels, displayMetrics.heightPixels);
        board.addCities(board.getDimensions());
    }

    @Override
    protected void onResume() {
        super.onResume();
        board.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        board.pause();
    }
}