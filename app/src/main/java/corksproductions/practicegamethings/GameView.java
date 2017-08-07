package corksproductions.practicegamethings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Thomas on 8/4/2017.
 */

public class GameView extends SurfaceView implements Runnable {

    //DSOFHDUFHUAHDFAIUGDAFGDFAIAGF DHAFHAFHISGDAFD DSGKAGFHLAF

    volatile boolean playing; //just to check if playing. volatile means it can be affected by other threads, I think

    private Thread gameThread = null; //This is the thread. I think it does things
    private Context mContext; //This stores the context. It'll be useful when we want shared preferences or to leave the screen


    private Paint paint; //Pretty much your paint brush
    private Canvas canvas; //Your painting canvas
    private SurfaceHolder surfaceHolder; //??? your easel? ??

    private int max_x;
    private int max_y;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        mContext=context;
        surfaceHolder = getHolder(); //initialize things
        paint = new Paint();


        max_x=screenX;// record max screen size
        max_y=screenY;

        //We'll use this later
        stickFigureSprites[0]= BitmapFactory.decodeResource(context.getResources(), R.drawable.okay); //sprites for the figure
        stickFigureSprites[1]= BitmapFactory.decodeResource(context.getResources(), R.drawable.hurt);


        float stickWidth = stickFigureSprites[0].getWidth();
        float stickHeight = stickFigureSprites[0].getHeight();
        stickX=max_x/2-stickWidth/2; //location of the figure
        stickY=max_y/2-stickHeight/2;
        stickFigureRect = new Rect( //This is the hitbox for the stick figure
                (int) stickX, //left
                (int) stickY, //top
                (int) (stickX+stickWidth),//right
                (int) (stickY+stickHeight)); //bottom

    }



    @Override
    public void run() {
        while (playing) { //This is the main loop for the game
            update(); //Move all objects

            draw(); //Actually draw them

            sleep(); //pause for a few millisecs before starting next frame
        }
    }

    float ballX=60; //Normally, these are in an object, but for the sake of the demo
    float ballY=60; //X and Y locations
    float ballVx=10; //velocities in the x and y directions
    float ballVy=10;
    float ballRadius = 100;


    //Now, a stick figure "class"
    Bitmap[] stickFigureSprites = new Bitmap[2]; //these are the sprites for the stick figure
    int stickSpriteState=0;
    float stickX; //the figure's locations
    float stickY;
    Rect stickFigureRect; //hit box

    private void update(){
        if (ballX<ballRadius/2 || ballX>max_x-ballRadius/2) ballVx=-ballVx;
        if (ballY<ballRadius/2 || ballY>max_y-ballRadius/2) ballVy=-ballVy; //bounce when wall hit

        ballX+=ballVx;
        ballY+=ballVy; //ball is moved in one direction by its speed
    }
    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {

            canvas = surfaceHolder.lockCanvas(); //You have to do this whenever you want to draw

            canvas.drawColor(Color.WHITE); //Just the background is now white

            paint.setColor(Color.RED); //change color of paint --> ball will be red
            canvas.drawCircle(ballX,ballY,100,paint); //Draws the ball

            canvas.drawBitmap(stickFigureSprites[stickSpriteState],stickX,stickY,paint); //draws figure


            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas); //When you finished drawing the frame, you have to do this to save the changes
        }
    }

    private void sleep(){
        try {
            gameThread.sleep(17); //This is how long of a wait b/w frames
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) { //These are the touch sensores
        int x = (int) motionEvent.getX(); //These get the touch locations
        int y = (int) motionEvent.getY();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) { //a switch block for different ways they can touch
            case MotionEvent.ACTION_DOWN://just pressing down
            case MotionEvent.ACTION_MOVE://dragging finger
                if (stickFigureRect.contains(x,y)) stickSpriteState=1;
                else stickSpriteState=0;
                break;
            case MotionEvent.ACTION_UP://letting go
                stickSpriteState=0;
                break;
        }
        return true;
    }


    public void pause() {
        playing = false; //paused-->not playing
        try {
            //stops the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true; //resumed --> playing
        gameThread = new Thread(this);
        gameThread.start();
    }

}
