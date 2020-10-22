package com.example.rakshak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Timer;
import java.util.TimerTask;


public class LevelonePayPage extends AppCompatActivity {
TextView message;
Button pay1,pay2;
FirebaseAuth auth;
FirebaseUser user;
ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelone_pay_page);
        message=findViewById(R.id.levelonemsg);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        message.setText("\n"+"Register using Rs.100 and get direct sponsorship referrals to three people and get a Team of 10 People." +"\n\n"+"Or"+"\n\n\nPay Rs. 500 to get a team of 10 People.");
        pay1=findViewById(R.id.buttonregular);
        pay2=findViewById(R.id.buttonspecial);
        bar=findViewById(R.id.barRegister);
        bar.setVisibility(View.INVISIBLE);
        pay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);



                        pay2.setBackgroundColor(getResources().getColor(R.color.grey));
                        pay2.setEnabled(false);



                Intent i=new Intent(LevelonePayPage.this,Levelipayreg.class);
                //i.putExtra("getPackageName()"+"amount","100");
                startActivity(i);

            }
        });

        pay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);
                pay1.setBackgroundColor(getResources().getColor(R.color.grey));
                pay1.setEnabled(false);
                Intent i=new Intent(LevelonePayPage.this,Levelipayspecial.class);
                //i.putExtra("getPackageName()"+"amount","500");
                startActivity(i);
            }
        });

    }
}