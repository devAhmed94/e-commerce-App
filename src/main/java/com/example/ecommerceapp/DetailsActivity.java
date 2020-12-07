package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerceapp.model.Prevalent;
import com.example.ecommerceapp.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {

    ImageView iv_imageProduct;
    TextView tv_productName,tv_productPrice,tv_productDes;
    ElegantNumberButton elegant_btn;
    Button btn_addToCart;
    private String productID ="" , orderState ="normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

         productID = getIntent().getStringExtra("pid");

        iv_imageProduct =findViewById(R.id.details_iv_imageProduct);
        tv_productName =findViewById(R.id.details_tv_nameProduct);
        tv_productPrice =findViewById(R.id.details_tv_priceProduct);
        tv_productDes =findViewById(R.id.details_tv_desProduct);
        elegant_btn =findViewById(R.id.details_eBtn_counter);
        btn_addToCart =findViewById(R.id.details_btn_addToCart);
        btn_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderState.equals("order placed") ||orderState.equals("order shipped")){
                    Toast.makeText(DetailsActivity.this," the order will be place soon ",Toast.LENGTH_LONG).show();

                }else {
                    onAddToCart();
                }
            }
        });
        showDetailsOfProduct(productID);
    }


    @Override
    protected void onStart() {
        super.onStart();
        stateOrdersMethod();
    }

    private void onAddToCart() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleFormatDate = new SimpleDateFormat("MM :dd,yyyy");
        String saveCurrentDate = simpleFormatDate.format(calendar.getTime());
        SimpleDateFormat simpleFormatTime = new SimpleDateFormat("HH :mm:ss a");
        String saveCurrentTime = simpleFormatTime.format(calendar.getTime());


       final DatabaseReference dateRefCartList = FirebaseDatabase.getInstance().getReference().child("cart list");
        HashMap<String ,Object> map = new HashMap<>();
        map.put("pid", productID);
        map.put("time", saveCurrentTime);
        map.put("date", saveCurrentDate);
        map.put("name", tv_productName.getText().toString());
        map.put("price", tv_productPrice.getText().toString());
        map.put("des", tv_productDes.getText().toString());
        map.put("quantity", elegant_btn.getNumber());
        map.put("discount", "");
        dateRefCartList.child("user view").child(Prevalent.onlineUser.getPhone()).child("products").child(productID)
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if ((task.isSuccessful())){
                            dateRefCartList.child("admin view").child(Prevalent.onlineUser.getPhone()).child("products").child(productID)
                                    .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(DetailsActivity.this,"added this product to list cart",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(DetailsActivity.this,HomeActivity.class));
                                    }
                                }
                            });
                        }
                    }
                });

    }

    private void showDetailsOfProduct(String productID) {
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("products");
        dRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Products productObject = snapshot.getValue(Products.class);
                    tv_productName.setText(productObject.getName());
                    tv_productPrice.setText(productObject.getPrice());
                    tv_productDes.setText(productObject.getDescription());
                    Picasso.with(DetailsActivity.this).load(productObject.getImage()).into(iv_imageProduct);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void stateOrdersMethod()
    {
        DatabaseReference stateRef = FirebaseDatabase.getInstance().getReference()
                .child("orders").child(Prevalent.onlineUser.getPhone());

        stateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists()){
                    String shippedState = snapshot.child("state").getValue().toString();
                    String userName = snapshot.child("name").getValue().toString();

                    if(shippedState.equals("shipment"))
                    {
                      orderState ="order placed";
                    }
                    else if(shippedState.equals("not shipment"))
                    {
                        orderState ="order shipped";

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}