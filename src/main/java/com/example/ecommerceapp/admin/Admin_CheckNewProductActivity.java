package com.example.ecommerceapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.interfacePackage.ItemClickListener;
import com.example.ecommerceapp.model.Products;
import com.example.ecommerceapp.model.RecycleViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Admin_CheckNewProductActivity extends AppCompatActivity {

    RecyclerView recycle_admin_check;
    DatabaseReference approveRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_product);
        approveRef = FirebaseDatabase.getInstance().getReference().child("products");
        recycle_admin_check =findViewById(R.id.admin_check_new_product_recycleView);
        recycle_admin_check.setLayoutManager(new LinearLayoutManager(Admin_CheckNewProductActivity.this));
        recycle_admin_check.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products>options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(approveRef.orderByChild("productState").equalTo("Not approved"),Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, RecycleViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Products, RecycleViewHolder>(options) {
                    @NonNull
                    @Override
                    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v =getLayoutInflater().inflate(R.layout.product_item_layout,parent,false);
                        return new RecycleViewHolder(v);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull RecycleViewHolder holder, int position, @NonNull Products model) {
                        holder.tv_productName.setText(model.getName());
                        holder.tv_productDes.setText(model.getDescription());
                        holder.tv_productPrice.setText(model.getPrice());
                        Picasso.with(Admin_CheckNewProductActivity.this).load(model.getImage()).into(holder.iv_productImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String productId = model.getPid();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_CheckNewProductActivity.this);
                                builder.setTitle("Do you want approved this product");
                                builder.setCancelable(false);
                                CharSequence [] option_items = new CharSequence[]{"Yes","No"};
                                builder.setItems(option_items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which==0)
                                        {
                                            approveProductMethod(productId);

                                        }else if (which==1){

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                };
        recycle_admin_check.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void approveProductMethod(String productId)
    {
        approveRef.child(productId).child("productState").
                setValue("approved").
                addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Admin_CheckNewProductActivity.this, "That item has been approved , done", Toast.LENGTH_SHORT).show();

                }
                
            }
        });

    }
}