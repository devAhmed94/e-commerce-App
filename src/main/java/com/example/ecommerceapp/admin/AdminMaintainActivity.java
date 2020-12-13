package com.example.ecommerceapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.sellers.SellerCategoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainActivity extends AppCompatActivity {

    ImageView image_maintain;
    EditText et_name_maintain,et_des_maintain,et_price_maintain;
    Button btn_apply,btn_delete;
    private String productID;
    private DatabaseReference productRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain);

        productID = getIntent().getStringExtra("pid");
        productRef = FirebaseDatabase.getInstance().getReference().child("products").child(productID);

        image_maintain = findViewById(R.id.maintain_imageView);
        et_name_maintain = findViewById(R.id.maintain_et_name);
        et_price_maintain = findViewById(R.id.maintain_et_price);
        et_des_maintain = findViewById(R.id.maintain_et_description);
        btn_apply =findViewById(R.id.maintain_btn_apply);
        btn_delete =findViewById(R.id.maintain_btn_delete);

        displayInfoItem();
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataProduct();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AdminMaintainActivity.this," deleted " ,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminMaintainActivity.this, SellerCategoryActivity.class);
                            startActivity(intent);

                        }
                    }
                });
            }
        });
    }

    private void updateDataProduct() {
        if(et_name_maintain.getText().toString().isEmpty())
        {
            Toast.makeText(AdminMaintainActivity.this," name is empty" ,Toast.LENGTH_SHORT).show();
        }else if (et_price_maintain.getText().toString().isEmpty())
        {
            Toast.makeText(AdminMaintainActivity.this," price is empty" ,Toast.LENGTH_SHORT).show();

        }else if (et_des_maintain.getText().toString().isEmpty())
        {
            Toast.makeText(AdminMaintainActivity.this," description is empty" ,Toast.LENGTH_SHORT).show();
        }else {

            HashMap<String ,Object> map =new HashMap<>();
            map.put("pid", productID);
            map.put("name", et_name_maintain.getText().toString());
            map.put("description", et_des_maintain.getText().toString());
            map.put("price", et_price_maintain.getText().toString());

            productRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        Toast.makeText(AdminMaintainActivity.this," update successful" ,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainActivity.this, SellerCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }




    }

    private void displayInfoItem() {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String pName = snapshot.child("name").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String  pDes = snapshot.child("description").getValue().toString();
                    String  pImage = snapshot.child("image").getValue().toString();

                    Picasso.with(AdminMaintainActivity.this).load(pImage).into(image_maintain);
                    et_name_maintain.setText(pName);
                    et_price_maintain.setText(pPrice);
                    et_des_maintain.setText(pDes);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}