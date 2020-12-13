package com.example.ecommerceapp.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.interfacePackage.ItemClickListener;

public class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tv_productName ,tv_productPrice,tv_productDes,tv_doller;
    public ImageView iv_productImage;
    private ItemClickListener listener;

    public RecycleViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_productName = itemView.findViewById(R.id.product_item_name);
        tv_productPrice = itemView.findViewById(R.id.product_item_price);
        tv_productDes = itemView.findViewById(R.id.product_item_des);
        tv_doller = itemView.findViewById(R.id.doller);
        iv_productImage =itemView.findViewById(R.id.product_item_image);

    }


    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }





    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
