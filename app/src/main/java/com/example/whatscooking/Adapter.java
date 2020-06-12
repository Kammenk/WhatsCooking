package com.example.whatscooking;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context mContext;
    private ArrayList<GridItem> mGridList;
    private int mFragmentNum;


    public Adapter(Context context, ArrayList<GridItem> gridList, int fragmentNum){
        mContext = context;
        mGridList = gridList;
        mFragmentNum = fragmentNum;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (mFragmentNum == 1) {
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
        } else if(mFragmentNum == 2) {
            view = LayoutInflater.from(mContext).inflate(R.layout.searchview_list_item, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.favorite_item, parent, false);
        }

        System.out.println("FRAGMENT NUM: " + mFragmentNum);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLICKED: " + viewHolder.getAdapterPosition());
                Intent intent = new Intent(view.getContext(),DetailActivity.class);
                intent.putExtra("Image",mGridList.get(viewHolder.getAdapterPosition()).getmImage());
                intent.putExtra("Title",mGridList.get(viewHolder.getAdapterPosition()).getmTitle());
                intent.putExtra("Quantity",mGridList.get(viewHolder.getAdapterPosition()).getmQuantity());
                intent.putExtra("Calories",mGridList.get(viewHolder.getAdapterPosition()).getmCalories());
                intent.putExtra("dietLabel",mGridList.get(viewHolder.getAdapterPosition()).getmDietLabel());
                intent.putExtra("healthLabel",mGridList.get(viewHolder.getAdapterPosition()).getmHealthLabel());
                intent.putExtra("ingredients",mGridList.get(viewHolder.getAdapterPosition()).getmIngredients());
                intent.putExtra("totalTime",mGridList.get(viewHolder.getAdapterPosition()).getmTotalTime());
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int cookTimeTopMinimum = 60;
        int cookTimeBottomMinimum = 0;

        GridItem currentItem = mGridList.get(position);

        String imageUrl = currentItem.getmImage();
        System.out.println("IMAGEURL  " + imageUrl);
        String title = currentItem.getmTitle();
        int quantity = currentItem.getmQuantity();
        int calories = currentItem.getmCalories();
        String dietLabel = currentItem.getmDietLabel();
        String healthLabel = currentItem.getmHealthLabel();
        String ingredients = currentItem.getmIngredients();
        int totalTime = currentItem.getmTotalTime();

        holder.mGridTitle.setText(title);
        holder.mGridQuantity.setText("Servings: " + quantity);
        holder.mGridCalories.setText("Calories: " + calories);
        holder.mGridDietLabel.setText("Diet labels: " + dietLabel);
        holder.mGridHealthLabel.setText("Health labels: " + healthLabel);
        holder.mGridIngredients.setText("Ingredients: " + ingredients);

        if (totalTime > cookTimeTopMinimum){
            holder.mGridTotalTime.setText("Cook time: Over 60 minutes");
        } else if(totalTime <= cookTimeBottomMinimum) {
            holder.mGridTotalTime.setText("Cook time: Not measured");
        } else {
            holder.mGridTotalTime.setText("Cook time: " + totalTime + " minutes");
        }

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mGridImage);
    }

    @Override
    public int getItemCount() {
        return mGridList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mGridImage;
        TextView mGridTitle;
        TextView mGridQuantity;
        TextView mGridCalories;
        TextView mGridDietLabel;
        TextView mGridHealthLabel;
        TextView mGridIngredients;
        TextView mGridTotalTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mGridImage = itemView.findViewById(R.id.gridImage);
            mGridTitle = itemView.findViewById(R.id.gridTitle);
            mGridQuantity = itemView.findViewById(R.id.gridQuantity);
            mGridCalories = itemView.findViewById(R.id.gridCalories);
            mGridDietLabel = itemView.findViewById(R.id.gridDietLabel);
            mGridHealthLabel = itemView.findViewById(R.id.gridHealthLabel);
            mGridIngredients = itemView.findViewById(R.id.gridIngredients);
            mGridTotalTime = itemView.findViewById(R.id.gridTotalTime);

        }
    }
}
