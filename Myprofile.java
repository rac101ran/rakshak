package com.example.rakshak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Myprofile extends AppCompatActivity {
    RecyclerView rview;
    DatabaseReference ref, ref2, ref3;
    ArrayList<String> name, contribution_money, level, imageUris;
    FirebaseAuth auth;
    FirebaseUser userid;
    TextView nametextview;
    SearchView view;
    Button refresh;
    String yourname;
    StorageReference refstorage, refstorage2;

    Proflieadapter team;
    ArrayList<String> unique;
    ProgressBar bar;
    Map<String, Object> mapteamup, updatesponsorshipemail, map, updateTeamLevel;
    String email, highestlevel;
    //SharedPreferences emailpref, userjoiningTime;
    ArrayList<String> Mname, Memail, Mhighest, Mcon;
    private int date, month, year = 0;
    private long timestamp = 0;
    Map<String, Integer> MONTHS;
    ImageView imageView;
    String image;
    int countT = 0;
    long teamsize, sponsorteamsize = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        refresh = findViewById(R.id.refreshbutton);
        rview = findViewById(R.id.rviewid);
        bar = findViewById(R.id.loadteam);
        imageView = findViewById(R.id.imageView6);
        bar.setVisibility(View.INVISIBLE);
        nametextview=findViewById(R.id.nameiduser);
        image="";
        highestlevel = "-";

        //lists=findViewById(R.id.listview);

        name = new ArrayList<>();
        contribution_money = new ArrayList<>();
        level = new ArrayList<>();
        unique = new ArrayList<>();
        mapteamup = new HashMap<>();
        imageUris = new ArrayList<>();
        updatesponsorshipemail = new HashMap<>();
        Mname = new ArrayList<>();
        Memail = new ArrayList<>();
        Mhighest = new ArrayList<>();
        Mcon = new ArrayList<>();
        map = new HashMap<>();
        MONTHS = new HashMap<>();
        updateTeamLevel = new HashMap<>();

        MONTHS.put("Jan", 1);
        MONTHS.put("Feb", 2);
        MONTHS.put("Mar", 3);
        MONTHS.put("Apr", 4);
        MONTHS.put("May", 5);
        MONTHS.put("Jun", 6);
        MONTHS.put("Jul", 7);
        MONTHS.put("Aug", 8);
        MONTHS.put("Sep", 9);
        MONTHS.put("Oct", 10);
        MONTHS.put("Nov", 11);
        MONTHS.put("Dec", 12);

        //userjoiningTime = (SharedPreferences) this.getSharedPreferences("getPackageName()", Context.MODE_PRIVATE);

        auth = FirebaseAuth.getInstance();
        userid = auth.getCurrentUser();
        ref = FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users");
        ref2 = FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users").child(userid.getUid()).child("TEAM");
        ref3 = ref.child(userid.getUid()).child("SPONSORED PEOPLE");

        refstorage = FirebaseStorage.getInstance().getReference("Uploads").child(userid.getUid() + ".png");
        refstorage2 = FirebaseStorage.getInstance().getReference("Uploads").child(userid.getUid() + ".jpg");

        rview.setAdapter(team);
        rview.setLayoutManager(new LinearLayoutManager(Myprofile.this));

        imageView.setVisibility(View.INVISIBLE);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bar.setVisibility(View.VISIBLE);

                ref.child(userid.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        timestamp = Long.parseLong(snapshot.child("Exact time").getValue().toString());
                        highestlevel = snapshot.child("Highest level").getValue().toString();
                        image = snapshot.child("image").getValue().toString();
                        yourname=snapshot.child("Name").getValue().toString();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if (image.equals("false")) {
                    Toast.makeText(Myprofile.this, "Update Profile settings", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Myprofile.this, MainActivity.class);
                    startActivity(i);
                }else {
                    try {
                        final File file = File.createTempFile("name", "jpg").getAbsoluteFile();
                        refstorage.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                bar.setVisibility(View.INVISIBLE);
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                                nametextview.setText(yourname);

                                imageView.setImageBitmap(bitmap);
                                imageView.setVisibility(View.VISIBLE);
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

                                            nametextview.setText(yourname);
                                            Bitmap bit = BitmapFactory.decodeFile(file2.getAbsolutePath());

                                            nametextview.setText(yourname);
                                            imageView.setImageBitmap(bit);
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
                }

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> users = snapshot.getChildren().iterator();

                        while (users.hasNext()) {
                            DataSnapshot user = users.next();
                            if (!user.getKey().equals(userid.getUid()) && user.child("Sponsorship email").getValue().toString().equals("empty") && user.child("level 1").getValue().toString().equals("true")) {
                                if (Long.parseLong(user.child("Exact time").getValue().toString()) > timestamp) {

                                    //mapteamup.put("Sponsorship email","not spon");
                                    mapteamup.put("Name", user.child("Name").getValue().toString());
                                    mapteamup.put("Email", user.child("Email").getValue().toString());
                                    mapteamup.put("Contribution money", user.child("Contribution money").getValue().toString());
                                    mapteamup.put("Highest level", user.child("Highest level").getValue().toString());
                                    ref2.child(user.getKey()).updateChildren(mapteamup);
                                    Toast.makeText(Myprofile.this, "showing...", Toast.LENGTH_SHORT).show();

                                }

                            }


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                refresh.setText(R.string.visitteam);
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> team = snapshot.getChildren().iterator();
                        teamsize = snapshot.getChildrenCount();

                        while (team.hasNext()) {
                            DataSnapshot member = team.next();
                            if (!Memail.contains(member.child("Email").getValue().toString())) {
                                Mname.add(member.child("Name").getValue().toString());
                                imageUris.add(member.getKey().toString());
                                Mcon.add(member.child("Contribution money").getValue().toString());
                                Mhighest.add(member.child("Highest level").getValue().toString());
                                Memail.add(member.child("Email").getValue().toString());
                                countT++;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                team = new Proflieadapter(Myprofile.this, Mname, Mhighest, Mcon, imageUris);
                rview.setAdapter(team);


                ref3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sponsorteamsize = snapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Toast.makeText(Myprofile.this,Long.toString(teamsize), Toast.LENGTH_SHORT).show();
                if (highestlevel.equals("-")) {
                    if (teamsize >= 10 && sponsorteamsize > 3) {
                        updateTeamLevel.put("Highest level", "I");
                        updateTeamLevel.put("level 2", true);
                        ref.child(userid.getUid()).updateChildren(updateTeamLevel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    new AlertDialog.Builder(Myprofile.this).setIcon(R.drawable.ic_baseline_group_add_24).setTitle("Level I Unlocked").setMessage("You can get you money from E-wallet").setPositiveButton("YES", new
                                            DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = new Intent(Myprofile.this, Wallet.class);
                                                    startActivity(i);
                                                }
                                            }).setNegativeButton("NO", null).show();
                                }
                            }
                        });
                    }
                } else if (highestlevel.equals("I")) {
                    if (teamsize >= 200 && sponsorteamsize >= 25) {
                        updateTeamLevel.put("level 3", true);
                        updateTeamLevel.put("Highest level", "II");
                        ref.child(userid.getUid()).updateChildren(updateTeamLevel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    new AlertDialog.Builder(Myprofile.this).setIcon(R.drawable.ic_baseline_group_add_24).setTitle("Level II Unlocked").setMessage("You can get you money from E-wallet").setPositiveButton("YES", new
                                            DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = new Intent(Myprofile.this, Wallet.class);
                                                    startActivity(i);
                                                }
                                            }).setNegativeButton("NO", null).show();
                                }
                            }
                        });
                    }

                } else if (highestlevel.equals("II")) {
                    if (teamsize >= 500 && sponsorteamsize >= 50) {
                        updateTeamLevel.put("Highest level", "III");
                        updateTeamLevel.put("level 4", true);
                        ref.child(userid.getUid()).updateChildren(updateTeamLevel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    new AlertDialog.Builder(Myprofile.this).setIcon(R.drawable.ic_baseline_group_add_24).setTitle("Level III Unlocked").setMessage("You can get you money from E-wallet").setPositiveButton("YES", new
                                            DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = new Intent(Myprofile.this, Wallet.class);
                                                    startActivity(i);
                                                }
                                            }).setNegativeButton("NO", null).show();
                                }
                            }
                        });

                    }

                }

                // Toast.makeText(Myprofile.this, Integer.toString(countT), Toast.LENGTH_SHORT).show();
               /* ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> members=snapshot.getChildren().iterator();

                        while(members.hasNext()) {
                              DataSnapshot member=members.next();
                                   if(member.child("Sponsorship").getValue().toString().equals("empty") && !Memail.contains(member.child("Email").getValue().toString())) {
                                       Mname.add(member.child("Name").getValue().toString());
                                       Mhighest.add(member.child("Highest level").getValue().toString());
                                       Mcon.add(member.child("Contribution money").getValue().toString());
                                       Memail.add(member.child("Email").getValue().toString());
                                   }
                             }

                       // Toast.makeText(Myprofile.this, email, Toast.LENGTH_SHORT).show();
                        team=new Proflieadapter(Myprofile.this,Mname,Mhighest,Mcon);
                        rview.setAdapter(team);
                        // arrayAdapter.notifyDataSetChanged();
                        bar.setVisibility(View.INVISIBLE);
                        map.put("Team count",String.valueOf(Memail.size()));
                        ref3.child(userid.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Myprofile.this,"Team of "+ String.valueOf(Memail.size())+" people", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/
                Toast.makeText(Myprofile.this, "Your Team", Toast.LENGTH_SHORT).show();
            }
        });


    }
}