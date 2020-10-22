package com.example.rakshak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;

import android.view.View;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.sql.Time;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class Wallet extends AppCompatActivity {
    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser user;
    StorageReference refstorage, refstorage2;
    ImageView imageview;
    TextView name, teamsizeview, money, getMoneylink;
    String flag, Teamsize = "";
    private long TotalteamCount, teamCount = 0;
    private long sponsorshipTeamCount = 0;
    ProgressBar bar;
    String imageuploaded;
    String earnings = "";
    boolean moneyflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        imageview = findViewById(R.id.profilephotoinwallet);
        getMoneylink = findViewById(R.id.getmoney);
        name = findViewById(R.id.nametextinwallet);
        teamsizeview = findViewById(R.id.teamsizeid);
        money = findViewById(R.id.moneyvalue);
        bar = findViewById(R.id.barload);
        moneyflag = false;

        bar.setVisibility(View.INVISIBLE);
        getMoneylink.setPaintFlags(getMoneylink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        refstorage = FirebaseStorage.getInstance().getReference("Uploads").child(user.getUid() + ".png");
        refstorage2 = FirebaseStorage.getInstance().getReference("Uploads").child(user.getUid() + ".jpg");
        ref = FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users").child(user.getUid());
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_baseline_account_balance_wallet_24).setTitle("Wallet Money Update").setMessage("Your next" +
                "pay will be on the 10th of next month").setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        flag = snapshot.child("level 1").getValue().toString();
                        imageuploaded = snapshot.child("image").getValue().toString();
                        if (flag.equals("false")) {
                            Toast.makeText(Wallet.this, "You have not registered!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Wallet.this, MainActivity.class);
                            startActivity(i);
                        } else if (imageuploaded.equals("false")) {
                            Toast.makeText(Wallet.this, "Fill the profile Settings", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Wallet.this, MainActivity.class);
                            startActivity(i);
                        } else {

                            try {
                                final File file = File.createTempFile("name", "jpg").getAbsoluteFile();
                                refstorage.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        bar.setVisibility(View.INVISIBLE);
                                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                        imageview.setImageBitmap(bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        bar.setVisibility(View.VISIBLE);
                                        try {
                                            final File file2 = File.createTempFile("name", "jpg").getAbsoluteFile();
                                            refstorage2.getFile(file2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    bar.setVisibility(View.INVISIBLE);
                                                    Bitmap bit = BitmapFactory.decodeFile(file2.getAbsolutePath());
                                                    imageview.setImageBitmap(bit);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    bar.setVisibility(View.INVISIBLE);
                                                }
                                            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    bar.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        bar.setVisibility(View.VISIBLE);
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            teamCount = snapshot.child("TEAM").getChildrenCount();
                            TotalteamCount = snapshot.child("TEAM").getChildrenCount() + snapshot.child("SPONSORED PEOPLE").getChildrenCount();
                            sponsorshipTeamCount = snapshot.child("SPONSORED PEOPLE").getChildrenCount();
                            Teamsize = Long.toString(TotalteamCount);
                            teamsizeview.setText(Teamsize);
                            earnings = percentEarn(snapshot.child("Contribution money").getValue().toString());
                            money.setText(earnings);
                            name.setText(snapshot.child("Name").getValue().toString());


                            if (flag.equals("true") && teamCount > 10) {
                                if (sponsorshipTeamCount > 3) {
                                    moneyflag = true;
                                    teamsizeview.setTextColor(getResources().getColor(R.color.green));
                                }
                                 // long c=10-icount;
                                // Toast.makeText(Wallet.this,c + " People required to start Payout", Toast.LENGTH_SHORT).show();
                            } else {
                                teamsizeview.setTextColor(getResources().getColor(R.color.red));
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Wallet.this, MainActivity.class);
                startActivity(i);
            }
        }).show();

        getMoneylink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMoneylink.setTextColor(getResources().getColor(R.color.yellow));
                if (!moneyflag) {
                    Toast.makeText(Wallet.this, "Not enough Team size to get Paid.", Toast.LENGTH_SHORT).show();
                }
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getMoneylink.setTextColor(getResources().getColor(R.color.black));
                    }
                }, 1000);

            }
        });

    }

    private String percentEarn(String price) {
        return Double.toString(0.3 * Integer.parseInt(price));
    }

}