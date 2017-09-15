package com.example.aidan.tilegameredo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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

            if (angle > 45 && angle <= 135) {
                if (!tilesMoving()) {
                    tileSort("Up");
                    for (Tile t : tiles) {
                        t.pushUp();
                    }
                }
            } else if (angle >= 135 && angle < 180 || angle < -135 && angle > -180) {
                tileSort("Left");
                if (!tilesMoving()) {
                    for (Tile t : tiles) {
                        t.pushLeft();
                    }
                }
            } else if (angle < -45 && angle >= -135) {
                if (!tilesMoving()) {
                    tileSort("Down");
                    for (Tile t : tiles) {
                        t.pushDown();
                    }
                }
            } else if (angle > -45 && angle <= 45) {
                if (!tilesMoving()) {
                    if(firstPlay){
                        firstPlay=false;
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("firstPlay", false);
                        editor.commit();

                    }
                    tileSort("Right");
                    for (Tile t : tiles) {
                        t.pushRight();
                    }
                }
            }
            return false;
        }
    }

}
