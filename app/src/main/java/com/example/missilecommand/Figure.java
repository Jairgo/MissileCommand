package com.example.missilecommand;

import android.graphics.Canvas;
import android.graphics.RectF;

public interface Figure {

    void update(RectF dimensions);
    void draw(Canvas canvas);
}
