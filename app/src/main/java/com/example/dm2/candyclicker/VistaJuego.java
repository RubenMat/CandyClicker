package com.example.dm2.candyclicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
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
    int cuantosDroids=0, restantes =20,vidas=3;
    private long lastClick;

    List<Androide> listaDroid = new ArrayList<Androide>();
    List<Androide> listaAndroidesABorrar = new ArrayList<Androide>();



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
        AsyncCallWS spawner = new AsyncCallWS(this);
        spawner.execute();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        Paint color = new Paint();
        color.setColor(Color.WHITE);
        color.setTextSize(50);
        canvas.drawText("Restantes: " + restantes, 50, 200, color);
        canvas.drawText("Vidas: " + vidas,400,200,color);
        for(Androide andr:listaDroid)
            andr.onDraw(canvas);
        for(Androide andr:listaAndroidesABorrar){
            listaDroid.remove(andr);
        }
        listaAndroidesABorrar.clear();
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
                        restantes--;
                        comprobarVidas();
                        break;
                    }
                }
            }
        }
        return true;
    }

    public void removeAndroide(Androide androide) {
        listaAndroidesABorrar.add(androide);
    }

    public void comprobarVidas() {
        if(vidas <= 0 || restantes <=0){
            ((MainActivity)getContext()).terminar();
        }
    }

    public class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        private VistaJuego juego;
        private boolean continuar;

        AsyncCallWS(VistaJuego juego){
            this.juego = juego;
            this.continuar = true;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            while(continuar){
                listaDroid.add(new Androide(juego,bmp));
                cuantosDroids++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

        }


    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public int getVidas() {
        return vidas;
    }

    public List<Androide> getListaDroid() {
        return listaDroid;
    }

    public void setListaDroid(List<Androide> listaDroid) {
        this.listaDroid = listaDroid;
    }
}


