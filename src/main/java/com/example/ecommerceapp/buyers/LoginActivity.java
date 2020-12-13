package com.example.ecommerceapp.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.admin.AdminHomeActivity;
import com.example.ecommerceapp.sellers.SellerCategoryActivity;
import com.example.ecommerceapp.model.Prevalent;
import com.example.ecommerceapp.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    EditText et_phone,et_password;
    Button btn_login;
    TextView tv_admin,tv_not_admin;
    TextView tv_forget_password;
    CheckBox checkBox_remember;
    ProgressDialog progressDialog;
    public static  String parentDBRoot ="Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(LoginActivity.this);
        tv_forget_password = findViewById(R.id.login_forget_tv);
        et_phone = findViewById(R.id.login_phone_et);
        et_password = findViewById(R.id.login_password_et);
        btn_login = findViewById(R.id.login_login_btn);
        checkBox_remember = findViewById(R.id.login_checkbox);
        tv_admin = findViewById(R.id.login_admin_tv);
        tv_not_admin =findViewById(R.id.login_not_admin_tv);
        Paper.init(this);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        tv_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.setText(R.string.login_admins);
                tv_admin.setVisibility(View.INVISIBLE);
                tv_not_admin.setVisibility(View.VISIBLE);
                parentDBRoot ="Admins";
            }
        });

        tv_not_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.setText(R.string.login);
                tv_admin.setVisibility(View.VISIBLE);
                tv_not_admin.setVisibility(View.INVISIBLE);
                parentDBRoot ="Users";
            }
        });
        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });
    }

    private void loginUser() {



        String phone = et_phone.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(LoginActivity.this, "your phone is empty", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "your password is empty", Toast.LENGTH_LONG).show();
        }else{
            validation_login(phone,password);
        }
    }

    private void validation_login(String phone, String password) {
        progressDialog.setTitle("Login");
        progressDialog.setMessage("wait for login....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(checkBox_remember.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        DatabaseReference dataReference = FirebaseDatabase.getInstance().getReference();
        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDBRoot).child(phone).exists()){
                    Users user = snapshot.child(parentDBRoot).child(phone).getValue(Users.class);
                    if(user.getPhone().equals(phone)){
                        if (user.getPassword().equals(password)){
                            if(parentDBRoot.equals("Admins")){
                                progressDialog.dismiss();

                                Toast.makeText(LoginActivity.this, "successful login admin", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));

                            }else if(parentDBRoot.equals("Users")){
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "successful login user", Toast.LENGTH_LONG).show();
                                Prevalent.onlineUser = user;
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "your password incorrect", Toast.LENGTH_LONG).show();
                        }
                    }

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "not exist this phone number", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

    }
}