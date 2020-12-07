package com.example.ecommerceapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.model.Orders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {

    RecyclerView orderList;
    private String uID;
    private DatabaseReference dateRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);
        orderList =findViewById(R.id.products_recycler);
        orderList.setLayoutManager(new LinearLayoutManager(AdminNewOrderActivity.this));
        orderList.setHasFixedSize(true);

    }

    @Override
    protected void onStart() {
        super.onStart();

        dateRef = FirebaseDatabase.getInstance().getReference().child("orders");
        FirebaseRecyclerOptions<Orders> options =new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(dateRef,Orders.class)
                .build();
        FirebaseRecyclerAdapter<Orders,OrderViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list_layout,parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Orders model) {
                holder.tv_name.setText(" userName : "+model.getName());
                holder.tv_phone.setText(" phone : "+model.getPhone());
                holder.tv_totalPrice.setText(" the total price ="+model.getTotalPrice());
                holder.tv_addressCity.setText(model.getAddress() +" "+model.getCityName());
                holder.tv_dateTime.setText(model.getDate() +" "+model.getTime());
                holder.btn_show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        uID =getRef(position).getKey();
                        Intent intent = new Intent(AdminNewOrderActivity.this, AdminUserProductsActivity.class);
                        intent.putExtra("uid",uID);
                        startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrderActivity.this);
                        builder.setTitle("Have you shipped this order products ?");
                        CharSequence[] choose = new CharSequence[]{
                                "Yes" ,"No"
                        };
                        builder.setItems(choose, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    dateRef.child(uID).removeValue();
                                }else {
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }
        };

        orderList.setAdapter(adapter);
        adapter.startListening();
    }


   private static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,tv_phone,tv_totalPrice,tv_addressCity,tv_dateTime;
        Button btn_show;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.item_orders_name);
            tv_phone = itemView.findViewById(R.id.item_orders_phone);
            tv_totalPrice = itemView.findViewById(R.id.item_orders_totalPrice);
            tv_addressCity = itemView.findViewById(R.id.item_orders_addressCity);
            tv_dateTime = itemView.findViewById(R.id.item_orders_dateTime);
            btn_show =itemView.findViewById(R.id.item_orders_btn_show);

        }
    }
}