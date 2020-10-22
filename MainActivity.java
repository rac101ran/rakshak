package com.example.rakshak;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth auth;
    DatabaseReference ref;
    String profilesettingsflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        profilesettingsflag="empty";

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        new AlertDialog.Builder(this).setTitle("UPDATE YOUR PROFILE").setMessage("first things first update your profile").setIcon(R.drawable.ic_baseline_assignment_ind_24).setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, ProfileSettings.class);
                        startActivity(i);
                    }
                }).setNegativeButton("NO", null).show();


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.walletuser, R.id.creatorpagemain)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.nav_home) {
                    Intent i = new Intent(MainActivity.this, Homepage.class);
                    startActivity(i);
                }
                if (destination.getId() == R.id.nav_gallery) {
                    Intent i = new Intent(MainActivity.this, Myprofile.class);
                    startActivity(i);
                }
                if (destination.getId() == R.id.nav_slideshow) {
                    Intent intent = new Intent(MainActivity.this, Contributionpage.class);
                    startActivity(intent);
                }
                if (destination.getId() == R.id.walletuser) {
                    Intent i = new Intent(MainActivity.this, Wallet.class);
                    startActivity(i);
                }
                if (destination.getId() == R.id.creatorpagemain) {
                    Intent i = new Intent(MainActivity.this, Creatorpage.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentuser = auth.getCurrentUser();
        if (currentuser == null) {
            Intent i = new Intent(MainActivity.this, Welcomepage.class);
            startActivity(i);
            finish();
        } /*else {

            try {
                ref=FirebaseDatabase.getInstance().getReference("Users").child(currentuser.getUid());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       // profilesettingsflag=snapshot.child("Profile settings update").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if (!profilesettingsflag.equals("done")) {

            }catch(Exception e) {
                e.printStackTrace();
            }
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {

            Intent i = new Intent(MainActivity.this, ProfileSettings.class);
            startActivity(i);

            return true;
        }
        if (item.getItemId() == R.id.logout) {
            auth.signOut();
            Intent i = new Intent(MainActivity.this, Welcomepage.class);
            startActivity(i);
            finish();

            return true;
        }
        if (R.id.profileid == item.getItemId()) {
            Intent i = new Intent(MainActivity.this, Profilemain.class);
            startActivity(i);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}