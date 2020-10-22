package com.example.rakshak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Addsponsorship extends AppCompatActivity {
FirebaseAuth auth;
DatabaseReference database,userspon,allsponsors;
FirebaseUser user;
EditText spid;
Button b;
String sponsorship,name,useradd,highestlevel,contribution,emailuser;
Map<String,Object> map,map2;
ProgressBar bar;
String spname="";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsponsorship);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        database=FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users").child(user.getUid());
        userspon=FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users");

        map=new HashMap<>();
        map2=new HashMap<>();

        allsponsors=userspon;
        spid=findViewById(R.id.sponsoradd);
        b=findViewById(R.id.sponsorbutton);
        bar=findViewById(R.id.progressaddsponsor);
        sponsorship="";
        useradd=user.getUid();

        bar.setVisibility(View.INVISIBLE);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(spid.getText().toString())) {
                      database.addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              bar.setVisibility(View.VISIBLE);
                              sponsorship=snapshot.child("Sponsorship email").getValue().toString();
                              contribution=snapshot.child("Contribution money").getValue().toString();
                              highestlevel=snapshot.child("Highest level").getValue().toString();
                              name=snapshot.child("Name").getValue().toString();
                              emailuser=snapshot.child("Email").getValue().toString();
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {

                          }
                      });

                      userspon.addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              Iterator<DataSnapshot> allusers=snapshot.getChildren().iterator();
                               while(allusers.hasNext()) {
                                    DataSnapshot user=allusers.next();
                                    if(sponsorship.equals("empty") && user.child("Email").getValue().toString().equals(spid.getText().toString())) {
                                        spname=user.child("Name").getValue().toString();
                                        Toast.makeText(Addsponsorship.this, "Welcome to "+user.child("Name").getValue().toString() + " Team", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(Addsponsorship.this, "You were referred by "+user.child("Name").getValue().toString(), Toast.LENGTH_SHORT).show();
                                         map.put("Sponsorship email",user.child("Email").getValue().toString());
                                         database.updateChildren(map);
                                         map2.put("Email",emailuser);
                                         map2.put("Name",name);
                                         map2.put("Highest level",highestlevel);
                                         map2.put("Contribution money",contribution);

                                         allsponsors.child(user.getKey().toString()).child("SPONSORED PEOPLE").child(useradd).updateChildren(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 if(task.isSuccessful()) {
                                                     new AlertDialog.Builder(Addsponsorship.this).setIcon(R.drawable.ic_baseline_sports_kabaddi_24).setTitle("SPONSORSHIP ADDED!")
                                                             .setMessage("Welcome to "+spname+"'s Team").setPositiveButton("YAYY", new DialogInterface.OnClickListener() {
                                                         @Override
                                                         public void onClick(DialogInterface dialog, int which) {
                                                             Intent i=new Intent(Addsponsorship.this,MainActivity.class);
                                                             startActivity(i);
                                                         }
                                                     }).show();

                                                 }
                                             }
                                         });
                                    }
                               }
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {

                          }
                      });



                }
            }
        });
    }
}