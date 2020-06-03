package com.example.whatscooking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context mContext;
    private ArrayList<GridItem> mGridList;

    public Adapter(Context context, ArrayList<GridItem> gridList){
        mContext = context;
        mGridList = gridList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GridItem currentItem = mGridList.get(position);

        String imageUrl = currentItem.getmImage();
        String title = currentItem.getmTitle();
        int quantity = currentItem.getmQuantity();
        int calories = currentItem.getmCalories();

        holder.mGridTitle.setText(title);
        holder.mGridQuantity.setText("Quantity: " + quantity);
        holder.mGridCalories.setText("Calories: " + calories);
        Picasso.get().load(imageUrl).into(holder.mGridImage);
    }

    @Override
    public int getItemCount() {
        return mGridList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mGridImage;
        public TextView mGridTitle;
        public TextView mGridQuantity;
        public TextView mGridCalories;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mGridImage = itemView.findViewById(R.id.gridImage);
            mGridTitle = itemView.findViewById(R.id.gridTitle);
            mGridQuantity = itemView.findViewById(R.id.gridQuantity);
            mGridCalories = itemView.findViewById(R.id.gridCalories);
        }
    }
}
