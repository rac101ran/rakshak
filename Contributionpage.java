package com.example.rakshak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class Contributionpage extends AppCompatActivity {
    ImageView level1, level2, level3;
    DatabaseReference ref, ref2;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressBar bar;
    long teamsize = 0;
    String leveltwo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributionpage);
        level1 = findViewById(R.id.lvl1);
        level2 = findViewById(R.id.lvl2);
        level3 = findViewById(R.id.lvl3);
        bar = findViewById(R.id.bar2);
        bar.setVisibility(View.INVISIBLE);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bar.setVisibility(View.VISIBLE);
            }
        },2000);

    }

    public void lvloneclick(View view) {
        bar.setVisibility(View.VISIBLE);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref = FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("level 1").getValue().toString().equals("false")) {
                    bar.setVisibility(View.INVISIBLE);
                    new AlertDialog.Builder(Contributionpage.this).setTitle("GET STARTED WITH LEVEL I").setMessage("Enter and contribute with a min. of Rs. 100").setIcon(
                            R.drawable.ic_baseline_monetization_on_24).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Contributionpage.this, LevelonePayPage.class);
                            startActivity(i);

                        }
                    }).setNegativeButton("NO", null).show();
                } else {
                    Toast.makeText(Contributionpage.this, "You have already reached level 1", Toast.LENGTH_SHORT).show();
                    bar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void lvltwoclick(View view) {
        bar.setVisibility(View.VISIBLE);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref = FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users").child(user.getUid());
        ref2 = FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users").child(user.getUid()).child("TEAM");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leveltwo = snapshot.child("level 2").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teamsize = snapshot.getChildrenCount();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (teamsize >= 200) {
            if (leveltwo.equals("false")) {
                new AlertDialog.Builder(Contributionpage.this).setIcon(R.drawable.ic_baseline_group_24).setTitle("Enter Level Two").setMessage("Moving right along...$ $ $").setPositiveButton("YES", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Contributionpage.this, LeveltwoPayPage.class);
                                startActivity(i);
                            }
                        }).show();
            } else if (leveltwo.equals("true")) {

                Toast.makeText(this, "You are already on level one", Toast.LENGTH_SHORT).show();

            }
        } else {
            bar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "You haven't unlocked this level yet.", Toast.LENGTH_SHORT).show();
        }
    }
}



