package com.example.ecommerceapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ecommerceapp.HomeActivity;
import com.example.ecommerceapp.MainActivity;
import com.example.ecommerceapp.R;

public class AdminCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView tshirts, sports, female_dresses , sweather,
            glasses, purses_bags, hats ,shoess ,
            headphoness , laptops, watches ,mobiles;

    Button btn_check,btn_logout,btn_maintain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        btn_check=findViewById(R.id.ad_category_btn_check);
        btn_logout =findViewById(R.id.ad_category_btn_logout);
        btn_maintain =findViewById(R.id.ad_category_btn_maintain);

        tshirts =findViewById(R.id.ad_category_iv_tshirts);
        sports =findViewById(R.id.ad_category_iv_sports);
        female_dresses = findViewById(R.id.ad_category_iv_dress);
        sweather = findViewById(R.id.ad_category_iv_sweather);
        glasses = findViewById(R.id.ad_category_iv_glasses);
        purses_bags = findViewById(R.id.ad_category_iv_purses_bags);
        hats = findViewById(R.id.ad_category_iv_hats);
        shoess =findViewById(R.id.ad_category_iv_shoess);
        headphoness =findViewById(R.id.ad_category_iv_headphoness);
        laptops =findViewById(R.id.ad_category_iv_laptops);
        watches =findViewById(R.id.ad_category_iv_watches);
        mobiles =findViewById(R.id.ad_category_iv_mobiles);
            tshirts.setOnClickListener(this);
            sports.setOnClickListener(this);
            female_dresses.setOnClickListener(this);
            sweather.setOnClickListener(this);
            glasses.setOnClickListener(this);
            purses_bags.setOnClickListener(this);
            hats.setOnClickListener(this);
            shoess.setOnClickListener(this);
            headphoness.setOnClickListener(this);
            laptops.setOnClickListener(this);
            watches.setOnClickListener(this);
            mobiles.setOnClickListener(this);
            btn_check.setOnClickListener(this);
            btn_logout.setOnClickListener(this);
            btn_maintain.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.ad_category_iv_tshirts:
                 intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductsActivity.class);
                intent.putExtra("category", "tshirts");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_sports:
                 intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("category", "sports");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_dress:
                intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("category", "female_dress");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_sweather:
                intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("category", "sweather");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_glasses:
                intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("category", "glasses");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_purses_bags:
                intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("category", "purses_bags");
                startActivity(intent);
                break;

            case R.id.ad_category_iv_hats:
                intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("category", "hats");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_shoess:
                intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("category", "shoess");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_headphoness:
                intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("category", "headphoness");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_laptops:
                intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("category", "laptops");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_watches:
                intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("category", "watches");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_mobiles:
                intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("category", "mobiles");
                startActivity(intent);
                break;
            case R.id.ad_category_btn_check:
                intent =new Intent(AdminCategoryActivity.this, AdminNewOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.ad_category_btn_logout:
                intent =new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(intent);
                finish();
                break;
            case R.id.ad_category_btn_maintain:
                intent =new Intent(AdminCategoryActivity.this, HomeActivity.class);
                intent.putExtra("admin","admin");
                startActivity(intent);
                break;

        }

    }
}