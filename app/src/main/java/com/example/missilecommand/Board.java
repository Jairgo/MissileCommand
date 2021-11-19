package com.example.missilecommand;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.SoundPool;
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


    // Arrays de bombas, misiles y ciudades
    private ArrayList<Bomb> bombs = new ArrayList<>();
    private ArrayList<Missile> missiles = new ArrayList<>();
    private ArrayList<City> cities = new ArrayList<>();

    private Battery battery;
    protected boolean gameOver = false;
    private int timerValue = -1, expId, expBombId, countMissiles = 0;
    private Thread hilo;

    // Posiciones en X y Y de las bombas y los misiles
    float misPosX, misPosY, bombPosX, bombPosY;
    private SoundPool explosionSp;

    public Board(Context context) {
        super(context);
        surfaceHolder = getHolder();
        explosionSp = new SoundPool.Builder().build();
        expId = explosionSp.load(context, R.raw.explosion, 1);
        expBombId = explosionSp.load(context, R.raw.explosion_bomb, 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN)
            bombs.add(new Bomb(new PointF(event.getX(), event.getY())));
            // Log.i("BoardClass", "Se creó una nueva bomba y se agrego al array list");
        return true;
    }

    @Override
    public void run() {
        while (drawing){
            update();
            draw();
            checkBomb();
            checkCities();
        }
    }

    public void setDimensions(float left, float top, float right, float bottom) {
        dimensions = new RectF(left, top, right, bottom);
    }

    public RectF getDimensions() {
        return dimensions;
    }

    /**
     * Funcion para añadir las ciudades al board
     * @param dimensions Parametro de las dimensiones de la pantalla, que ocupará para calcular donde colocar las ciudades
     */
    public void addCities(RectF dimensions){
        // Obtengo la mitad de la mitad de la pantalla
        float mitad = ((dimensions.left + dimensions.right) / 2) / 2;

        // Añado las ciudades al array de ciudades
        cities.add(new City(getDimensions(), this.dimensions.left + 100)); 
        cities.add(new City(getDimensions(), this.dimensions.left + mitad));
        cities.add(new City(getDimensions(), this.dimensions.right - mitad));
        cities.add(new City(getDimensions(), this.dimensions.right - 100));

        // Creo un objeto de la clase Battery
        battery = new Battery(getDimensions(), (this.dimensions.left + this.dimensions.right) / 2);
    }

    /**
     * Función que checa si alguna de las bombas existentes ya llegó a su tamaño maximo
     * Tambien checa si existe una colisión de una bomba con algun misil
     * Los dos For que puse, no tienen el mejor rendimiento, pues esta recorriendo ambos arrays constantemente
     * Se debería bajar la complejidad, buscando otra forma para encontrar la colission
     */
    private void checkBomb(){
        boolean eliminado = false;

        // Recorre todas las bombas que aun existen
        for (int i = 0; i < bombs.size(); i++){
            if(!missiles.isEmpty()){
                // Recorre todos las misiles que aun existen
                for(int j = 0; j < missiles.size(); j++){
                    misPosX = missiles.get(j).center.x;
                    misPosY = missiles.get(j).center.y;

                    bombPosX = bombs.get(i).center.x;
                    bombPosY = bombs.get(i).center.y;

                    float a = (float) Math.pow((misPosX - bombPosX), (float) 2);
                    float b = (float) Math.pow((misPosY - bombPosY), (float) 2);

                    // Distancia que existe entre la bomba y el misil
                    float distancia = (float) Math.sqrt((a+b));

                    if(distancia <= bombs.get(i).getRadius() ){
                        bombs.remove(i);
                        missiles.remove(j);
                        eliminado = true;
                        explosionSp.play(expBombId, 1, 1, 0, 0, 1);
                        countMissiles++;
                        break;
                    }
                }
            }

            // Solo entra aquí si la bomba que quiero revisar no ha sido eliminada ya
            if(!eliminado){
                if(bombs.get(i).getRadius() >= bombs.get(i).getMaxSize()){
                    bombs.remove(i);
                    // Log.i("BoardClass", "Se eliminó la bomba en la posición " + i);
                }
                eliminado = false;
            }

        }
    }

    public int getTimerValue(){
        return this.timerValue;
    }

    private void update() {
        Runnable runnable = () -> {
            Random randTimer = new Random();
            int val = randTimer.nextInt(8000 + 2000) + 2000;
            timerValue = val;
            try {
                Thread.sleep(val);
                // Log.i("Hilo", "" + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            missiles.add(new Missile(getDimensions(), cities));
            timerValue = -1;
        };

        // Hilo para ir creando los misiles cada cierto tiempo random
        if(getTimerValue() == -1){
            hilo = new Thread(runnable);
            hilo.start();
        }

        for(int i = 0; i < bombs.size(); i++)
            bombs.get(i).update(dimensions);

        for(int i = 0; i < missiles.size(); i++){
            // Log.i("UpdateMissile", "" + i);
            missiles.get(i).update(dimensions);

            // Aquí se evalua si el misil colisionó con una ciudad, si es así, le cambia el
            // valor de setAlive y reproduce el sonido
            int cityIndex = missiles.get(i).detectCollisionCity();
            if(cityIndex != -1){
                if(!cities.isEmpty() && cityIndex <= cities.size()){
                    if(cities.get(cityIndex).isAlive())
                        explosionSp.play(expId, 1, 1, 0, 0, 1);

                    cities.get(cityIndex).setAlive(false);
                }
                missiles.remove(i);
                break;
            }
        }
    }


    private void draw() {
        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();

            // Limpio la pantalla
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Dibujo la batería
            battery.draw(canvas);

            // Dibujo las bombas que esten disponibles, que aun no hayan sido eliminadas del arraylist
            for(int i = 0; i < bombs.size(); i++)
                bombs.get(i).draw(canvas);

            // Dibujo los misiles que esten disponibles, que aun no hayan sido eliminadas del arraylist
            for(int i = 0; i < missiles.size(); i++)
                missiles.get(i).draw(canvas);


            if(!getGameOver()){
                // Dibujo las ciudades que estan disponibles, que aun no hay sido eliminadas del array list
                for(int i = 0; i < cities.size(); i++){
                    if(cities.get(i).isAlive())
                        cities.get(i).draw(canvas);
                }
            }
            else{
                // Si ya se eliminaron todas las ciudad el juego termina.
                drawing = false;
                canvas.drawColor(Color.argb(255, 255, 255, 255));
                Intent myIntent = new Intent(getContext(), EndGameActivity.class);

                myIntent.putExtra("missiles", String.valueOf(countMissiles));
                getContext().startActivity(myIntent);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private boolean getGameOver(){
        return gameOver;
    }

    /**
     * Funcion que va checando cuantas ciudades ya no están vivas.
     * Si encuentra que todas las ciudades ya no están vivas, termina el juego
     */
    public void checkCities(){
        int count = 0;

        for(int i = 0; i < cities.size(); i++){
            if(!cities.get(i).isAlive())
                count++;
        }

        if(count > 3){
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
