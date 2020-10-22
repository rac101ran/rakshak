package com.example.rakshak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
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

import java.util.ArrayList;
import java.util.Iterator;

public class Homepage extends AppCompatActivity {

    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser user;
    ArrayList<String> arrayListuri;

ArrayList<String> topcontributorsname,topcontributormoney, topcontributorlevel,cname,cprice,clevel;
Integer[] images1={R.drawable.download,R.drawable.download,R.drawable.download,R.drawable.download};
Integer[]ratings1={R.drawable.fivestar,R.drawable.fourstar,R.drawable.threestar,R.drawable.threestar};
Integer[] images2={R.drawable.download,R.drawable.download,R.drawable.download,R.drawable.download,R.drawable.download,R.drawable.download,R.drawable.download,R.drawable.download,R.drawable.download,R.drawable.download,R.drawable.download,R.drawable.download,R.drawable.download,R.drawable.download,R.drawable.download};
Integer[]ratings2={R.drawable.threestar,R.drawable.twostr,R.drawable.twostr,R.drawable.twostr,R.drawable.onestr,R.drawable.onestr,R.drawable.onestr
,R.drawable.onestr,R.drawable.onestr,R.drawable.onestr,R.drawable.onestr,R.drawable.onestr,R.drawable.onestr,R.drawable.onestr,R.drawable.onestr};
RecyclerView view1,view2;
Totalcontributors contri,contrinormal;
TextView count;
FirebaseFirestore firestore;
DocumentReference docs;
ArrayList<String> Mname,Highestlevel,ContrigutionMoney,Memail;
Proflieadapter contributionT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_homepage);

         topcontributorlevel=new ArrayList<>();
         topcontributormoney=new ArrayList<>();
         topcontributorsname=new ArrayList<>();
         Mname=new ArrayList<>();
         Highestlevel=new ArrayList<>();
         ContrigutionMoney=new ArrayList<>();
         Memail=new ArrayList<>();
         arrayListuri=new ArrayList<>();

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        ref=FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users");

        firestore=FirebaseFirestore.getInstance();
        docs=firestore.collection("RAKSHAK").document("ADMIN");



        cname=new ArrayList<>();
        clevel=new ArrayList<>();
        cprice=new ArrayList<>();

        view1=findViewById(R.id.recyclerid);
        view2=findViewById(R.id.recyclerid2);
        count=findViewById(R.id.contributioncountid);


        topcontributorsname.add("Ashutosh");
        topcontributorsname.add("Avi andro");
        topcontributorsname.add("Pallawi");


        topcontributormoney.add("240");
        topcontributormoney.add("220");
        topcontributormoney.add("150");


        topcontributorlevel.add("III");
        topcontributorlevel.add("II");
        topcontributorlevel.add("II");

        cname.add("raman");
        cname.add("raman");
        cname.add("raman");
        cname.add("raman");
        cname.add("raman");
        cname.add("raman");
        cname.add("raman");
        cname.add("raman");
        cname.add("raman");
        cname.add("raman");
        cname.add("raman");
        cname.add("raman");
        cname.add("raman");
        cname.add("raman");
        cname.add("raman");

        cprice.add("100");
        cprice.add("150");
        cprice.add("100");
        cprice.add("150");
        cprice.add("100");
        cprice.add("150");
        cprice.add("100");
        cprice.add("150");
        cprice.add("100");
        cprice.add("150");
        cprice.add("100");
        cprice.add("150");
        cprice.add("100");
        cprice.add("150");
        cprice.add("100");

        clevel.add("II");
        clevel.add("II");
        clevel.add("I");
        clevel.add("II");
        clevel.add("II");
        clevel.add("I");
        clevel.add("II");
        clevel.add("II");
        clevel.add("I");
        clevel.add("II");
        clevel.add("II");
        clevel.add("I");
        clevel.add("II");
        clevel.add("II");
        clevel.add("I");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 Iterator<DataSnapshot> allusers=snapshot.getChildren().iterator();
                 Memail.clear();
                 arrayListuri.clear();
                 Highestlevel.clear();
                 ContrigutionMoney.clear();
                 Mname.clear();
                 while(allusers.hasNext()) {
                    DataSnapshot userunit=allusers.next();
                    if(!Memail.contains(userunit.child("Email").getValue().toString())) {
                        arrayListuri.add(userunit.getKey().toString());
                        Mname.add(userunit.child("Name").getValue().toString());
                        Highestlevel.add(userunit.child("Highest level").getValue().toString());
                        ContrigutionMoney.add(userunit.child("Contribution money").getValue().toString());
                        Memail.add(userunit.child("Email").getValue().toString());



                    }
                 }
                contributionT=new Proflieadapter(Homepage.this,Mname,Highestlevel,ContrigutionMoney,arrayListuri);
                 view2.setAdapter(contributionT);
                 view2.setLayoutManager(new LinearLayoutManager(Homepage.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        contri=new Totalcontributors(this,topcontributorsname,topcontributormoney,topcontributorlevel,images1,ratings1);
        contrinormal=new Totalcontributors(this,cname,cprice,clevel,images2,ratings2);
        view1.setAdapter(contri);
        view1.setLayoutManager(new LinearLayoutManager(this));

        //view2.setAdapter(contrinormal);
        //view2.setLayoutManager(new LinearLayoutManager(this));*/

        docs.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                 if(e!=null) {
                     return;
                 }
                 if(documentSnapshot.contains("Contributions")) {

                      count.setText(documentSnapshot.get("Contributions").toString());
                 }
            }
        });
    }


}
