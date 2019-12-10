package com.example.ponggame;

import android.content.Intent;

import android.view.SurfaceView;
import android.content.Context;
import android.view.SurfaceHolder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.graphics.RectF;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;
import android.media.AudioManager;
import android.media.MediaPlayer;

class PongGame extends SurfaceView implements Runnable {

    // Are we debugging?
    private final boolean DEBUGGING = true;
    // These objects are needed to do the drawing
    private SurfaceHolder mOurHolder;
    private Canvas mCanvas;
    private Paint mPaint;
    // FPS
    private long mFPS;

    public static Bitmap bitmap;
    // The number of milliseconds in a second
    private final int MILLIS_IN_SECOND = 1000;
    // resolution of the screen
    private int mScreenX;
    private int mScreenY;
    // text size
    private int mFontSize;
    private int mFontMargin;
    // The game objects
    private Bat mBat;
    private Ball mBall;
    //array of bricks
    ArrayList<Block> mBlock;

    int mWidth;
    int mHeight;
    // The current score and lives remaining
    public static int mScore;
    private int mLives;

    // Thread and two control variables
    private Thread mGameThread = null;
    // volatile variable can be accessed from inside and outside the thread
    private volatile boolean mPlaying;
    private boolean mPaused = true;

    //private boolean isChallen;
    //private Block whichBlock;

    public PongGame(Context context, int x, int y) {
        super(context);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.geof);
        //Initialize the array of blocks called mBlocks
        mBlock = new ArrayList<Block>();

        // Initialize these two members/fields
        // With the values passed in as parameters
        mScreenX = x;
        mScreenY = y;
        // Font is 5% (1/20th) of screen width
        mFontSize = mScreenX / 20;
        // Margin is 1.5% (1/75th) of screen width
        mFontMargin = mScreenX / 75;
        // Initialize the objects for drawing with getHolder
        mOurHolder = getHolder();
        mPaint = new Paint();
        // Initialize the bat and ball
        mBall = new Ball(mScreenX);
        mBat = new Bat(mScreenX, mScreenY);

        // Everything is ready so start the game
        startNewGame();
    }
    // This method is called by PongActivity
    // when the player quits the game
    public void pause() {
        // Set mPlaying to false
        // Stopping the thread isn't
        // always instant
        mPlaying = false;
        try {
            // Stop the thread
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }
    // This method is called by PongActivity
    // when player starts the game
    public void resume() {
        mPlaying = true;
        // Initialize the instance of Thread
        mGameThread = new Thread(this);
        // Start the thread
        mGameThread.start();
    }
    // The player has just lost
    // or is starting their first game
    private void startNewGame(){
        // Reset Ball
        mBall.reset(mScreenX, mScreenY);
        mBat.reset(mScreenX, mScreenY);

        //Reset Blocks
        mBlock.clear();

        //Reset brick layer
        mWidth = mScreenX / 7;
        mHeight = mScreenY / 10;
        //Build Brick Layer
        //int m = 0;
        for(int row = 0; row < 3; row ++ ){
            for(int column = 0; column < 7; column ++ ) {
                mBlock.add(new Block(row, column, mWidth, mHeight, getContext()));
            }
        }

        // Reset the score and the player's lives
        mScore = 0;
        mLives = 3;
    }
    @Override
    public void run() {
        while (mPlaying) {
            long frameStartTime = System.currentTimeMillis();
            if(!mPaused){
                update();
                detectCollisions();
            }
            draw();
            // Length of each loop
            // Store the answer in timeThisFrame
            long timeThisFrame = System.currentTimeMillis() - frameStartTime;
            if (timeThisFrame > 0) {
                // Store FPS
                mFPS = MILLIS_IN_SECOND / timeThisFrame;
            }
        }
    }
    private int counter = 0;
    private int ballSpeedCounter = 0;
    private void update() {
        // Update the bat and the ball
        mBall.update(mFPS);
        mBat.update(mFPS);
        counter++;
        ballSpeedCounter++;
        if (counter == 500) {
            mWidth = mScreenX / 7;
            mHeight = mScreenY / 10;

            for(int column = 0; column < 7; column ++) {
                mBlock.add(mBlock.get(mBlock.size() - 7));
                mBlock.get(mBlock.size() - 1).increaseRow();
            }

            for(int index = mBlock.size() - 8; index > 6; index--) {
                mBlock.set(index, mBlock.get(index - 7));
                mBlock.get(index).increaseRow();
            }

            for(int column = 0; column < 7; column ++) {
                mBlock.set(column, new Block(0, column, mWidth, mHeight, getContext()));
            }
            counter = 0;
        }
        //update block row location


    }
    private void detectCollisions(){
        for(int i = 0; i < mBlock.size(); i++) {
            if (mBlock.get(i).getVisibility()) {
                if (RectF.intersects(mBlock.get(i).getRect(), mBall.getRect())) {
                    mBlock.get(i).setInvisible();

                    if (mBlock.get(i).getType().equals("large")) {
                        mBat.changeBat(mScreenX, mScreenY);
                    }

                    mBall.reverseYVelocity();
                    //Bitmap bitmap = ((Bitmap) R.drawable.geof).getBitmap();
                    //.setImageResource(R.drawable.cs125);

                    mScore++;

                }
            }
        }
        // bat hits ball
        if(RectF.intersects(mBat.getRect(), mBall.getRect())) {
            //bounce
            mBall.setRandomXVelocity();
            mBall.reverseYVelocity();
            if (ballSpeedCounter > 2500) {
                mBall.increaseVelocity();
                ballSpeedCounter = 0;
            }

        }
        // Has the ball hit the edge of the screen
        // Bottom
        if(mBall.getRect().bottom > mScreenY){
            mBall.reverseYVelocity();
            mLives--;
            //mSP.play(mMissID, 1, 1, 0, 0, 1);
            if(mLives == 0){
                Intent intent = new Intent(this.getContext(), GameOver.class);
                this.getContext().startActivity(intent);
            }
        }
        // Top
        if(mBall.getRect().top < 1){
            mBall.reverseYVelocity();
        }
        // Left
        if(mBall.getRect().left < 1){
            mBall.reverseXVelocity();
        }
        // Right
        if(mBall.getRect().right > mScreenX){
            mBall.reverseXVelocity();
        }
    }
    // Draw the game objects and the HUD
    private void draw() {
        if (mOurHolder.getSurface().isValid()) {
            // Lock the canvas
            mCanvas = mOurHolder.lockCanvas();
            // Fill the screen
            mCanvas.drawColor(Color.argb
                    (255, 26, 128, 182));
            for(int i = 0; i < mBlock.size(); i++){
                mBlock.get(i).render(mCanvas);
//                if(mBlock.get(i).getVisibility()) {
//                    mPaint.setColor(Color.argb(255,  249, 129, 0));
//                    if (mBlock.get(i).getType().equals("large")){
//                        mPaint.setColor(Color.argb(255,  253, 253, 150));
//                    }
//                    mCanvas.drawRect(mBlock.get(i).getRect(), mPaint);
//                }
            }
            // Choose a color
            mPaint.setColor(Color.argb(255,  255, 255, 255));
            // Draw the bat and ball
            mCanvas.drawRect(mBall.getRect(), mPaint);
            mCanvas.drawRect(mBat.getRect(), mPaint);

            // Choose the font size
            mPaint.setTextSize(mFontSize);
            // Draw the HUD
            mCanvas.drawText("Score: " + mScore + " Lives: " + mLives,
                    mFontMargin, mFontSize, mPaint);
            if (DEBUGGING) {
                printDebuggingText();
            }
            // cover blocks with challen sprite
            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.geof);
            //mCanvas.drawBitmap(bitmap, null, whichBlock.getRect(), null);
            // Display the drawing on screen
            // unlockCanvasAndPost is a method of SurfaceView

            mOurHolder.unlockCanvasAndPost(mCanvas);
        }
    }
    private void printDebuggingText(){
        int debugSize = mFontSize / 2;
        int debugStart = 150;
        mPaint.setTextSize(debugSize);
        mCanvas.drawText("FPS: " + mFPS ,
                10, debugStart + debugSize, mPaint);
    }
    // Handle all the screen touches
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() &
                MotionEvent.ACTION_MASK) {
            // The player has put their finger on the screen
            case MotionEvent.ACTION_DOWN:
                // If the game was paused unpause
                mPaused = false;
                // Where did the touch happen
                if(motionEvent.getX() > mScreenX / 2){
                    // On the right hand side
                    mBat.setMovementState(mBat.RIGHT);
                }
                else{
                    // On the left hand side
                    mBat.setMovementState(mBat.LEFT);
                }
                break;
            case MotionEvent.ACTION_UP:
                // Stop the bat moving
                mBat.setMovementState(mBat.STOPPED);
                break;
        }
        return true;
    }
}
