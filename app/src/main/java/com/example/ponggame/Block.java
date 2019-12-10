package com.example.ponggame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Block {
    private RectF rect;
    private boolean isVisible, geofStart;
    private int row, col, width, height, padding, geofTicker;
    private Paint paint;
    private String type;

    //Constructor
    Block(int row, int col, int width, int height, Context context) {
        isVisible = true;
        this.padding = 2;
        this.row = row;
        this.col = col;
        this.width = width;
        this.height = height;
        paint = new Paint();
        geofTicker = -5;

        if (Math.random() * 100 <= 10) {
            type = "large";
            paint.setColor(Color.argb(255, 253, 253, 150));

        } else if (Math.random() * 500 <= 10) {
            type = "health";
            paint.setColor(Color.argb(255, 153, 0, 0));
        } else if (Math.random() * 500 <= 10) {
            type = "huge";
            paint.setColor(Color.argb(255, 0, 0, 0));
        } else {
            type = "nothing";
            paint.setColor(Color.argb(255,  249, 129, 0));
        }

        rect = new RectF(col * width + padding, row * height +padding,
                col * width + width - padding,
                row * height + height - padding);
    }

    public void render(Canvas canvas) {
        if (isVisible) {
            canvas.drawRect(rect, paint);
        } else if (geofStart) {
            canvas.drawBitmap(PongGame.bitmap, null, rect, null);
            rect.top = rect.top + 2*geofTicker;
            rect.bottom = rect.bottom + 2*geofTicker;
            if (geofTicker > 60 ) {
                geofStart = false;
            }
            geofTicker++;
        }
    }

    public void increaseRow() {
        if (isVisible) {
            row++;
            rect = new RectF(col * width + padding, row * height + padding,
                    col * width + width - padding,
                    row * height + height - padding);
        }
    }

    //get the current brick
    public RectF getRect(){
        return rect;
    }
    public void setInvisible(){
        isVisible = false;
        geofStart = true;
    }

    public String getType() {
        return type;
    }

    public boolean getVisibility(){
        return isVisible;
    }
}
