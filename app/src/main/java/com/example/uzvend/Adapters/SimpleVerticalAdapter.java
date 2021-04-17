package com.example.uzvend.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uzvend.Models.SimpleVerticalModel;
import com.example.uzvend.ProductView;
import com.example.uzvend.R;
import com.example.uzvend.Utils;

import java.util.List;

public class SimpleVerticalAdapter extends RecyclerView.Adapter<SimpleVerticalAdapter.PlateViewHolder> {

    private List<SimpleVerticalModel> simpleVerticalModelList;
    private Context context;

    public SimpleVerticalAdapter(List<SimpleVerticalModel> simpleVerticalModelList, Context context) {
        this.simpleVerticalModelList = simpleVerticalModelList;
        this.context = context;
    }



    @NonNull
    @Override
    public PlateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vertical_slider,viewGroup,false);

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
        holder.pro_status.setText(simpleVerticalModel.getSimple_status());
        //holder.pro_rating.setText(simpleVerticalModel.getSimple_rating());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductView.class);
                intent.putExtra("id", simpleVerticalModel.getId());
                intent.putExtra("image", simpleVerticalModel.getPro_img());
                intent.putExtra("title", simpleVerticalModel.getSimple_title());
                intent.putExtra("desc", simpleVerticalModel.getSimple_description());
                intent.putExtra("price", simpleVerticalModel.getSimple_quantity());
                intent.putExtra("status", simpleVerticalModel.getSimple_status());
                Utils.shared_model = simpleVerticalModel;
                context.startActivity(intent);
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

        public PlateViewHolder(@NonNull View itemView)
        {
            super(itemView);
            proImg = (ImageView) itemView.findViewById(R.id.imageView5);
            pro_title = (TextView) itemView.findViewById(R.id.textView3);
            pro_desc = (TextView) itemView.findViewById(R.id.textView8);
            pro_quantity = (TextView) itemView.findViewById(R.id.textView9);
            //pro_coupon = (TextView) itemView.findViewById(R.id.textView10);
            pro_status = (TextView) itemView.findViewById(R.id.textView11);
            //pro_rating = (TextView) itemView.findViewById(R.id.textView12);

        }
    }
}

