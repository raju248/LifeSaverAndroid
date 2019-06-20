package com.example.sdproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdapterOfRequest extends RecyclerView.Adapter<AdapterOfRequest.RequestViewHolder> {

    private Context mCtx;
    private List<BloodRequest> user;

    public AdapterOfRequest(Context mCtx, List<BloodRequest> requestList) {
        this.mCtx = mCtx;
        this.user = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        BloodRequest user = this.user.get(position);
        holder.textViewName.setText("Requested By : "+ user.getName());
        holder.textViewMobileNo.setText("Mobile No : " + user.getMobile());
        holder.textViewBloodGroup.setText("Required Blood Group : " + user.getBloodGroup());
        holder.textViewAddress.setText("Address : " + user.getAddress());

        if(user.getComment().equals("No comment"))
        {
            //holder.textViewComment.setVisibility(View.INVISIBLE);
            holder.textViewComment.setText("");
        }
        else
            holder.textViewComment.setText("Comment : "+user.getComment());


        final String contact = user.getMobile();

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

    class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewMobileNo, textViewBloodGroup, textViewAddress, textViewComment;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewMobileNo = itemView.findViewById(R.id.text_view_mobile);
            textViewBloodGroup = itemView.findViewById(R.id.text_view_bloodGroup);
            textViewAddress = itemView.findViewById(R.id.text_view_address);
            textViewComment = itemView.findViewById(R.id.text_view_comment);
        }
    }
}

