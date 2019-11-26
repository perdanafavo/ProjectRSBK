package com.androidtan.favoperdana.androidngunyah;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashscreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        createView();
    }

    private void createView() {
        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashscreenActivity.this, MainActivity.class));
                    finish();
                }
            }
        };
        thread.start();
    }
}
