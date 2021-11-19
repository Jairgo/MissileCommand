package com.example.missilecommand;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class Board extends SurfaceView implements Runnable{

    private SurfaceHolder surfaceHolder;
    private volatile boolean drawing = true;
    private Thread thread = null;
    private RectF dimensions;
    private Canvas canvas;
    private Intent intent;


    private ArrayList<Bomb> bombs = new ArrayList<Bomb>();
    private ArrayList<Missile> missiles = new ArrayList<Missile>();

    private ArrayList<City> cities = new ArrayList<City>();
    public ArrayList<Integer> posiblesCities = new ArrayList<Integer>();

    private Battery battery;
    protected boolean gameOver = false;
    private int timerValue = -1, timerCount = 0;
    Thread hilo;


    public Board(Context context) {
        super(context);
        surfaceHolder = getHolder();
    }

    public void setDimensions(float left, float top, float right, float bottom) {
        dimensions = new RectF(left, top, right, bottom);
    }

    public RectF getDimensions() {
        return dimensions;
    }

    public void addCities(RectF dimensions){
        float mitad = (this.dimensions.left + this.dimensions.right) / 2;
        mitad = mitad / 2;

        // Dibujo las ciudades en la pantalla
        cities.add(new City(getDimensions(), this.dimensions.left + 100)); 
        cities.add(new City(getDimensions(), this.dimensions.left + mitad));
        cities.add(new City(getDimensions(), this.dimensions.right - mitad));
        cities.add(new City(getDimensions(), this.dimensions.right - 100));

        /*posiblesCities.add(0);
        posiblesCities.add(1);
        posiblesCities.add(2);
        posiblesCities.add(3);*/

        battery = new Battery(getDimensions(), (this.dimensions.left + this.dimensions.right) / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN){
            bombs.add(new Bomb(new PointF(event.getX(), event.getY())));
            // missiles.add(new Missile(getDimensions(), cities));
            // Log.i("Lo que Mando", ""+posiblesCities);
            // Log.i("BoardClass", "Se creó una nueva bomba y se agrego al array list");
            // missiles.add(new Missile(getDimensions()));
        }
        // return super.onTouchEvent(event);
        return true;
    }

    @Override
    public void run() {
        while (drawing){
            checkBomb();
            update();
            draw();
            checkCities();
        }
    }




    private void checkBomb(){
        for (int i = 0; i < bombs.size(); i++){
            if(bombs.get(i).getRadius() >= bombs.get(i).getMaxSize()){
                // Log.i("BoardClass", "Se eliminó la bomba en la posición " + i);
                // cities.remove(i);
                bombs.remove(i);
            }
        }
    }

    public int getTimerValue(){
        return this.timerValue;
    }

    private void update() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Random randTimer = new Random();
                int val = randTimer.nextInt(8000 + 2000) + 2000;
                timerValue = val;
                try {
                    Thread.sleep(val);
                    // Log.i("Hilo", "" + Thread.currentThread().getName());
                    // Log.i("VAL", "" + val);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                missiles.add(new Missile(getDimensions(), cities));
                timerValue = -1;
            }
        };

        if(timerValue == -1){
            hilo = new Thread(runnable);
            hilo.start();
        }


        for(int i = 0; i < bombs.size(); i++){
            // Log.i("UpdateMethod", ""+i);
            bombs.get(i).update(dimensions);
        }

        for(int i = 0; i < missiles.size(); i++){
            // Log.i("UpdateMissile", "" + i);
            missiles.get(i).update(dimensions);

            // Log.i("Llega al if", ""+missiles.get(i).detectCollisionCity());
            int cityIndex = missiles.get(i).detectCollisionCity();
            if(cityIndex != -1){
                // Log.i("Se borra la posición", ""+missiles.get(i).detectCollisionCity());
                if(!cities.isEmpty() && cityIndex <= cities.size()){
                    cities.get(cityIndex).setAlive(false);
                    // Log.i("CityIndex eliminado", ""+cityIndex);
                }

                    // cities.remove(missiles.get(i).detectCollisionCity());
                // posiblesCities.remove(missiles.get(i).detectCollisionCity());
                missiles.remove(i);
            }

        }

        /*for (Figure figure : new ArrayList<>(bombs))
            figure.update(dimensions);*/
    }


    private void draw() {
        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();

            // Limpio la pantalla
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Dibujo la batería
            battery.draw(canvas);

            // Dibujo las bombas que esten disponibles, que aun no hayan sido eliminadas del arraylist
            for(int i = 0; i < bombs.size(); i++){
                bombs.get(i).draw(canvas);
            }

            // Dibujo los misiles que esten disponibles, que aun no hayan sido eliminadas del arraylist
            for(int i = 0; i < missiles.size(); i++){
                missiles.get(i).draw(canvas);
            }

            if(!getGameOver()){
                // Dibujo las ciudades que estan disponibles, que aun no hay sido eliminadas del array list
                for(int i = 0; i < cities.size(); i++){
                    if(cities.get(i).isAlive())
                        cities.get(i).draw(canvas);
                }
            }
            else{
                drawing = false;
                canvas.drawColor(Color.argb(255, 255, 255, 255));
                // intent = new Intent(getContext(), MainActivity.class);

            }
            /* for (Figure figure : new ArrayList<>(bombs)) {
                figure.draw(canvas);
            }*/
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private boolean getGameOver(){
        return gameOver;
    }

    public void checkCities(){
        boolean allClear = false;
        int count = 0;

        for(int i = 0; i < cities.size(); i++){
            if(!cities.get(i).isAlive())
                count++;
        }


        if(count > 3){ // cities.size() <= 0
            gameOver = true;
            bombs.clear();
            cities.clear();
            missiles.clear();
        }
    }

    public void resume() {
        drawing = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        drawing = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
