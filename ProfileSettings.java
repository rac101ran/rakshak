package com.example.rakshak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.BasicPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileSettings extends AppCompatActivity {
 FirebaseAuth auth;
 FirebaseUser user;
 DatabaseReference ref,ref2;
 Map<String,Object> settingsdata,imageupload;
 EditText phn,acc,occ,addr;
 TextView name;
 Button save;
 ProgressBar bar;
 String nameT="";
 ImageView imageView;
 Boolean flag;
 Uri imageuri;
 Bitmap bitmap;
 StorageReference referenceimage;
 String encodedimage;
 String SERVERADDRESS="http://localhost/rakshak/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        name=findViewById(R.id.profilenameid);
        imageView=findViewById(R.id.imageiddd);
        imageupload = new HashMap<>();
        referenceimage= FirebaseStorage.getInstance().getReference("Uploads");
        ref= FirebaseDatabase.getInstance("https://rakshak-7cf3a.firebaseio.com/").getReference("Users").child(user.getUid());
        ref2=ref.child("USER DETAILS");
        bar=findViewById(R.id.barimageload);
        bar.setVisibility(View.INVISIBLE);
        flag=false;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("Name").getValue().toString());
                nameT=snapshot.child("Name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        settingsdata=new HashMap<>();

        phn=findViewById(R.id.phoneid);
        acc=findViewById(R.id.accid);
        occ=findViewById(R.id.occid);
        addr=findViewById(R.id.addressid);
        save=findViewById(R.id.profileupdateclick);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);
                 if(flag && !TextUtils.isEmpty(phn.getText().toString()) && !TextUtils.isEmpty(acc.getText().toString()) && !TextUtils.isEmpty(occ.getText().toString()) && !TextUtils.isEmpty(addr.getText().toString())){
                      

                     settingsdata.put("Phone number",phn.getText().toString());
                     settingsdata.put("Account number",acc.getText().toString());
                     settingsdata.put("Occupation",occ.getText().toString());
                     settingsdata.put("Address",addr.getText().toString());
                     ref2.updateChildren(settingsdata);

                    // PROFILESETTINGSUPDATE++;

                    // ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

                     StringRequest request=new StringRequest(Request.Method.POST, SERVERADDRESS, new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {
                             Toast.makeText(ProfileSettings.this, "Uploaded", Toast.LENGTH_SHORT).show();
                         }
                     }, new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {
                             Toast.makeText(ProfileSettings.this, error.getMessage(), Toast.LENGTH_LONG).show();
                         }
                     }) {
                         @Override
                         protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> m=new HashMap<>();
                            m.put("image",encodedimage);
                            m.put("name",user.getUid());
                            return m;
                         }
                     };
                     RequestQueue queue= Volley.newRequestQueue(ProfileSettings.this);
                     queue.add(request);


                    // Bitmap imagebitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();
                     upload();
                     Intent intent=new Intent(ProfileSettings.this,MainActivity.class);
                     startActivity(intent);
                     finish();
                 }else if(!flag){
                     bar.setVisibility(View.INVISIBLE);
                     Toast.makeText(ProfileSettings.this, "Select an image", Toast.LENGTH_SHORT).show();
                 }else {
                     bar.setVisibility(View.INVISIBLE);
                     Toast.makeText(ProfileSettings.this, "Enter Again.", Toast.LENGTH_SHORT).show();
                 }
            }
        });

    }

    public void addphoto(View view) {
        flag=true;
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      if(requestCode==3 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {

          Uri image=data.getData();
          InputStream inputStream= null;
          try {
              inputStream = getContentResolver().openInputStream(image);
              bitmap= BitmapFactory.decodeStream(inputStream);
              imageView.setImageBitmap(bitmap);
              imageConvert(bitmap);
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          }




      }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void imageConvert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imagebyte=outputStream.toByteArray();
        encodedimage=android.util.Base64.encodeToString(imagebyte, Base64.DEFAULT);
    }

    private String nameUri(Uri uri) {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mine=MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(uri));
    }
    private void upload() {
        if(imageuri!=null) {
            StorageReference  file=referenceimage.child(user.getUid()+"."+nameUri(imageuri));
            file.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  
                    bar.setProgress(100);
                    Toast.makeText(ProfileSettings.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                    Imageupload imageload=new Imageupload(nameT.trim(),taskSnapshot.getUploadSessionUri().toString());
                    imageupload.put("image",taskSnapshot.getMetadata().toString());
                    ref.updateChildren(imageupload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileSettings.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bar.setProgress(30);
                        }
                    },600);
                    Handler h=new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bar.setProgress(70);
                        }
                    },3000);


                }
            });
        }else {
            Toast.makeText(this, "No image Selected", Toast.LENGTH_SHORT).show();
        }
    }




}