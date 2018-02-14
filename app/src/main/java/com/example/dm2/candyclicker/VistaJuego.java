package com.example.dm2.candyclicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm2 on 09/02/2018.
 */

public class VistaJuego extends SurfaceView {
    private Bitmap bmp;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    int cuantosDroids=0;
    private long lastClick;

    List<Androide> listaDroid = new ArrayList<Androide>();



    public VistaJuego(Context context) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {


                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        listaDroid.add(new Androide(this,bmp));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        if(listaDroid.get(cuantosDroids).getY()>250){
            listaDroid.add(new Androide(this,bmp));
            cuantosDroids++;
        }
        else{
           for(Androide andr:listaDroid)
            andr.onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            synchronized (getHolder()) {
                for(int i = listaDroid.size() - 1; i >= 0; i--) {
                    Androide andr = listaDroid.get(i);
                    if (andr.isCollition(e.getX(), e.getY())) {
                        listaDroid.remove(andr);
                        cuantosDroids--;
                        break;
                    }
                }
            }
        }
        return true;
    }
}


