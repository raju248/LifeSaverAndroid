package com.example.sdproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class AdapterOfUserInfo extends RecyclerView.Adapter<AdapterOfUserInfo.UserViewHolder>{

    private Context mCtx;
    private List<User> user;

    public AdapterOfUserInfo(Context mCtx, List<User> user) {
        this.mCtx = mCtx;
        this.user = user;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_donors, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = this.user.get(position);
        holder.textViewName.setText(user.getName());
        holder.textViewMobileNo.setText("Mobile : " + user.getPhone());
        holder.textViewBloodGroup.setText("Blood Group : " + user.getBloodGroup());
        holder.textViewAddress.setText("Address : " + user.getAddress());

        final String contact = user.getPhone();

        holder.textViewMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+contact));
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewMobileNo, textViewBloodGroup, textViewAddress;
        LinearLayout linearLayout;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewMobileNo = itemView.findViewById(R.id.text_view_mobile);
            textViewBloodGroup = itemView.findViewById(R.id.text_view_bloodGroup);
            textViewAddress = itemView.findViewById(R.id.text_view_address);
            linearLayout = itemView.findViewById(R.id.recyclerView);
        }
    }
}

