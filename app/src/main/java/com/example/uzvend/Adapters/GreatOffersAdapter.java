package com.example.uzvend.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uzvend.Models.GreatOffersModel;
import com.example.uzvend.R;

import java.util.List;

public class GreatOffersAdapter extends RecyclerView.Adapter<GreatOffersAdapter.GreatOfferViewHolder> {

    List<GreatOffersModel> greatOffersModelList;
    Context context;

    public GreatOffersAdapter(List<GreatOffersModel> greatOffersModelList, Context context) {
        this.greatOffersModelList = greatOffersModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public GreatOfferViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_horizontal_great_offers,viewGroup,false);
        return  new GreatOfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GreatOfferViewHolder holder, int position) {
        GreatOffersModel greatOffersModel = greatOffersModelList.get(position);

        Glide.with(context).load(greatOffersModel.getShop_img()).placeholder(R.drawable.dress2).into(holder.shopImg);


        holder.shop_name.setText(greatOffersModel.getShop_img());
        holder.discount.setText(greatOffersModel.getDiscount());
        holder.rating.setText(greatOffersModel.getRating());
        holder.time.setText(greatOffersModel.getTime());

    }

    @Override
    public int getItemCount() {
        return greatOffersModelList.size();
    }

    public class GreatOfferViewHolder extends RecyclerView.ViewHolder {

        private ImageView shopImg;
        private TextView shop_name,discount,rating,time;
        public GreatOfferViewHolder(@NonNull View itemView) {
            super(itemView);


            shopImg = (ImageView) itemView.findViewById(R.id.imageView6);
            shop_name = (TextView) itemView.findViewById(R.id.textView13);
            discount = (TextView) itemView.findViewById(R.id.textView15);
            rating = (TextView) itemView.findViewById(R.id.textView16);
            time = (TextView) itemView.findViewById(R.id.textView14);
        }
    }
}
