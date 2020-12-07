package com.example.ecommerceapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.model.Cart;
import com.example.ecommerceapp.model.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProductsActivity extends AppCompatActivity {

    RecyclerView productsList;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);
        String uid = getIntent().getStringExtra("uid");
        reference = FirebaseDatabase.getInstance().getReference().child("cart list").child("admin view").child(uid).child("products");
        productsList = findViewById(R.id.products_recycler);
        productsList.setHasFixedSize(true);
        productsList.setLayoutManager(new LinearLayoutManager(AdminUserProductsActivity.this));

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(reference,Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_list_layout,parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.tv_name.setText(model.getName());
                holder.tv_quantity.setText("the quantity is " + model.getQuantity());
                holder.tv_price.setText(" the price is "+model.getPrice());

            }
        };

        productsList.setAdapter(adapter);
        adapter.startListening();
    }
}