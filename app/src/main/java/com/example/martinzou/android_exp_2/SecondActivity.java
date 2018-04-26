package com.example.martinzou.android_exp_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by 10630 on 2018/4/26.
 */

public class SecondActivity extends AppCompatActivity {
    private Button button2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("SecondActivity","onCreate()方法调用");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SecondActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("SecondActivity","onStart()方法调用");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SecondActivity","onResume()方法调用");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("SecondActivity","onPause()方法调用");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("SecondActivity","onStop()方法调用");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SecondActivity","onDestroy()方法调用");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("SecondActivity","onRestart()方法调用");
    }
}
