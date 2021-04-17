package com.example.uzvend.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uzvend.Models.SimpleVerticalModel;
import com.example.uzvend.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BasketVerticalAdapter extends RecyclerView.Adapter<BasketVerticalAdapter.PlateViewHolder> {

    private List<SimpleVerticalModel> simpleVerticalModelList;
    private Context context;

    public BasketVerticalAdapter(List<SimpleVerticalModel> simpleVerticalModelList, Context context) {
        this.simpleVerticalModelList = simpleVerticalModelList;
        this.context = context;
    }



    @NonNull
    @Override
    public PlateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basket_row,viewGroup,false);

        return  new PlateViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PlateViewHolder holder, int position) {
        SimpleVerticalModel simpleVerticalModel = simpleVerticalModelList.get(position);

        Glide.with(context).load(simpleVerticalModel.getPro_img()).into(holder.proImg);
        holder.pro_title.setText(simpleVerticalModel.getSimple_title());
        holder.pro_desc.setText(simpleVerticalModel.getSimple_description());
        holder.pro_quantity.setText(simpleVerticalModel.getSimple_quantity());
        //holder.pro_coupon.setText(simpleVerticalModel.getSimple_coupon());
        holder.pro_status.setText("Quantity: "+simpleVerticalModel.getCart_qty());
        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFromBasket(simpleVerticalModel.getId());
                simpleVerticalModelList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return simpleVerticalModelList.size();
    }

    public class PlateViewHolder extends RecyclerView.ViewHolder {

        private ImageView proImg;
        private TextView pro_title, pro_desc,pro_quantity,pro_coupon,pro_status,pro_rating;
        private ImageView trash;

        public PlateViewHolder(@NonNull View itemView)
        {
            super(itemView);
            proImg = (ImageView) itemView.findViewById(R.id.imageView5);
            trash = (ImageView) itemView.findViewById(R.id.trash);
            pro_title = (TextView) itemView.findViewById(R.id.textView3);
            pro_desc = (TextView) itemView.findViewById(R.id.textView8);
            pro_quantity = (TextView) itemView.findViewById(R.id.textView9);
            //pro_coupon = (TextView) itemView.findViewById(R.id.textView10);
            pro_status = (TextView) itemView.findViewById(R.id.textView11);
            //pro_rating = (TextView) itemView.findViewById(R.id.textView12);

        }
    }

    void removeFromBasket(String id){

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("basket", "");
        Type type = new TypeToken<List<SimpleVerticalModel>>(){}.getType();
        List<SimpleVerticalModel> basket = gson.fromJson(json, type);

        if(basket == null){
            basket = new ArrayList<>();
        }

        if(basket.size()>0){
            for (int i=0;i<basket.size();i++){
                if (basket.get(i).getId().equals(id)){
                    basket.remove(i);
                }
            }
        }

        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        json = gson.toJson(basket);
        prefsEditor.putString("basket", json);
        prefsEditor.apply();

    }
}

