package com.example.rakshak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class Profilemain extends AppCompatActivity {
    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView name, teamsizet, sponsorteamsize, highestlevel, emailid, phoneid, occid;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilemain);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        name = findViewById(R.id.nameid);
        teamsizet = findViewById(R.id.teamsizeprofilemainid);
        sponsorteamsize = findViewById(R.id.sponteamsizeprofilemainid);
        highestlevel = findViewById(R.id.highestlevelid);
        emailid = findViewById(R.id.emailidpromainvalue);
        phoneid = findViewById(R.id.phonemainvalue);
        occid=findViewById(R.id.occupationidvalue);
        title="    ";

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("Name").getValue().toString());
                highestlevel.setText(snapshot.child("Highest level").getValue().toString());

                long sponsoredcount = snapshot.child("SPONSORED PEOPLE").getChildrenCount();
                long teamsize = snapshot.child("TEAM").getChildrenCount();
                sponsorteamsize.setText(String.valueOf(sponsoredcount));
                teamsizet.setText(String.valueOf(teamsize));
                title+=snapshot.child("Email").getValue().toString();
                emailid.setText(title);
                title="    ";
                title+=snapshot.child("USER DETAILS").child("Phone number").getValue().toString();
                phoneid.setText(title);
                title="    ";
                title+=snapshot.child("USER DETAILS").child("Occupation").getValue().toString();
                occid.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        teamsizet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent i=new Intent(Profilemain.this,Teamview.class);
                  startActivity(i);
            }
        });
        sponsorteamsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent i=new Intent (Profilemain.this,Sponview.class);
                 startActivity(i);
            }
        });
    }




}