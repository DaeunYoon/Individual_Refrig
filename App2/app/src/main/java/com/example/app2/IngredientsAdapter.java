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

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.CustomViewHolder> {

    private ArrayList<Ingredient> arrayList;
    private Context context;
    public static String TAG = "Refrig";


    @NonNull
    @Override
    public IngredientsAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.CustomViewHolder holder, final int position) {
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_content.setText(Integer.toString(arrayList.get(position).getAmount()));
        //click or long click setOnClickListener function

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("LongClick: "+position);
                Ingredient selected = arrayList.get(position);

                Intent intent = new Intent(view.getContext(), EditActivity.class);
                intent.putExtra("INGREDIENT_NAME", selected.getName());
                intent.putExtra("INGREDIENT_AMOUNT", selected.getAmount());
                intent.putExtra("INGREDIENT_UNIT", selected.getUnit());
                view.getContext().startActivity(intent);
                return true;// returning true instead of false, works for me
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public IngredientsAdapter(Context context, ArrayList<Ingredient> arrayList) {
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

    void updateData(ArrayList<Ingredient> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }
}
