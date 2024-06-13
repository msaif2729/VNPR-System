package com.example.vnpr;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Mydownload extends Fragment  {

    private RecyclerView mydownload_recycler;
    private RecyclerView.LayoutManager mydownload_layputmanager;
    private My_Downloads_List_Adapter mydownload_adapter;
    private ArrayList<My_Downloads_List> mydownloadLists;
    private View view;
    private  SessionMaintain sessionMaintain;
    private String ownname,regisno,ownship,modelimage;
    private FirebaseFirestore db;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mydownload_recycler = view.findViewById(R.id.mydownload_recycler);
        mydownload_recycler.setHasFixedSize(true);
        mydownload_layputmanager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        mydownload_recycler.setLayoutManager(mydownload_layputmanager);
        this.view=view;
        sessionMaintain = new SessionMaintain(Mydownload.this.getContext());
        db = FirebaseFirestore.getInstance();

        mydownloadLists = new ArrayList<>();
        showData();

    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mydownload, container, false);
    }

    public void showData()
    {
        String name = sessionMaintain.getSession("key_uname");
//        Toast.makeText(Recent_Search.this.getContext(), name, Toast.LENGTH_SHORT).show();
        db.collection("users").document(name).collection("mydownloads").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mydownloadLists.clear();
                for (DocumentSnapshot d:value)
                {
                    ownname = d.getString("ownname");
                    regisno = d.getString("regisno");
                    ownship = d.getString("ownship");
                    modelimage = d.getString("modelimage");
                    Log.d("Mohammed Saif",ownname+modelimage);
                    mydownloadLists.add(new My_Downloads_List(regisno,ownname,ownship,modelimage));
                    mydownload_adapter = new My_Downloads_List_Adapter(mydownloadLists);
                    mydownload_recycler.setAdapter(mydownload_adapter);
                     mydownload_adapter.notifyDataSetChanged();
                }
            }
        });
    }

//
//    @Override
//    public void start_Activity(Intent intent) {
//        startActivity(intent);
//    }
}