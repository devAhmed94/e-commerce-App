package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ecommerceapp.model.Products;
import com.example.ecommerceapp.model.RecycleViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    EditText search_et;
    ImageView search_iv;
    RecyclerView searchList;
    private String inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchList = findViewById(R.id.searchActivity_recyclerView);
        search_et =findViewById(R.id.searchActivity_editText);
        search_iv =findViewById(R.id.searchActivity_icon);

        searchList.setHasFixedSize(true);
        searchList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch = search_et.getText().toString();
                onStart();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference searchRef = FirebaseDatabase.getInstance().getReference().child("products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(searchRef.orderByChild("name").startAt(inputSearch),Products.class).build();

        FirebaseRecyclerAdapter<Products, RecycleViewHolder> adapter = new FirebaseRecyclerAdapter<Products,RecycleViewHolder> (options) {
            @NonNull
            @Override
            public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new RecycleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull RecycleViewHolder holder, int position, @NonNull Products model) {
                holder.tv_productName.setText(model.getName());
                holder.tv_productPrice.setText(model.getPrice());
                holder.tv_productDes.setText(model.getDescription());
                Picasso.with(getApplicationContext()).load(model.getImage()).into(holder.iv_productImage);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchActivity.this,DetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }
        };

        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}