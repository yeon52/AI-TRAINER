package com.google.mlkit.vision.demo.preference;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.demo.R;
import com.google.mlkit.vision.demo.java.LivePreviewActivity;

public class UserInterface extends AppCompatActivity {
    public static Context context_interface;
    public boolean squat = false;
    public boolean lunge = false;
    public boolean pushup = false;
    public boolean situp = false;

    private PermissionSupport permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_interface);

        permissionCheck();

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

    //권한 체크
    private void permissionCheck(){
        // SDK 23버전 이하 버전에서는 Permission이 필요하지 않습니다.
        if(Build.VERSION.SDK_INT >= 23){
            // 방금 전 만들었던 클래스 객체 생성
            permission = new PermissionSupport(this, this);

            // 권한 체크한 후에 리턴이 false로 들어온다면
            if (!permission.checkPermission()){
                // 권한 요청을 해줍니다.
                permission.requestPermission();
            }
        }
    }

    // Request Permission에 대한 결과 값을 받아올 수 있습니다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 여기서도 리턴이 false로 들어온다면 (사용자가 권한 허용을 거부하였다면)
        if(!permission.permissionResult(requestCode, permissions, grantResults)){
            // 저의 경우는 여기서 다시 Permission 요청을 걸었습니다.
            permission.requestPermission();
        }
    }
}
