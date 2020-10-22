package com.example.rakshak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Signuppage extends AppCompatActivity {
    EditText name, email, pass;
    ProgressBar bar;
    Button signupbutton;
    FirebaseUser user;
    FirebaseAuth auth;
    SharedPreferences pref;
    DatabaseReference ref2,reference;
    Map<String ,Object> m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);
        name = findViewById(R.id.nametextinwallet);
        email = findViewById(R.id.emailtext);
        pass = findViewById(R.id.passtext);
        bar = findViewById(R.id.progressSignup);
        signupbutton = findViewById(R.id.signup);
        auth = FirebaseAuth.getInstance();
        bar.setVisibility(View.INVISIBLE);
        //user = auth.getCurrentUser();
        m=new HashMap<>();
       // pref=(this).getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      bar.setVisibility(View.VISIBLE);
                 if(!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(pass.getText().toString())) {
                     //  pref.edit().putString("name",name.getText().toString()).apply();
                       signup(name.getText().toString(),email.getText().toString(),pass.getText().toString());
                 }else {
                     bar.setVisibility(View.INVISIBLE);

                     Toast.makeText(Signuppage.this, "Enter Again", Toast.LENGTH_SHORT).show();
                 }
            }
        });
    }


    public void signup(final String name, final String email, String pass) {
         auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful()) {
                         user = auth.getCurrentUser();
                         reference=FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users");
                         ref2=reference.child(user.getUid());

                         Calendar c=Calendar.getInstance();
                         String currentdate= DateFormat.getDateInstance().format(c.getTime());
                         String strDate="";
                         String strYear="";
                         for(int i=0; i<currentdate.length(); i++) {
                               if(currentdate.charAt(i)==',') {
                                     strDate=currentdate.substring(3,i);
                                     strYear=currentdate.substring(i+1,currentdate.length());
                                     //strYear=currentdate.substring(i+1,currentdate.length());
                                     break;
                               }
                         }
                         Timestamp timestamp=Timestamp.now();
                         strYear=strYear.replace(" ","");
                         strDate=strDate.replace(" ","");
                         String strMonth=currentdate.substring(0,3);
                         m.put("Name",name);
                         m.put("level 1",false);
                         m.put("Sponsorship email","empty");
                         m.put("Highest level","-");
                         m.put("Email",email);
                         m.put("JOINED ON",currentdate);
                         m.put("Joined month",strMonth);
                         m.put("Joined year",strYear);
                         m.put("level 2",false);
                         m.put("level 3",false);
                         m.put("level 4",false);
                         m.put("image",false);
                         m.put("Contribution money",0);
                         m.put("Joined date",Integer.parseInt(strDate));
                         m.put("Exact time",timestamp.getSeconds());
                         Toast.makeText(Signuppage.this, user.getUid(), Toast.LENGTH_LONG).show();
                        /* pref=(SharedPreferences)Signuppage.this.getSharedPreferences("getPackageName()",Context.MODE_PRIVATE);
                         pref.edit().putInt("userdate",Integer.parseInt(strDate)).apply();
                         pref.edit().putString("usermonth",strMonth).apply();
                         pref.edit().putInt("useryear",Integer.parseInt(strYear)).apply();
                         pref.edit().putString("Email",email).apply();
                         pref.edit().putLong("usertimestamp",timestamp.getSeconds()).apply();*/
                         Toast.makeText(Signuppage.this, "Welcome to Rakshak: An Initiative", Toast.LENGTH_SHORT).show();
                         ref2.updateChildren(m);
                         Intent i=new Intent(Signuppage.this,MainActivity.class);
                         startActivity(i);
                         finish();
                     }
             }
         });

    }

}
