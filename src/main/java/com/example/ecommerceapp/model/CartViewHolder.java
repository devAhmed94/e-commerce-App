package com.example.ecommerceapp.model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.interfacePackage.ItemClickListener;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public   TextView tv_name,tv_price,tv_quantity;
    ItemClickListener listener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_name =itemView.findViewById(R.id.item_orders_name);
        tv_price =itemView.findViewById(R.id.item_orders_phone);
        tv_quantity =itemView.findViewById(R.id.item_cart_totalPrice);

    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }


    public void setItemClickListener(ItemClickListener listener){
        this.listener =listener;
    }
}
