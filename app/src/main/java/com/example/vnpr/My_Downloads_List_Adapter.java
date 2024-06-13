package com.example.vnpr;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class My_Downloads_List_Adapter extends RecyclerView.Adapter<My_Downloads_List_ViewHolder> {

    ArrayList<My_Downloads_List> myDownloadsLists;
    View view;



    public My_Downloads_List_Adapter(ArrayList<My_Downloads_List> myDownloadsLists) {
        this.myDownloadsLists = myDownloadsLists;

    }

    @NonNull
    @Override
    public My_Downloads_List_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_downloads_list,parent,false);
        return new My_Downloads_List_ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull My_Downloads_List_ViewHolder holder, int position) {

        holder.itemView.setTag(myDownloadsLists.get(position));
        holder.regis_no.setText(myDownloadsLists.get(position).getRegis_no());
        holder.owner_name.setText(myDownloadsLists.get(position).getOwner_name());
        holder.owner_type.setText(myDownloadsLists.get(position).getOwner_type());
//        holder.vehicle_img.setImageResource(myDownloadsLists.get(position).getVerhicle_img());
        Glide.with(holder.itemView.getContext()).load(myDownloadsLists.get(position).getVerhicle_img()).into(holder.vehicle_img);

    }

    @Override
    public int getItemCount() {
        return myDownloadsLists.size();
    }
}




class My_Downloads_List_ViewHolder extends RecyclerView.ViewHolder
{
    ImageView vehicle_img;
    TextView regis_no,owner_name,owner_type;
    Button view,remove;
    My_Downloads_List_Adapter adapter;
    private FirebaseFirestore db;
    SessionMaintain sessionMaintain;



    public My_Downloads_List_ViewHolder(@NonNull View itemView) {
        super(itemView);

        regis_no = itemView.findViewById(R.id.regis_no);
        owner_name = itemView.findViewById(R.id.owner_name);
        owner_type = itemView.findViewById(R.id.owner_type);
        vehicle_img = itemView.findViewById(R.id.vehicle_img);
        view = itemView.findViewById(R.id.view);
        remove = itemView.findViewById(R.id.remove);
        db = FirebaseFirestore.getInstance();
        sessionMaintain = new SessionMaintain(itemView.getContext());

        itemView.findViewById(R.id.remove).setOnClickListener(view -> {
            String name = sessionMaintain.getSession("key_uname");
            String regisno = adapter.myDownloadsLists.get(getAdapterPosition()).getRegis_no().toString();
//            db.collection("users").document(name).collection("mydownloads").document(regisno).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful())
//                    {
                        adapter.myDownloadsLists.remove(getAdapterPosition());
                        adapter.notifyItemRemoved(getAdapterPosition());
                        Toast.makeText(itemView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(itemView.getContext(), "Not Deleted", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });


        });
        itemView.findViewById(R.id.view).setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "View All", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(itemView.getContext(), Vehicle_Desp.class);
            String plateno = adapter.myDownloadsLists.get(getAdapterPosition()).getRegis_no().toString();
            String model_name = adapter.myDownloadsLists.get(getAdapterPosition()).getVerhicle_img().toString();
            String owname = adapter.myDownloadsLists.get(getAdapterPosition()).getOwner_name().toString();
            String owntype = adapter.myDownloadsLists.get(getAdapterPosition()).getOwner_type().toString();
            intent.putExtra("vehicle_plate_no", plateno);
            intent.putExtra("model_image",model_name);
            intent.putExtra("oname", owname);
            intent.putExtra("otype",owntype);
            itemView.getContext().startActivity(intent);

        });

    }

    public My_Downloads_List_ViewHolder linkAdapter(My_Downloads_List_Adapter adapter)
    {
        this.adapter = adapter;
        return this;
    }



}

