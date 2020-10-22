package com.example.rakshak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Totalcontributors extends RecyclerView.Adapter<Totalcontributors.Littlecontri>  {
    Context c;
     ArrayList<String> name,contributions,level;
     Integer[] image,rating;
    public Totalcontributors(Context c,ArrayList<String> name,ArrayList<String> contributions,ArrayList<String> level,Integer [] image,Integer[]rating) {
        this.contributions=contributions;
        this.name=name;
        this.level=level;
        //this.type=type;
        this.c=c;
        this.image=image;
        this.rating=rating;
    }

    @NonNull
    @Override
    public Totalcontributors.Littlecontri onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(c);
        View v=inflater.inflate(R.layout.rowtopcontributor,parent,false);
        return new Littlecontri(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Totalcontributors.Littlecontri holder, int position) {

        /*Iterator<String> image=this.name.iterator();
        while(image.hasNext()) {
            String i=image.next();
            if(this.image.containsKey(i)) {
                holder.profilepic.setImageResource(this.image.get(i));
            }
        }
        holder.profilepic.setImageResource(this.image.put(name.)*/

        holder.profilepic.setImageResource(this.image[position]);
        holder.namet.setText(this.name.get(position));
        holder.contri.setText(this.contributions.get(position));
        holder.lvl.setText(this.level.get(position));
        holder.rating.setImageResource(this.rating[position]);
    }

    @Override
    public int getItemCount() {
        return name.size();
    }
    public class Littlecontri extends RecyclerView.ViewHolder {
        TextView namet,contri,lvl;
                ImageView profilepic,rating;
        public Littlecontri(@NonNull View itemView) {
            super(itemView);
            namet=itemView.findViewById(R.id.nameid);
            contri=itemView.findViewById(R.id.contripricebox);
            lvl=itemView.findViewById(R.id.levelbox);
            rating=itemView.findViewById(R.id.starstat);
            profilepic=itemView.findViewById(R.id.contributorpic);

        }
    }

}



