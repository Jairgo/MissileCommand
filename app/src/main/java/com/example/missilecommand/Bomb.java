package com.example.missilecommand;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import androidx.appcompat.app.AppCompatActivity;

public class Bomb extends AppCompatActivity implements Figure{
    private final int maxSize = 80;
    private float radius;
    public PointF center;
    private Paint paint;


    public Bomb(PointF center){
        this.center = new PointF(center.x, center.y);
        this.radius = 1;
        paint = new Paint();
        paint.setARGB(255, 255, 0, 0);
    }

    @Override
    public void update(RectF dimensions) {
        if(this.radius <= maxSize)
            this.radius += 1;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(center.x, center.y, radius, paint);
    }

    public float getRadius() {
        return this.radius;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
