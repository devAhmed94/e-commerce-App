package com.example.ecommerceapp.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.model.Cart;
import com.example.ecommerceapp.model.CartViewHolder;
import com.example.ecommerceapp.model.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartListRecycle;
    Button btn_next;
    TextView tv_totalPrice,msg;
    int totalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartListRecycle = findViewById(R.id.cart_recycle_list);
        btn_next =findViewById(R.id.cart_btn_next);
        tv_totalPrice =findViewById(R.id.cart_tv_totalPrice);
        msg =findViewById(R.id.cart_msg);
        cartListRecycle.setHasFixedSize(true);
        cartListRecycle.setLayoutManager(new LinearLayoutManager(CartActivity.this));

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ConfirmFinalActivity.class);
                intent.putExtra("total price", String.valueOf(totalPrice));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        stateOrdersMethod();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("cart list");
        FirebaseRecyclerOptions<Cart> options =new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(reference.child("user view").child(Prevalent.onlineUser.getPhone()).child("products"),Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_list_layout,parent,false));
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.tv_name.setText(model.getName());
                holder.tv_quantity.setText("the quantity is " + model.getQuantity());
                holder.tv_price.setText(" the price is "+model.getPrice());
                int sumOfOneModel =( Integer.valueOf(model.getPrice()) ) * (Integer.valueOf(model.getQuantity())) ;
                totalPrice +=sumOfOneModel;
                tv_totalPrice.setText(" total price is "+String.valueOf(totalPrice));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence [] items =new CharSequence[]{
                          "Edited","Remove"
                        };
                        AlertDialog.Builder builder =new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart options");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    Intent intent =new Intent(CartActivity.this, DetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if(which==1){
                                    reference.child("user view").child(Prevalent.onlineUser.getPhone()).child("products")
                                            .child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(CartActivity.this,"remove successful",Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(CartActivity.this, HomeActivity.class));
                                            }
                                        }
                                    });

                                }
                            }
                        });
                        builder.show();

                    }
                });
            }
        };

        cartListRecycle.setAdapter(adapter);
        adapter.startListening();
    }

   public void stateOrdersMethod()
   {
       DatabaseReference stateRef = FirebaseDatabase.getInstance().getReference()
               .child("orders").child(Prevalent.onlineUser.getPhone());

       stateRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot)
           {
               if(snapshot.exists()){
                   String shippedState = snapshot.child("state").getValue().toString();
                   String userName = snapshot.child("name").getValue().toString();

                   if(shippedState.equals("shipped"))
                   {
                       tv_totalPrice.setText("Dear " + userName+" \n Congratulation , the shipment is successfully ");
                       cartListRecycle.setVisibility(View.GONE);
                       msg.setVisibility(View.VISIBLE);
                       msg.setText("Congratulations, your final order has been Shipped successfully." +
                               " Soon you will received your order at your door step.");
                       btn_next.setVisibility(View.GONE);
                       Toast.makeText(CartActivity.this," you can purchase more products, once you received your first final order.",Toast.LENGTH_LONG).show();
                   }
                   else if(shippedState.equals("not shipped"))
                   {
                       tv_totalPrice.setText("state ship = not shipment");
                       cartListRecycle.setVisibility(View.GONE);
                       msg.setVisibility(View.VISIBLE);
                       btn_next.setVisibility(View.GONE);
                       Toast.makeText(CartActivity.this," you can purchase more products, once you received your first final order.",Toast.LENGTH_LONG).show();

                   }

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

    }

}