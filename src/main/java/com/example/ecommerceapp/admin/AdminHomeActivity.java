package com.example.ecommerceapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.buyers.HomeActivity;
import com.example.ecommerceapp.buyers.MainActivity;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {

        Button btn_check,btn_logout,btn_maintain ,btn_approve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        btn_check=findViewById(R.id.ad_home_btn_check);
        btn_logout =findViewById(R.id.ad_home_btn_logout);
        btn_maintain =findViewById(R.id.ad_home_btn_maintain);
        btn_approve =findViewById(R.id.ad_home_btn_check_approve);
        btn_check.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        btn_maintain.setOnClickListener(this);
        btn_approve.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.ad_home_btn_check:
                intent =new Intent(AdminHomeActivity.this, AdminNewOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.ad_home_btn_check_approve:
                intent =new Intent(AdminHomeActivity.this, Admin_CheckNewProductActivity.class);
                startActivity(intent);
                break;
            case R.id.ad_home_btn_logout:
                intent =new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(intent);
                finish();
                break;
            case R.id.ad_home_btn_maintain:
                intent =new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("admin","admin");
                startActivity(intent);
                break;
        }
    }
}