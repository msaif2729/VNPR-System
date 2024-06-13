package com.example.vnpr;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Recent_Searches_List_Adapter extends RecyclerView.Adapter<Recent_Searches_List_Adapter.ViewHolder> {

    private ArrayList<Recent_Searches_List> recentSearchesLists;
    ItemClicked activity;
    View view;

    public Recent_Searches_List_Adapter(Fragment context, ArrayList<Recent_Searches_List> recentSearchesLists) {
        this.recentSearchesLists = recentSearchesLists;
//        Toast.makeText(context.getContext(), context.getActivity().getClass().toString(), Toast.LENGTH_SHORT).show();
        activity = (ItemClicked) context;
    }


    public interface ItemClicked
    {
        void onItemClicked(int index,String str);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView vehicle_img;
        TextView regis_no,owner_name,owner_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            regis_no = itemView.findViewById(R.id.regis_no);
            owner_name = itemView.findViewById(R.id.owner_name);
            owner_type = itemView.findViewById(R.id.owner_type);
            vehicle_img = itemView.findViewById(R.id.vehicle_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String str="short";
                    activity.onItemClicked(recentSearchesLists.indexOf((Recent_Searches_List) view.getTag()),str);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    String str= "long";
                    activity.onItemClicked(recentSearchesLists.indexOf((Recent_Searches_List) view.getTag()),str);
                    return true;
                }
            });

        }
    }

    @NonNull
    @Override
    public Recent_Searches_List_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_searches_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Recent_Searches_List_Adapter.ViewHolder holder, int position) {

        holder.itemView.setTag(recentSearchesLists.get(position));
        holder.regis_no.setText(recentSearchesLists.get(position).getRegis_no());
        holder.owner_name.setText(recentSearchesLists.get(position).getOwner_name());
        holder.owner_type.setText(recentSearchesLists.get(position).getOwner_type());
//        holder.vehicle_img.setImageURI(recentSearchesLists.get(position).getVerhicle_img());
        Glide.with(view.getContext()).load(recentSearchesLists.get(position).verhicle_img).into(holder.vehicle_img);
    }

    @Override
    public int getItemCount() {
        return recentSearchesLists.size();
    }
}
