package com.example.aidan.tilegameredo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GameScreen extends AppCompatActivity {
    private GamePanel panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        panel = new GamePanel(this,b.getString("pack"));
        //panel = new GamePanel(this,"default");
        Intent in = getIntent();
        Uri data = in.getData();
        Log.e("url",data.getEncodedPath());
        final GestureDetector gestureDetector = new GestureDetector(this, new SwipeGestureDetector());
        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        panel.setOnTouchListener(gestureListener);

        setContentView(panel);
    }

    @Override
    protected void onResume(){
        super.onResume();
        panel.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        panel.pause();
    }

    static class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {

            Double angle = Math.toDegrees(Math.atan2(e1.getY() - e2.getY(), e2.getX() - e1.getX()));
            //Log.e("distance",Math.sqrt((e1.getY() - e2.getY())*(e1.getY() - e2.getY())+(e2.getX() - e1.getX())*(e2.getX() - e1.getX()))+"");
            if(Math.sqrt((e1.getY() - e2.getY())*(e1.getY() - e2.getY())+(e2.getX() - e1.getX())*(e2.getX() - e1.getX()))>60){
                if (angle > 45 && angle <= 135) {
                    Game.swipe(1);

                } else if (angle >= 135 && angle < 180 || angle < -135 && angle > -180) {
                    Game.swipe(4);
                } else if (angle < -45 && angle >= -135) {
                    Game.swipe(3);

                } else if (angle > -45 && angle <= 45) {
                    Game.swipe(2);

                }
            }
            return false;
        }
    }

}
