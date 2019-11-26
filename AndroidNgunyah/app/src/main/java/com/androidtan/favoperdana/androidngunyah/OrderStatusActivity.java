package com.androidtan.favoperdana.androidngunyah;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidtan.favoperdana.androidngunyah.Common.Common;
import com.androidtan.favoperdana.androidngunyah.Model.Request;
import com.androidtan.favoperdana.androidngunyah.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatusActivity extends AppCompatActivity {

    RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    FirebaseRecyclerOptions<Request> options;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        createView();
    }

    private void createView() {
        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("request");

        recyclerview = findViewById(R.id.listOrders);
        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager((layoutManager));

        if(Common.isConnectedToInternet(getBaseContext()))
            loadOrders(Common.currentUser.getPhone());
        else
        {
            Toast.makeText(OrderStatusActivity.this, "please check your connection !!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadOrders(String phone) {
        options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(requests.orderByChild("phone").equalTo(phone),Request.class).build();
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options){

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_layout, viewGroup, false);
                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, final int position, @NonNull Request model) {
                holder.txtOrderId.setText(adapter.getRef(position).getKey());
                holder.txtOrderStatus.setText(convertCodeStatus(model.getStatus()));
                holder.txtOrderAddress.setText(model.getAddress());
                holder.txtOrderPhone.setText(model.getPhone());
                holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (adapter.getItem(position).getStatus().equals("0"))
                            deleteOrder(adapter.getRef(position).getKey());
                        else 
                            Toast.makeText(OrderStatusActivity.this,"You cannot delete this order!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        adapter.startListening();
        recyclerview.setAdapter(adapter);
    }

    private void deleteOrder(final String key) {
        requests.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(OrderStatusActivity.this, "Order " + key + " has been deleted",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrderStatusActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertCodeStatus(String status) {
        if (status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "On my way";
        else
            return "Shipped";
    }
}
