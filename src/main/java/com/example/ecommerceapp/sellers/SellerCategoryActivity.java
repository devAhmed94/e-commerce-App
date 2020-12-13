package com.example.ecommerceapp.sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.ecommerceapp.R;

public class SellerCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView tshirts, sports, female_dresses , sweather,
            glasses, purses_bags, hats ,shoess ,
            headphoness , laptops, watches ,mobiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_category);

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


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.ad_category_iv_tshirts:
                 intent = new Intent(SellerCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "tshirts");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_sports:
                 intent = new Intent(SellerCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "sports");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_dress:
                intent = new Intent(SellerCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "female_dress");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_sweather:
                intent = new Intent(SellerCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "sweather");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_glasses:
                intent = new Intent(SellerCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "glasses");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_purses_bags:
                intent = new Intent(SellerCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "purses_bags");
                startActivity(intent);
                break;

            case R.id.ad_category_iv_hats:
                intent = new Intent(SellerCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "hats");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_shoess:
                intent = new Intent(SellerCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "shoess");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_headphoness:
                intent = new Intent(SellerCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "headphoness");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_laptops:
                intent = new Intent(SellerCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "laptops");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_watches:
                intent = new Intent(SellerCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "watches");
                startActivity(intent);
                break;
            case R.id.ad_category_iv_mobiles:
                intent = new Intent(SellerCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "mobiles");
                startActivity(intent);
                break;


        }

    }
}