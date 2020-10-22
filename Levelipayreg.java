package com.example.rakshak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
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
import java.util.Iterator;
import java.util.Map;

public class Levelipayreg extends AppCompatActivity {
Button b;
Map<String,Object> map;
DatabaseReference ref,ref2;
FirebaseAuth auth;
FirebaseFirestore firestore;
DocumentReference docs;
int amount=100;
int c;
public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
int GOOGLE_PAY_REQUEST_CODE = 123;
    String name = "";
    String upiId = "";
    String transactionNote = "Level I registration";
    String status;
    Uri uri;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelipayreg);
        b=findViewById(R.id.payregular);
        map=new HashMap<>();
        c=1;
        firestore=FirebaseFirestore.getInstance();
        docs=firestore.collection("RAKSHAK").document("ADMIN");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        ref= FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users").child(user.getUid());
        ref2=FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.setEnabled(false);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name=snapshot.child("Name").getValue().toString();
                        upiId=snapshot.child("Account number").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                firestore.collection("RAKSHAK").document("ADMIN").update("Contributions",c+100).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Levelipayreg.this, "THANK YOU FOR CONTRIBUTION", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                try {
                    if (amount == 100 && !upiId.equals("") && !name.equals("")) {
                        uri = getUpiPaymentUri(name, upiId, transactionNote, Integer.toString(amount));
                        payWithGPay();
                    }
                }catch(Exception e) {
                    Toast.makeText(Levelipayreg.this, "Update Profile Settings", Toast.LENGTH_SHORT).show();
                }


            }
        });

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

    }
    private static Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }
    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void payWithGPay() {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        } else {
            Toast.makeText(Levelipayreg.this, "Please Install GPay", Toast.LENGTH_SHORT).show();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }

        if ((RESULT_OK == resultCode) && status.equals("success")) {
            map.put("level 1",true);
            map.put("Highest level","-");
            map.put("Contribution money","100");
            ref.updateChildren(map);
            Toast.makeText(Levelipayreg.this, "Transaction Successful", Toast.LENGTH_SHORT).show();

            new AlertDialog.Builder(Levelipayreg.this).setIcon(R.drawable.ic_baseline_supervised_user_circle_24).setTitle("Add Sponsorship ID ?").setMessage(
                    "Add the ID of the person who made you a contributer."
            ).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i=new Intent(Levelipayreg.this,Addsponsorship.class);
                    startActivity(i);
                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i=new Intent(Levelipayreg.this,MainActivity.class);
                    startActivity(i);
                }
            }).show();

        } else {
            Toast.makeText(Levelipayreg.this, "Transaction Failed", Toast.LENGTH_SHORT).show();
        }
    }
}