package com.example.missilecommand;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Bomb implements Figure{
    private final int maxSize = 80;
    private float radius;
    private PointF center;
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


        // center.x += velocity.x;
        // center.y += velocity.y;

        // if ((center.x - radius) < dimensions.left || (center.x + radius) > dimensions.right)
        // velocity.x *= -1;

        // if ((center.y - radius) < dimensions.top || (center.y + radius) > dimensions.bottom)
        // velocity.y *= -1;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(center.x, center.y, radius, paint);
    }

    public float getRadius() {
        return radius;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
