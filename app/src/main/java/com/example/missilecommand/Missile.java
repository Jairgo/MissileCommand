package com.example.missilecommand;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;

public class Missile implements Figure {

    private int origenX, origenY, rand, div;
    private float restax, restay, saltox, saltoy;
    public PointF center, objective;
    private Paint paint;
    private Random random;


    public Missile(RectF dimensiones, ArrayList<City> cities) {
        Random rand = new Random();
        div = rand.nextInt(600 + 10) + 10;

        origenY = (int) dimensiones.top;
        origenX = (int) (rand.nextInt((int) (dimensiones.right + dimensiones.left)) + dimensiones.left);
        center = new PointF(origenX, dimensiones.top);

        setObjective(dimensiones, cities);

        restax = center.x - objective.x;
        restay = center.y - objective.y;

        saltox = restax / div;
        saltoy = restay / div;

        paint = new Paint();
        paint.setARGB(255, 255, 233, 0);
        paint.setStrokeWidth(10f);
    }

    private void setObjective(RectF dimensiones, ArrayList<City> cities) {

        ArrayList<Integer> posibles = new ArrayList<>();

        for(int i = 0; i < cities.size(); i++){
            if(cities.get(i).isAlive()){
                posibles.add(i);
            }
        }

        if(posibles.size() > 0){
            random = new Random();
            rand = random.nextInt(posibles.size());
            rand = posibles.get(rand);
            // Log.i("Random", ""+rand);
            switch(rand) {
                case 0:
                    objective = cities.get(0).getPositionCity();
                    break;
                case 1:
                    objective = cities.get(1).getPositionCity();
                    break;
                case 2:
                    objective = cities.get(2).getPositionCity();
                    break;
                case 3:
                    objective = cities.get(3).getPositionCity();
                    break;
            }
        }else{
            objective = new PointF(0, dimensiones.bottom);
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
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine((float) origenX, (float) origenY, center.x, center.y, paint);
    }
}
