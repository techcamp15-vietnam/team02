package com.shinkansen.touchcolor;

import com.shinkansen.touchcolor.soundmanager.SoundManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreen extends Activity { 
	 // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private AnimationDrawable animation2;
	private ImageView imgAni2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalsh);
        imgAni2 = (ImageView) findViewById(R.id.logoView);
		animation2 = (AnimationDrawable) imgAni2.getDrawable();
		 
        animation2.setCallback(imgAni2);

        animation2.setVisible(true, true);
        animation2.start();
        SoundManager.getInstance().initSoundBackground(this);
        SoundManager.getInstance().initSound(this);
 
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}