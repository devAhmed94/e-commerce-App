package com.example.ecommerceapp;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.model.Prevalent;
import com.example.ecommerceapp.model.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    EditText et_name,et_phone,et_address;
    TextView tv_close,tv_update,tv_change;
    CircleImageView iv_imageProfile;
    Uri uri;
    String downloadUri ="";
    String checker="";
    StorageReference storageReference;
    Button btn_setSecurity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        storageReference = FirebaseStorage.getInstance().getReference().child("profile images");

        iv_imageProfile = findViewById(R.id.setting_iv_imageProfile);
        tv_close = findViewById(R.id.setting_close);
        tv_update =findViewById(R.id.setting_update);
        tv_change =findViewById(R.id.setting_tv_change);
        et_name =findViewById(R.id.setting_et_name);
        et_phone =findViewById(R.id.setting_et_phone);
        et_address =findViewById(R.id.setting_et_address);
        btn_setSecurity = findViewById(R.id.setting_btn_setSecurity);
        fechDataDisplay(iv_imageProfile,et_name,et_phone,et_address);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){

                    userInfoSave();
                }else {
                    updateUserInfo();
                }
            }
        });

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker ="clicked";
                CropImage.activity(uri)
                        .setAspectRatio(1, 1)
                        .start(SettingActivity.this);
            }
        });
        btn_setSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","setting");
                startActivity(intent);
            }
        });

    }

    private void updateUserInfo() {

        DatabaseReference dataref =FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap =new HashMap<>();
        userMap.put("name", et_name.getText().toString());
        userMap.put("address", et_address.getText().toString());
        userMap.put("phone", et_phone.getText().toString());
        dataref.child(Prevalent.onlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingActivity.this,HomeActivity.class));
        Toast.makeText(SettingActivity.this,"update successful",Toast.LENGTH_LONG).show();
        finish();

    }

    private void uploadImge() {
        ProgressDialog progressDialog = new ProgressDialog(SettingActivity.this);
        progressDialog.setTitle("uploading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("plz wait,we are upload image");
        progressDialog.show();
        if(uri !=null){
            StorageReference filpath = storageReference.child(Prevalent.onlineUser.getPhone());
            UploadTask uploadTask = filpath.putFile(uri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public  Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful()){

                        throw task.getException();
                    }
                    return filpath.getDownloadUrl();
                }
            })
             .addOnCompleteListener(new OnCompleteListener<Uri>() {
                 @Override
                 public void onComplete(@NonNull Task <Uri>task) {
                     if(task.isSuccessful()){
                         Uri taskResult = task.getResult();
                         downloadUri =taskResult.toString();

                         DatabaseReference dataref =FirebaseDatabase.getInstance().getReference().child("Users");
                         HashMap<String,Object> userMap =new HashMap<>();
                         userMap.put("name", et_name.getText().toString());
                         userMap.put("address", et_address.getText().toString());
                         userMap.put("phoneOrder", et_phone.getText().toString());
                         userMap.put("image", downloadUri);
                         dataref.child(Prevalent.onlineUser.getPhone()).updateChildren(userMap);

                         progressDialog.dismiss();
                         startActivity(new Intent(SettingActivity.this,HomeActivity.class));
                         Toast.makeText(SettingActivity.this,"update successful",Toast.LENGTH_LONG).show();
                         finish();

                     }else {
                            progressDialog.dismiss();
                            Toast.makeText(SettingActivity.this,"Error : ",Toast.LENGTH_LONG).show();
                     }

                 }
             });
        }else {
            Toast.makeText(SettingActivity.this,"image is not selected",Toast.LENGTH_LONG).show();
        }

    }

    private void userInfoSave() {
        if(TextUtils.isEmpty(et_name.getText())){
            Toast.makeText(SettingActivity.this, " name is empty", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(et_phone.getText())){
            Toast.makeText(SettingActivity.this, " phone is empty", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(et_address.getText())){
            Toast.makeText(SettingActivity.this, " address is empty", Toast.LENGTH_LONG).show();
        }else if (checker.equals("clicked")){
            uploadImge();
        }


    }


    private void fechDataDisplay(CircleImageView iv_imageProfile, EditText et_name, EditText et_phone, EditText et_address)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.onlineUser.getPhone());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("image").exists()){
                        String image = snapshot.child("image").getValue().toString();
                        String name =snapshot.child("name").getValue().toString();
                        String phone =snapshot.child("phone").getValue().toString();
                        String address =snapshot.child("address").getValue().toString();

                        Picasso.with(SettingActivity.this).load(image).into(iv_imageProfile);
                        et_name.setText(name);
                        et_phone.setText(phone);
                        et_address.setText(address);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode ==RESULT_OK && data !=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            uri = result.getUri();
            iv_imageProfile.setImageURI(uri);
        }else {
            Toast.makeText(SettingActivity.this, "Error try again", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SettingActivity.this,SettingActivity.class));
            finish();
        }
    }
}