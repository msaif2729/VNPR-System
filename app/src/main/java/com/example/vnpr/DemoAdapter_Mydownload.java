package com.example.vnpr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DemoAdapter_Mydownload extends RecyclerView.Adapter<DemoViewHolder> {

    ArrayList<My_Downloads_List> myDownloadsLists;

    public DemoAdapter_Mydownload(ArrayList<My_Downloads_List> myDownloadsLists) {
        this.myDownloadsLists = myDownloadsLists;
    }

    @NonNull
    @Override
    public DemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_downloads_list,parent,false);
        return new DemoViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull DemoViewHolder holder, int position) {

        holder.itemView.setTag(myDownloadsLists.get(position));
        holder.regis_no.setText(myDownloadsLists.get(position).getRegis_no());
        holder.owner_name.setText(myDownloadsLists.get(position).getOwner_name());
        holder.owner_type.setText(myDownloadsLists.get(position).getOwner_type());

    }

    @Override
    public int getItemCount() {
        return myDownloadsLists.size();
    }
}




class DemoViewHolder extends RecyclerView.ViewHolder
{
    ImageView vehicle_img;
    TextView regis_no,owner_name,owner_type;
    Button view,remove;
    DemoAdapter_Mydownload adapter;

    public DemoViewHolder(@NonNull View itemView) {
        super(itemView);

        regis_no = itemView.findViewById(R.id.regis_no);
        owner_name = itemView.findViewById(R.id.owner_name);
        owner_type = itemView.findViewById(R.id.owner_type);
        vehicle_img = itemView.findViewById(R.id.vehicle_img);
        view = itemView.findViewById(R.id.view);
        remove = itemView.findViewById(R.id.remove);

        itemView.findViewById(R.id.remove).setOnClickListener(view -> {
            adapter.myDownloadsLists.remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
        });

    }

    public DemoViewHolder linkAdapter(DemoAdapter_Mydownload adapter)
    {
        this.adapter = adapter;
        return this;
    }


}

