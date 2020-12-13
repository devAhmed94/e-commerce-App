package com.example.ecommerceapp.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.interfacePackage.ItemClickListener;


public class SellerViewHolder extends RecyclerView.ViewHolder {

    public TextView seller_tv_productName ,seller_tv_productPrice,seller_tv_productDes,seller_tv_productStatus;
    public ImageView seller_iv_productImage;

    public SellerViewHolder(@NonNull View itemView) {
        super(itemView);
        seller_tv_productName = itemView.findViewById(R.id.seller_item_name);
        seller_tv_productPrice = itemView.findViewById(R.id.seller_item_price);
        seller_tv_productDes = itemView.findViewById(R.id.seller_item_des);
        seller_tv_productStatus = itemView.findViewById(R.id.seller_item_status);
        seller_iv_productImage= itemView.findViewById(R.id.seller_item_image);
    }
}
