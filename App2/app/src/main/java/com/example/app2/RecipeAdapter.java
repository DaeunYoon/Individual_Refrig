package com.example.app2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.CustomViewHolder> {

    private ArrayList<Recipe> arrayList;
    private Context context;
    public static String TAG = "Refrig";


    @NonNull
    @Override
    public RecipeAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.CustomViewHolder holder, final int position) {
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_content.setText(Integer.toString(arrayList.get(position).getMin()));
        //click or long click setOnClickListener function

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click: "+position);
                Recipe selected = arrayList.get(position);

                Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
                intent.putExtra("RECIPE_NAME", selected.getName());
                view.getContext().startActivity(intent);
                return;
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public RecipeAdapter(Context context, ArrayList<Recipe> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_name;
        protected TextView tv_content;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_name = itemView.findViewById(R.id.name);
            this.tv_content = itemView.findViewById(R.id.content);
        }
    }

    void updateData(ArrayList<Recipe> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }
}
