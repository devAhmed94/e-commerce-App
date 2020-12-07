package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button btn_create;
    EditText et_name,et_phone,et_password;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog =new ProgressDialog(RegisterActivity.this);
        et_name = findViewById(R.id.register_name_et);
        et_phone = findViewById(R.id.register_phone_et);
        et_password = findViewById(R.id.register_password_et);
        btn_create = findViewById(R.id.register_create_btn);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {


        String name = et_name.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(RegisterActivity.this, "name is empty ", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phone)){
            Toast.makeText(RegisterActivity.this, "phone is empty ", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this, "password is empty ", Toast.LENGTH_SHORT).show();
        }else {
            validationAccount(name,phone,password);
        }

    }

    private void validationAccount(String name, String phone, String password) {
        progressDialog.setTitle("Creation");
        progressDialog.setMessage("wait for register your account ....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        DatabaseReference dataReference = FirebaseDatabase.getInstance().getReference();
        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(phone)).exists()){
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("phone", phone);
                    map.put("password", password);
                    dataReference.child("Users").child(phone).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, " Congratulation, Account is created", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, " Network error", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                            }
                        }
                    });


                }else{
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "this "+ phone+" is already exists", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }
}