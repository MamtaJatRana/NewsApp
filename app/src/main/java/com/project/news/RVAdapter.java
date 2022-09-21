package com.project.news;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>
        {
           Context context;
           ArrayList<RVModel> arrayList=new ArrayList<>();
            public RVAdapter(MainActivity mainActivity, ArrayList<RVModel> rvlist) {
                context=mainActivity;
                arrayList=rvlist;
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rvitem, parent, false);

                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RVAdapter.ViewHolder holder, int position) {
                RVModel model=arrayList.get(position);
                holder.desc.setText(model.getDescription());
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context,NewsDetails.class);
                        intent.putExtra("url",model.getUrl());
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public int getItemCount() {
                Log.e("size",""+arrayList.size());
                return arrayList.size();
            }

            public class ViewHolder extends RecyclerView.ViewHolder {
                TextView desc;
                LinearLayout container;
                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    desc=itemView.findViewById(R.id.desc);
                    container=itemView.findViewById(R.id.container);
                }
            }
        }
