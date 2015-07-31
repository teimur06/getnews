package com.example.getnews;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;


public class GifImageView extends SurfaceView implements SurfaceHolder.Callback {

	float x, y;
	final static String LOG = "GifImageView";
    Movie movie;
    InputStream inputStream;
    private DrawThread drawThread;
    private long mMovieStart;
    int id;

    
    public GifImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(LOG, "GifImageView(Context context, AttributeSet attrs, int defStyleAttr)");
        getHolder().addCallback(this);
        
    }

    public GifImageView(Context context) {
        super(context);
        Log.d(LOG, "GifImageView(Context context)");
        getHolder().addCallback(this);
    }

    public GifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        Log.d(LOG, "GifImageView(Context context, AttributeSet attrs)");

    }

    
	public void loadResurce(int id)
    {
		
        setFocusable(true);
        this.id = id;
        inputStream = getContext().getResources().openRawResource(id);
        movie = Movie.decodeStream(inputStream);
        LayoutParams lp = GifImageView.this.getLayoutParams();
        lp.width = movie.width()/2; // required width
        lp.height = movie.height()/2; // required height
        GifImageView.this.setLayoutParams(lp);         
    }
 
	@Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {  }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
   	
    	
      drawThread = new DrawThread(getHolder());
      drawThread.setRunning(true);
      drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
      boolean retry = true;
      drawThread.setRunning(false);
      while (retry) {
        try {
          drawThread.join();
          retry = false;
        } catch (InterruptedException e) {
        }
      }
    }
    
    private void DrawGIF(Canvas canvas)
    {
          canvas.drawColor(Color.TRANSPARENT);
          if (movie == null) return;
          
          long now = SystemClock.uptimeMillis();
          if (mMovieStart == 0) { 
              mMovieStart = now;
          }
      
          int dur = movie.duration();
          if (dur == 0) {
              dur = 3000;
          }
          canvas.scale(0.5f, 0.5f);
          int relTime = (int) ((now - mMovieStart) % dur);
          movie.setTime(relTime);
          movie.draw(canvas,0,0);
	
    }    

    class DrawThread extends Thread {

      private boolean running = false;
      private SurfaceHolder surfaceHolder;

      public DrawThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
      }

      public void setRunning(boolean running) {
        this.running = running;
      }

      @Override
      public void run() {
        Canvas canvas;
      
        while (running) {
          canvas = null;
          try {
              if (movie == null)
              	continue;            	  
            canvas = surfaceHolder.lockCanvas(new Rect(0, 0, movie.width()/2, movie.height()/2));
            if (canvas == null)
                continue;

            DrawGIF(canvas);
          } finally {
            if (canvas != null) {
              surfaceHolder.unlockCanvasAndPost(canvas);
            }
          }
        }
      }
    }

    

}
