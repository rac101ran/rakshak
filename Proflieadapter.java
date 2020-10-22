package com.example.rakshak;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class Proflieadapter extends RecyclerView.Adapter<Proflieadapter.Childadapter> {
    ArrayList<String> name,level,contributions;
    ArrayList<String> id;
    Context c;
    StorageReference ref,ref2;
    String refstr1="";
    String refstr2="";
    public Proflieadapter(Context c, ArrayList<String> name, ArrayList<String>level, ArrayList<String>contributions, ArrayList<String> id) {
        this.name=name;
        this.level=level;
        this.contributions=contributions;
        this.c=c;
        this.id=id;
    }

    @NonNull
    @Override
    public Childadapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(this.c);
        View v=inflater.inflate(R.layout.rowtopcontributor,parent,false);
        return new Childadapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final Childadapter holder, int position) {
          holder.name.setText(this.name.get(position));
          holder.contribution.setText(this.contributions.get(position));
          holder.level.setText(this.level.get(position));
          refstr1=this.id.get(position)+".png";
          refstr2=this.id.get(position)+".jpg";

          try {
                ref= FirebaseStorage.getInstance().getReference("Uploads").child(refstr1);
                final File file=File.createTempFile("image","jpg").getAbsoluteFile();
                ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                        holder.viewimages.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        try {
                            ref2=FirebaseStorage.getInstance().getReference("Uploads").child(refstr2);
                            final  File file2=File.createTempFile("image","jpg").getAbsoluteFile();
                            ref2.getFile(file2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap=BitmapFactory.decodeFile(file2.getAbsolutePath());
                                    holder.viewimages.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                        }catch(Exception e2) {
                              e2.printStackTrace();

                        }
                    }
                });
          }catch(Exception e1) {
              e1.printStackTrace();
          }

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class Childadapter extends RecyclerView.ViewHolder {
        TextView name,contribution,level;
        ImageView viewimages;
        public Childadapter(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.nameid);
            contribution=itemView.findViewById(R.id.contripricebox);
            level=itemView.findViewById(R.id.levelbox);
            viewimages=itemView.findViewById(R.id.contributorpic);

        }
    }
 }
