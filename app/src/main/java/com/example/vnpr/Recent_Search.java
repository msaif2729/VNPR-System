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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Recent_Search extends Fragment implements Recent_Searches_List_Adapter.ItemClicked {
    private TextView text;
    private RecyclerView recentsearch_recycler;
    private RecyclerView.LayoutManager recentsearch_layputmanager;
    private RecyclerView.Adapter recentsearch_adapter;
    private ArrayList<Recent_Searches_List> recentSearchesLists;
    private View view;
    private String ownname,regisno,ownship,modelimage;
    private FirebaseFirestore db;
    private SessionMaintain sessionMaintain;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent__search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recentsearch_recycler = view.findViewById(R.id.recentsearch_recycler);
        recentsearch_recycler.setHasFixedSize(true);
        recentsearch_layputmanager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        recentsearch_recycler.setLayoutManager(recentsearch_layputmanager);
        db = FirebaseFirestore.getInstance();
        this.view=view;

        recentSearchesLists = new ArrayList<>();
        recentsearch_adapter = new Recent_Searches_List_Adapter(Recent_Search.this,recentSearchesLists);
        recentsearch_recycler.setAdapter(recentsearch_adapter);
        //create recent sessions
        sessionMaintain = new SessionMaintain(Recent_Search.this.getContext());
//        Toast.makeText(Recent_Search.this.getContext(), "Mohammed Saif", Toast.LENGTH_SHORT).show();
        showData();
    }

    @Override
    public void onItemClicked(int index,String str) {
        if(str.equals("long"))
        {
            Toast.makeText(view.getContext(),recentSearchesLists.get(index).getRegis_no().toString(), Toast.LENGTH_SHORT).show();
            String name = sessionMaintain.getSession("key_uname");
            String regisno = recentSearchesLists.get(index).getRegis_no().toString();
//            db.collection("users").document(name).collection("recent").document(regisno).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful())
//                    {
                        notifyUpdate(index);
                        Toast.makeText(Recent_Search.this.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(Recent_Search.this.getContext(), "Not Deleted", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }
        else
        {
            Toast.makeText(view.getContext(),recentSearchesLists.get(index).getRegis_no().toString(), Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(Recent_Search.this.getContext(), Vehicle_Desp.class);
            String plateno = recentSearchesLists.get(index).getRegis_no().toString();
            String model_name = recentSearchesLists.get(index).getVerhicle_img().toString();
            String owname = recentSearchesLists.get(index).getOwner_name().toString();
            String owntype = recentSearchesLists.get(index).getOwner_type().toString();
            intent.putExtra("vehicle_plate_no", plateno);
            intent.putExtra("model_image",model_name);
            intent.putExtra("oname", owname);
            intent.putExtra("otype",owntype);
            startActivity(intent);
        }


    }

    public void notifyUpdate(int pos)
    {
        recentSearchesLists.remove(pos);
        recentsearch_adapter.notifyItemRemoved(pos);
//        showData();
    }
    public void showData()
    {
        String name = sessionMaintain.getSession("key_uname");
//        Toast.makeText(Recent_Search.this.getContext(), name, Toast.LENGTH_SHORT).show();
        db.collection("users").document(name).collection("recent").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                recentSearchesLists.clear();
                for (DocumentSnapshot d:value)
                {
                    ownname = d.getString("ownname");
                    regisno = d.getString("regisno");
                    ownship = d.getString("ownship");
                    modelimage = d.getString("modelimage");
                    Log.d("Mohammed Saif",ownname+modelimage);
                    recentSearchesLists.add(new Recent_Searches_List(regisno,ownname,ownship,modelimage));
                    recentsearch_adapter.notifyDataSetChanged();
                }
            }
        });
    }
}