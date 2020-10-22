package com.example.rakshak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ViewFlipper;

import java.util.Timer;
import java.util.TimerTask;

public class Welcomepage extends AppCompatActivity {
ViewFlipper flipper;
Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomepage);
        flipper=findViewById(R.id.viewwelcome);
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                flipper.startFlipping();
            }
        },300);
    }

    public void signup(View view) {
        Intent i=new Intent(Welcomepage.this,Signuppage.class);
        startActivity(i);
    }
    public void login(View view) {
        Intent i=new Intent(Welcomepage.this,Loginpage.class);
        startActivity(i);
    }

}