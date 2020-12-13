package com.example.ecommerceapp.sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductsActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 1;
    Button btn_addProduct;
    ImageView iv_addImage;
    EditText et_name, et_des, et_price;
    String category_name, ProductName, ProductDescription, ProductPrice;
    StorageReference storeRef;
    DatabaseReference dataRef, sellerRef;
    private Uri imageUri;
    private String saveCurrentDate;
    private String saveCurrentTime;
    private String productRandomKey;
    private String dowloadUrl;

    private String sellerName;
    private String sellerEmail;
    private String sellerPassword;
    private String sellerPhone;
    private String sellerAddress;
    private String sellerId;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_prodects);
        progressDialog = new ProgressDialog(SellerAddNewProductsActivity.this);


        storeRef = FirebaseStorage.getInstance().getReference("products Images");
        dataRef = FirebaseDatabase.getInstance().getReference("products");
        sellerRef = FirebaseDatabase.getInstance().getReference().child("seller");
        category_name = getIntent().getExtras().getString("category");
        Toast.makeText(this, category_name, Toast.LENGTH_LONG).show();
        btn_addProduct = findViewById(R.id.ad_new_product_btn_add_product);
        iv_addImage = findViewById(R.id.ad_new_product_iv_add_image);
        et_name = findViewById(R.id.ad_new_product_et_name);
        et_des = findViewById(R.id.ad_new_product_et_des);
        et_price = findViewById(R.id.ad_new_product_et_price);
        iv_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
        btn_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });
        sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    sellerName = snapshot.child("name").getValue().toString();
                    sellerEmail = snapshot.child("email").getValue().toString();
                    sellerPhone = snapshot.child("phone").getValue().toString();
                    sellerAddress= snapshot.child("address").getValue().toString();
                    sellerPassword= snapshot.child("password").getValue().toString();
                    sellerId   = snapshot.child("uid").getValue().toString();
                } else {
                    Toast.makeText(SellerAddNewProductsActivity.this, " the user is not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void validateProductData() {
        ProductName = et_name.getText().toString();
        ProductDescription = et_des.getText().toString();
        ProductPrice = et_price.getText().toString();
        if (imageUri == null) {
            Toast.makeText(this, "plz enter image of product", Toast.LENGTH_SHORT).show();
        } else if (ProductName == "" && TextUtils.isEmpty(ProductName)) {
            Toast.makeText(this, "plz enter name of product", Toast.LENGTH_SHORT).show();
        } else if (ProductDescription == "" && TextUtils.isEmpty(ProductDescription)) {
            Toast.makeText(this, "plz enter description of product", Toast.LENGTH_SHORT).show();
        } else if (ProductPrice == "" && TextUtils.isEmpty(ProductPrice)) {
            Toast.makeText(this, "plz enter price of product", Toast.LENGTH_SHORT).show();
        } else {
            storeInformation();
        }

    }

    private void storeInformation() {

        progressDialog.setTitle("Adding");
        progressDialog.setMessage("wait for adding....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat("MMM  dd , yyyy");
        saveCurrentDate = formatDate.format(calendar.getTime());

        SimpleDateFormat formatTime = new SimpleDateFormat("HH:MM:SS a");
        saveCurrentTime = formatTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;
        StorageReference filepath = storeRef.child(imageUri.getLastPathSegment() + productRandomKey);

        UploadTask uploadTask = filepath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SellerAddNewProductsActivity.this, "Error1 :" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String downloadUri = task.getResult().toString();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("pid", productRandomKey);
                    map.put("currentTime", saveCurrentTime);
                    map.put("currentDate", saveCurrentDate);
                    map.put("name", ProductName);
                    map.put("description", ProductDescription);
                    map.put("price", ProductPrice);
                    map.put("image", downloadUri);
                    map.put("category", category_name);

                    map.put("sellerName", sellerName);
                    map.put("sellerId", sellerId);
                    map.put("sellerAddress", sellerAddress);
                    map.put("sellerPhone", sellerPhone);
                    map.put("sellerEmail", sellerEmail);
                    map.put("sellerPassword", sellerPassword);
                    map.put("productState", "Not approved");

                    dataRef.child(productRandomKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(SellerAddNewProductsActivity.this, " product is added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SellerAddNewProductsActivity.this, SellerHomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);
                                finish();

                            } else {
                                progressDialog.dismiss();
                                String msg = task.getException().getMessage().toString();
                                Toast.makeText(SellerAddNewProductsActivity.this, " Error :" + msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(AdminAddNewProductsActivity.this, "image upload successful", Toast.LENGTH_SHORT).show();
//
//                Task<Uri> downloadTask = taskSnapshot.getStorage().getDownloadUrl();
//                downloadTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        dowloadUrl =uri.toString();
//                    }
//                });
//                HashMap<String ,Object> map = new HashMap<>();
//                map.put("pid", productRandomKey);
//                map.put("currentTime", saveCurrentTime);
//                map.put("currentDate", saveCurrentDate);
//                map.put("name", ProductName);
//                map.put("description", ProductDescription);
//                map.put("price", ProductPrice);
//                map.put("image", dowloadUrl);
//                map.put("category", category_name);
//                dataRef.child(productRandomKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            progressDialog.dismiss();
//                            Toast.makeText(AdminAddNewProductsActivity.this, " product is added", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(AdminAddNewProductsActivity.this,AdminCategoryActivity.class);
//                            startActivity(intent);
//
//                        }else {
//                            progressDialog.dismiss();
//                            String msg = task.getException().getMessage().toString();
//                            Toast.makeText(AdminAddNewProductsActivity.this, " Error :" + msg, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });

    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            iv_addImage.setImageURI(imageUri);
        }

    }


}