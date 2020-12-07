package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerceapp.model.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalActivity extends AppCompatActivity {

    EditText et_name,et_phone,et_address,et_cityName;
    Button btn_confirm;
    String totalPrice ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final);
         totalPrice = getIntent().getStringExtra("total price");
         Toast.makeText(ConfirmFinalActivity.this,totalPrice, Toast.LENGTH_LONG).show();

        et_name =findViewById(R.id.confirm_et_name);
        et_phone =findViewById(R.id.confirm_et_phone);
        et_address =findViewById(R.id.confirm_et_address);
        et_cityName =findViewById(R.id.confirm_et_cityName);
        btn_confirm =findViewById(R.id.confirm_btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationFields();
            }
        });



    }

    private void validationFields()
    {
        if(TextUtils.isEmpty(et_name.getText())){
            Toast.makeText(ConfirmFinalActivity.this, " the name is empty",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(et_phone.getText().toString())&&!TextUtils.isDigitsOnly(et_phone.getText())){

            Toast.makeText(ConfirmFinalActivity.this, " check your phone number",Toast.LENGTH_LONG).show();

        }else if (TextUtils.isEmpty(et_address.getText())){
            Toast.makeText(ConfirmFinalActivity.this, " check your address",Toast.LENGTH_LONG).show();
        }else if ((TextUtils.isEmpty(et_cityName.getText().toString()))){
            Toast.makeText(ConfirmFinalActivity.this, " check your city name",Toast.LENGTH_LONG).show();
        }else {
            confirmMethod();
        }
    }

    private void confirmMethod()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleFormatDate = new SimpleDateFormat("MM :dd,yyyy");
        String saveCurrentDate = simpleFormatDate.format(calendar.getTime());
        SimpleDateFormat simpleFormatTime = new SimpleDateFormat("HH :mm:ss a");
        String saveCurrentTime = simpleFormatTime.format(calendar.getTime());

        DatabaseReference conRef = FirebaseDatabase.getInstance().getReference().child("orders")
                .child(Prevalent.onlineUser.getPhone());

        HashMap<String , Object> map = new HashMap<>();
        map.put("totalPrice", totalPrice);
        map.put("name", et_name.getText().toString());
        map.put("phone", et_phone.getText().toString());
        map.put("address", et_address.getText().toString());
        map.put("cityName", et_cityName.getText().toString());
        map.put("date", saveCurrentDate);
        map.put("time", saveCurrentTime);
        map.put("state", "not shipment");
        conRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("cart list").child("user view")
                            .child(Prevalent.onlineUser.getPhone()).child("products")
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(ConfirmFinalActivity.this, " confirmed successful ",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ConfirmFinalActivity.this,HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

    }


}