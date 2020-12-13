package com.example.ecommerceapp.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.admin.AdminMaintainActivity;
import com.example.ecommerceapp.model.Prevalent;
import com.example.ecommerceapp.model.Products;
import com.example.ecommerceapp.model.RecycleViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView iv_logout;
    CircleImageView circle_iv_imageProfile;
    TextView tv_nav_userName;
    LinearLayout setting_linear;
    LinearLayout cart_linear;
    LinearLayout search_linear;
    FloatingActionButton fBtn_goToCartList;
    DatabaseReference databaseReference;
    private RecyclerView productRecycle;
    private String admin ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent in = getIntent();
        Bundle bundle =in.getExtras();
        if(bundle !=null){
            admin =getIntent().getStringExtra("admin");
        }

        Paper.init(HomeActivity.this);
        setting_linear =findViewById(R.id.linear_setting);
        cart_linear =findViewById(R.id.cart_linear);
        search_linear =findViewById(R.id.linear_Search);
        fBtn_goToCartList =findViewById(R.id.home_f_btn);
        circle_iv_imageProfile =findViewById(R.id.profile_image);
        productRecycle =findViewById(R.id.home_recycleView);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");
        drawerLayout = findViewById(R.id.drawerLayout);
        iv_logout = findViewById(R.id.iv_logout);
        tv_nav_userName =findViewById(R.id.nav_userName);
        if(!admin.equals("admin")){
            tv_nav_userName.setText(Prevalent.onlineUser.getName());
            Picasso.with(HomeActivity.this).load(Prevalent.onlineUser.getImage()).placeholder(R.drawable.profile).into(circle_iv_imageProfile);
        }

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!admin.equals("admin")) {
                    Paper.book().destroy();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
        setting_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!admin.equals("admin")) {
                    Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                    startActivity(intent);
                }
            }
        });
        cart_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!admin.equals("admin")) {
                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
                }
            }
        });
        search_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!admin.equals("admin")) {
                    startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                }
            }
        });
        fBtn_goToCartList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!admin.equals("admin")){
                    startActivity(new Intent(HomeActivity.this,CartActivity.class));
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(databaseReference.orderByChild("productState").equalTo("approved"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products,RecycleViewHolder> adapter = new FirebaseRecyclerAdapter<Products,RecycleViewHolder> (options) {
            @NonNull
            @Override
            public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new RecycleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull RecycleViewHolder holder, int position, @NonNull Products model) {
                holder.tv_productName.setText(model.getName());
                holder.tv_productPrice.setText(model.getPrice());
                holder.tv_productDes.setText(model.getDescription());
                Picasso.with(getApplicationContext()).load(model.getImage()).into(holder.iv_productImage);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(admin.equals("")){

                            Intent intent = new Intent(HomeActivity.this, DetailsActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);

                        }
                        else if (admin.equals("admin")){
                            Intent intent = new Intent(HomeActivity.this, AdminMaintainActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);

                        }

                    }
                });
            }
        };
        productRecycle.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        productRecycle.setHasFixedSize(true);
        productRecycle.setAdapter(adapter);
        adapter.startListening();
    }

    public void onMenuClick(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}