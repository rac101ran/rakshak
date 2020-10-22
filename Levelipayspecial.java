package com.example.rakshak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Levelipayspecial extends AppCompatActivity {
Button b;
DatabaseReference ref;
FirebaseAuth auth;
FirebaseUser user;
Map<String ,Object> map;
FirebaseFirestore firestore;
DocumentReference docs;
int c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelipayspecial);
        b=findViewById(R.id.payspecial);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        c=1;
        map=new HashMap<>();
        ref=FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users").child(user.getUid());
        firestore=FirebaseFirestore.getInstance();
        docs=firestore.collection("RAKSHAK").document("ADMIN");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 b.setEnabled(false);

                docs.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(e!=null) {
                            return;
                        }
                        if(documentSnapshot.contains("Contributions")) {
                            c= Integer.parseInt( documentSnapshot.get("Contributions").toString());
                        }
                    }
                });

                docs.update("Contributions",c+500).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful()) {
                             Toast.makeText(Levelipayspecial.this, "Thank you for your contribution", Toast.LENGTH_SHORT).show();
                         }
                    }
                });

                 b.setText("PAID");
                 map.put("level 1",true);
                 map.put("Highest level","I");
                 map.put("Contribution money","500");
                 ref.updateChildren(map);
            }
        });



    }
}


