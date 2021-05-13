package com.google.mlkit.vision.demo.preference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.mlkit.vision.demo.R;

public class LoadingActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(getApplicationContext(), UserInterface.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
