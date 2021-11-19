package com.example.missilecommand;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;

public class BoardOld extends SurfaceView implements Runnable{

    private RectF dimensions;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Thread thread = null;
    private volatile boolean drawing;

    private ArrayList<Bomb> bombs = new ArrayList<Bomb>();
    private ArrayList<Missile> missiles = new ArrayList<Missile>();
    private ArrayList<City> cities = new ArrayList<City>();

    public BoardOld(Context context) {
        super(context);
        surfaceHolder = getHolder();
    }

    public RectF getDimensions() {
        return dimensions;
    }

    public void setDimensions(float left, float top, float right, float bottom) {
        dimensions = new RectF(left, top, right, bottom);
    }

    public void addCities(RectF dimensions){
        float mitad = (dimensions.left + dimensions.right) / 2;
        mitad = mitad / 2;

        // Dibujo las ciudades en la pantalla
        cities.add(new City(getDimensions(), this.dimensions.left + 100));
        cities.add(new City(getDimensions(), this.dimensions.left + mitad));
        cities.add(new City(getDimensions(), this.dimensions.right - mitad));
        cities.add(new City(getDimensions(), this.dimensions.right - 100));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN){
            bombs.add(new Bomb(new PointF(event.getX(), event.getY())));
            // missiles.add(new Missile(getDimensions(),posibles));
        }
        // return super.onTouchEvent(event);
        return true;
    }

    @Override
    public void run() {
        while (drawing){

            update();
            draw();
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

    private void update() {
        /*for(int i = 0; i < missiles.size(); i++){
            for (int j = 0; j < cities.size(); j++){
                if(missiles.get(i).detectCollision(cities.get(j).getObjective()))
                    cities.remove(j);
            }
        }*/



        for (Figure figure : new ArrayList<>(bombs))
            figure.update(dimensions);
        /*for (Figure figM : new ArrayList<>(missiles)){
            figM.update(dimensions);
        }*/
        for (int i = 0; i < missiles.size(); i++){
            missiles.get(i).update(dimensions);
            /*if(missiles.get(i).detectCollisionCity()){

                canvas = surfaceHolder.lockCanvas();
                // cities.get(0).unDraw(canvas);
                cities.remove(0);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }*/

        }

        // for (Figure figC : new ArrayList<>(cities))
            // figC.update(dimensions);
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();


            for (Figure figure : new ArrayList<>(bombs)) {
                figure.draw(canvas);
            }

            for (Figure figM : new ArrayList<>(missiles)) {
                figM.draw(canvas);
            }

            for (Figure figC : new ArrayList<>(cities)) {
                figC.draw(canvas);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

}
