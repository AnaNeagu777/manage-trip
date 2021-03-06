package com.example.ana.managetrip;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.ana.managetrip.MenuActivity.TRIP_ADD_REQUEST_CODE;
import static com.example.ana.managetrip.MenuActivity.TRIP_EDIT_REQUEST_CODE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<TripEntity> trips = new ArrayList<>();
    private Activity mActivity;

    public RecyclerViewAdapter(Activity mActivity, ArrayList<TripEntity> trips) {
        this.trips = trips;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        TripEntity e = trips.get(position);

        if (e.getImage() != null) {
            byte[] bytes = Base64.decode(e.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.image.setImageBitmap(bitmap);
        } else {
            holder.image.setImageDrawable(null);
        }
        holder.imageName.setText(trips.get(position).getName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Log.d(TAG, "onClick: clicked on: " + trips.get(position).getName());

                Intent intent = new Intent(mActivity, ManageTripActivity.class);
                intent.putExtra("entity", trips.get(position));
                intent.putExtra("position", position);
                mActivity.startActivityForResult(intent, TRIP_EDIT_REQUEST_CODE);
            }
        });
        holder.countyView.setText(e.getCountry());
        holder.ratingView.setText(String.format("%.1f", e.getRating()) + " / 5.0");
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }
}
