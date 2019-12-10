package com.example.ponggame;

import android.graphics.RectF;
import java.util.Random;

class Ball {
    // These are the member variables (fields)
    private RectF mRect;
    private float mXVelocity;
    private float mYVelocity;
    private float mBallWidth;
    private float mBallHeight;

    // This is the constructor
    Ball(int screenX) {
        // Make the ball square and 1% of screen width of the screen width
        mBallWidth = screenX / 100;
        mBallHeight = screenX / 100;
        // Initialize the RectF with 0, 0, 0, 0
        mRect = new RectF();
    }
    // Return a reference to mRect to PongGame
    RectF getRect(){
        return mRect;
    }
    // Update the ball position every frame
    void update(long fps){
        // Move the ball based upon the
        // horizontal (mXVelocity) and
        // vertical(mYVelocity) speed
        // and the current frame rate(fps)
        // Move the top left corner
        mRect.left = mRect.left + (mXVelocity / fps);
        mRect.top = mRect.top + (mYVelocity / fps);
        // Match up the bottom right corner
        // based on the size of the ball
        mRect.right = mRect.left + mBallWidth;
        mRect.bottom = mRect.top + mBallHeight;
    }
    // Reverse the vertical direction of travel
    void reverseYVelocity(){
        mYVelocity = -mYVelocity;
    }
    // Reverse the horizontal direction of travel
    void reverseXVelocity(){
        mXVelocity = -mXVelocity;
    }

    public void setRandomXVelocity(){
        Random generator = new Random();
        int answer = generator.nextInt(2);

        if(answer == 0){
            reverseXVelocity();
        }
    }

    void reset(int x, int y){
        // Initialise the four points of
        // the rectangle which defines the ball
        mRect.left = x / 2;
        mRect.top = y - 50;
        mRect.right = x / 2 + mBallWidth;
        mRect.bottom = y - 50 - mBallHeight;
        // How fast will the ball travel
        // You could vary this to suit
        // You could even increase it as the game progresses
        // to make it harder
        mYVelocity = -(y / 2);
        mXVelocity = (y / 2);
    }
    void increaseVelocity(){
        // increase the speed
        mXVelocity = mXVelocity * 1.05f;
        mYVelocity = mYVelocity * 1.05f;
    }
}
