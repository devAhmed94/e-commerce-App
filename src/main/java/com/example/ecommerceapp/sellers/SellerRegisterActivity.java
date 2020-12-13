package com.example.ecommerceapp.sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegisterActivity extends AppCompatActivity {

    Button seller_already_btn, seller_register_btn;
    EditText et_name, et_phone, et_email, et_password, et_address;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);
        progressDialog = new ProgressDialog(SellerRegisterActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        seller_already_btn = findViewById(R.id.seller_btn_already_have_account);
        seller_register_btn = findViewById(R.id.seller_btn_register);
        et_email = findViewById(R.id.seller_et_email);
        et_name = findViewById(R.id.seller_et_name);
        et_phone = findViewById(R.id.seller_et_phone);
        et_password = findViewById(R.id.seller_et_password);
        et_address = findViewById(R.id.seller_et_address);
        seller_already_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerRegisterActivity.this, SellerLoginActivity.class));
            }
        });
        seller_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerMethod();
            }
        });
    }

    private void registerMethod() {


        String name = et_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String address = et_address.getText().toString().trim();

        if (!name.equals("") && !email.equals("") && !phone.equals("") && !password.equals("") && !address.equals("")) {
            progressDialog.setTitle("Creation");
            progressDialog.setMessage("wait for register your account ....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        String uid = firebaseAuth.getCurrentUser().getUid();
                        HashMap<String, Object> sellerMap = new HashMap<>();
                        sellerMap.put("uid", uid);
                        sellerMap.put("name", name);
                        sellerMap.put("email", email);
                        sellerMap.put("phone", phone);
                        sellerMap.put("password", password);
                        sellerMap.put("address", address);
                        reference.child("seller").child(uid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SellerRegisterActivity.this, " you done registration", Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(SellerRegisterActivity.this,SellerHomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SellerRegisterActivity.this, "not successful", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(SellerRegisterActivity.this, "not complete", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "plz complete your information", Toast.LENGTH_SHORT).show();
        }
    }
}