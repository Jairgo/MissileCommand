package com.example.missilecommand;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class Battery implements Figure {

    private float left;
    private float top;
    private float right;
    private float  bottom;
    private PointF objective;
    private Paint paint;
    private Path path;

    public Battery(RectF dimensiones, float x){
        this.objective = new PointF(x, dimensiones.bottom);

        paint = new Paint();
        paint.setARGB(255,150, 152, 154);

        float radius = 150;
        float radians = (float)Math.toRadians(30);

        PointF vertexA = new PointF(
                (float)(Math.cos(radians) * radius) + objective.x,
                (float)(Math.sin(radians) * radius) + objective.y);
        radians = (float)Math.toRadians(150);
        PointF vertexB = new PointF(
                (float)(Math.cos(radians) * radius) + objective.x,
                (float)(Math.sin(radians) * radius) + objective.y);
        radians = (float)Math.toRadians(270);
        PointF vertexC = new PointF(
                (float)(Math.cos(radians) * radius) + objective.x,
                (float)(Math.sin(radians) * radius) + objective.y);

        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(vertexA.x, vertexA.y);
        path.lineTo(vertexB.x, vertexB.y);
        path.lineTo(vertexC.x, vertexC.y);
        path.close();
    }

    @Override
    public void update(RectF dimensions) {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }
}
