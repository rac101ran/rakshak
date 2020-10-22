package com.example.rakshak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class Loginpage extends AppCompatActivity {
    EditText emailtext;
    EditText passtext;
    ProgressBar bar;
    Timer timer;
    FirebaseAuth auth;
    FirebaseUser user;
    ViewFlipper flipper;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        bar = findViewById(R.id.progresslogin);
        emailtext = findViewById(R.id.emailid);
        passtext = findViewById(R.id.passid2);
        flipper = findViewById(R.id.wallviews);
        b = findViewById(R.id.loginbutton);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                flipper.startFlipping();
            }
        }, 2000);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        bar.setVisibility(View.INVISIBLE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(emailtext.getText().toString()) && !TextUtils.isEmpty(passtext.getText().toString())) {
                    login(emailtext.getText().toString(), passtext.getText().toString());
                } else {
                    bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Loginpage.this, "Enter Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(Loginpage.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });



    }
}