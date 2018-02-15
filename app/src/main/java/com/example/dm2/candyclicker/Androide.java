package com.example.dm2.candyclicker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class Androide {

    private int x = 0;
    private int y= 0;
    private int ySpeed = 0;
    private int width;
    private int height;
    private VistaJuego vistaJuego;
    private Bitmap bmp;

    public Androide(VistaJuego vistaJuego, Bitmap bmp) {
        this.vistaJuego = vistaJuego;
        this.bmp = bmp;
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
        this.x = (int)(Math.random()*1000)+1;
    }

    private void update() {
        if (y<= vistaJuego.getHeight() - bmp.getHeight())
            y += 5;
        else {
            vistaJuego.setVidas(vistaJuego.getVidas()-1);
            vistaJuego.removeAndroide(this);
            vistaJuego.comprobarVidas();
        }


        //y=y+ySpeed;
    }



    public void onDraw(Canvas canvas) {
        update();
        canvas.drawBitmap(bmp, x, y, null);
    }

    public boolean isCollition(float x2, float y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }

    public int getY() {
        return y;
    }
}
