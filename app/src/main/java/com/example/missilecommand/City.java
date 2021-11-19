package com.example.missilecommand;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

public class City implements Figure {
    private final float size = 150;
    private float left;
    private float top;
    private float right;
    private float  bottom;
    public PointF positionCity;
    private Paint paint;

    private boolean isAlive;

    public City(RectF dimensiones, float x){
        this.positionCity = new PointF(x,dimensiones.bottom);

        paint = new Paint();
        paint.setARGB(128, 0,255,0);
        
        left = positionCity.x - size / 2f;
        top = positionCity.y - size / 2f;
        right = positionCity.x  + size / 2f;
        bottom = positionCity.y + size / 2f;

        isAlive = true;

    }
    public PointF getPositionCity(){
        return this.positionCity;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void update(RectF dimensions) { }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(left, top, right, bottom, paint);
    }
}
