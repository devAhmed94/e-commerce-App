package com.example.ecommerceapp.sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.buyers.MainActivity;
import com.example.ecommerceapp.model.Products;
import com.example.ecommerceapp.model.SellerViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SellerHomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView seller_home_recycle;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        reference = FirebaseDatabase.getInstance().getReference().child("products");
        seller_home_recycle = findViewById(R.id.seller_home_recycle);
        seller_home_recycle.setHasFixedSize(true);
        seller_home_recycle.setLayoutManager(new LinearLayoutManager(SellerHomeActivity.this));
        bottomNavigationView = findViewById(R.id.seller_home_bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:

                        break;
                    case R.id.nav_add:

                        Intent cate_intent = new Intent(SellerHomeActivity.this, SellerCategoryActivity.class);

                        startActivity(cate_intent);

                        break;
                    case R.id.nav_logout:
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("sellerId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, SellerViewHolder> adapter = new FirebaseRecyclerAdapter<Products, SellerViewHolder>(options) {
            @NonNull
            @Override
            public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new SellerViewHolder(getLayoutInflater().inflate(R.layout.seller_item_layout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull SellerViewHolder holder, int position, @NonNull Products model) {
                holder.seller_tv_productName.setText(model.getName());
                holder.seller_tv_productPrice.setText(model.getPrice());
                holder.seller_tv_productDes.setText(model.getDescription());
                holder.seller_tv_productStatus.setText("Status :" + model.getProductState());
                Picasso.with(SellerHomeActivity.this).load(model.getImage()).into(holder.seller_iv_productImage);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SellerHomeActivity.this);
                        builder.setTitle("Do you need delete this product? Are you sure?");
                        builder.setCancelable(false);
                        CharSequence[] items = new CharSequence[]{"Yes", "No"};
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    reference.child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(SellerHomeActivity.this, "deleted Done", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else if (which == 1) {

                                }
                            }
                        });
                        builder.show();
                    }
                });
            }
        };
        seller_home_recycle.setAdapter(adapter);
        adapter.startListening();

    }
}