package com.google.mlkit.vision.demo.preference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.demo.R;
import com.google.mlkit.vision.demo.java.LivePreviewActivity;

public class UserInterface extends AppCompatActivity {
    public static Context context_interface;
    public boolean squat = false;
    public boolean lunge = false;
    public boolean pushup = false;
    public boolean situp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_interface);

        context_interface = this;

        Button imgbtn1= (Button) findViewById(R.id.button1);
        Button imgbtn2= (Button) findViewById(R.id.button2);
        Button imgbtn3= (Button) findViewById(R.id.button3);
        Button imgbtn4= (Button) findViewById(R.id.button4);

        imgbtn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                squat=true;
                pushup=false;
                lunge=false;
                situp=false;
                Intent intent = new Intent(getApplicationContext(), LivePreviewActivity.class);
                startActivity(intent);
            }
        });
        imgbtn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                squat=false;
                pushup=true;
                lunge=false;
                situp=false;
                Intent intent = new Intent(getApplicationContext(), LivePreviewActivity.class);
                startActivity(intent);
            }
        });
        imgbtn3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                squat=false;
                pushup=false;
                lunge=true;
                situp=false;
                Intent intent = new Intent(getApplicationContext(), LivePreviewActivity.class);
                startActivity(intent);
            }
        });
        imgbtn4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                squat=false;
                pushup=false;
                lunge=false;
                situp=true;
                Intent intent = new Intent(getApplicationContext(), LivePreviewActivity.class);
                startActivity(intent);
            }
        });

    }
}
