package com.example.ana.managetrip;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    public TextView imageName;
    public CardView parentLayout;
    public TextView countyView;
    public TextView ratingView;


    public ViewHolder(@NonNull View itemView) {

        super(itemView);

        image = itemView.findViewById(R.id.image);
        imageName = itemView.findViewById(R.id.image_name);
        parentLayout = itemView.findViewById(R.id.parent_layout);
        countyView = itemView.findViewById(R.id.textView5);
        ratingView = itemView.findViewById(R.id.textView6);
    }
}
