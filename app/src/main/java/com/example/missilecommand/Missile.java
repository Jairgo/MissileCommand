package com.example.missilecommand;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Missile implements Figure {

    private int div;
    private float radius;
    public PointF center;
    private Paint paint;
    private PointF velocity;

    public PointF objective;
    private float restax, restay, saltox, saltoy;
    private float xc1, xc2, xc3, xc4;

    private int origenX, origenY;
    private int rand;
    private Random random;


    public Missile(RectF dimensiones, ArrayList<City> cities) {
        Random rand = new Random();
        origenY = (int) dimensiones.top;
        origenX = (int) (rand.nextInt((int) (dimensiones.right + dimensiones.left)) + dimensiones.left);
        this.center = new PointF(origenX,dimensiones.top);

        setPosXCities(dimensiones);
        // Log.i("Lo que recibo", "" + posibles);
        setObjective(dimensiones, cities);

        restax = center.x - objective.x;
        restay = center.y - objective.y;

        div = rand.nextInt(600 + 10) + 10;

        saltox = restax / div;
        saltoy = restay / div;

        double magnitude = rand.nextInt(10) + 1;
        double angle = Math.toRadians(rand.nextInt(360));

        this.radius = 20;
        this.velocity = new PointF(1,1);

        paint = new Paint();
        paint.setARGB(255, 255, 233, 0);
        paint.setStrokeWidth(10f);
    }

    private void setPosXCities(RectF dimensiones){
        // Obtiene o setea la posicion en X de las ciudades
        this.xc1 = dimensiones.left + 100;
        this.xc2 = dimensiones.left + (((dimensiones.left + dimensiones.right) / 2) / 2);
        this.xc3 = dimensiones.right - (((dimensiones.left + dimensiones.right) / 2) / 2);
        this.xc4 = dimensiones.right - 100;
    }

    private void setObjective(RectF dimensiones, ArrayList<City> cities) {

        ArrayList<Integer> posibles = new ArrayList<Integer>();

        for(int i = 0; i < cities.size(); i++){
            if(cities.get(i).isAlive()){
                posibles.add(i);
            }

        }

        if(posibles.size() > 0){
            random = new Random();
            rand = random.nextInt(posibles.size());
            rand = posibles.get(rand);

            // cities.get(rand);
            /*rand = random.nextInt(cities.size());
            rand = cities.get(rand);
            */
            // Log.i("Random", ""+rand);
            switch(rand) {
                case 0:
                    this.objective = cities.get(0).getPositionCity();
                    // this.objective = new PointF(xc1, dimensiones.bottom);
                    break;
                case 1:
                    this.objective = cities.get(1).getPositionCity();
                    // this.objective = new PointF(xc2, dimensiones.bottom);
                    break;
                case 2:
                    this.objective = cities.get(2).getPositionCity();
                    // this.objective = new PointF(xc3, dimensiones.bottom);
                    break;
                case 3:
                    this.objective = cities.get(3).getPositionCity();
                    // this.objective = new PointF(xc4, dimensiones.bottom);
                    break;
            }
        }else{
            this.objective = new PointF(xc2, dimensiones.bottom);
        }



        posibles.clear();
    }

    public int detectCollisionCity(){
        if(this.center.y >= objective.y)
            return this.rand;
        return -1;
    }

    @Override
    public void update(RectF dimensions) {

        center.x = center.x - saltox;
        center.y = center.y - saltoy;
        // Checks if reaches left or right border
        /*if ((center.x - radius) < dimensions.left || (center.x + radius) > dimensions.right) {
            velocity.x *= -1;
        }*/
        // Checks if reaches top or bottom border
        /*if ((center.y - radius) < dimensions.top || (center.y + radius) > dimensions.bottom) {
            velocity.y *= -1;
        }*/
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine((float) origenX, (float) origenY, center.x, center.y, paint);
        // canvas.drawCircle(center.x, center.y, radius, paint);
    }
}
