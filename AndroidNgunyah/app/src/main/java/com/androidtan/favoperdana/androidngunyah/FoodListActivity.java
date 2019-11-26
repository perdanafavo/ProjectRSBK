package com.androidtan.favoperdana.androidngunyah;

import android.content.Intent;
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
import com.androidtan.favoperdana.androidngunyah.Interface.ItemClickListener;
import com.androidtan.favoperdana.androidngunyah.Model.Food;
import com.androidtan.favoperdana.androidngunyah.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryId = "";

    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    FirebaseRecyclerOptions<Food> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        setVariable();
        createView();
    }

    private void createView() {
        //Init Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("food");

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null)
        {
            if(Common.isConnectedToInternet(getBaseContext()))
                loadListFood(categoryId);
            else
            {
                Toast.makeText(FoodListActivity.this, "please check your connection !!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setVariable() {
        recyclerView = findViewById(R.id.recycler_food);
    }

    private void loadListFood(String categoryId) {
        options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodList.orderByChild("menuId").equalTo(categoryId),Food.class).build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.food_name.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.food_image);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Intent food detail
                        Intent foodDetail = new Intent(FoodListActivity.this, FoodDetailActivity.class);
                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey()); //Send food id to new activity
                        startActivity(foodDetail);
                    }
                });
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_item, viewGroup, false);
                return new FoodViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
