package com.example.rakshak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Teamview extends AppCompatActivity {
ArrayList<String> name,cont,level;
DatabaseReference ref;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamview);
        auth=FirebaseAuth.getInstance();
        //ref=FirebaseDatabase.getInstance().getReference("Users").child()
    }
}