package com.example.ana.managetrip;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecyclerViewFragment extends Fragment {

    private static final String TAG = "MainA";

    public ArrayList<TripEntity> trips = new ArrayList<>();
    public RecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: started");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initImageBitmaps();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

//        TripEntity e;

//        e = new TripEntity();
//        e.setName("Islands");
//        e.setImage("https://images.unsplash.com/photo-1529333241880-9558d5e5e064?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=658&q=80");
//        trips.add(e);
//
//        e = new TripEntity();
//        e.setName("Islands");
//        e.setImage("https://images.unsplash.com/photo-1529333241880-9558d5e5e064?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=658&q=80");
//        trips.add(e);
//        e = new TripEntity();
//        e.setName("Islands");
//        e.setImage("https://images.unsplash.com/photo-1529333241880-9558d5e5e064?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=658&q=80");
//        trips.add(e);
//        e = new TripEntity();
//        e.setName("Islands");
//        e.setImage("https://images.unsplash.com/photo-1529333241880-9558d5e5e064?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=658&q=80");
//        trips.add(e);

        FirebaseFirestore ff = FirebaseFirestore.getInstance();

        Task<QuerySnapshot> future = ff.collection("trips").get();

        future.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    trips.add(document.toObject(TripEntity.class));
                }
                adapter.notifyDataSetChanged();
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init RecyclerView.");
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(getActivity(), trips);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}
