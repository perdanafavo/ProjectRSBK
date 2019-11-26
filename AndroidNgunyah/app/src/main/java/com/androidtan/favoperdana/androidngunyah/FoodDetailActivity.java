package com.androidtan.favoperdana.androidngunyah;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidtan.favoperdana.androidngunyah.Common.Common;
import com.androidtan.favoperdana.androidngunyah.Database.Database;
import com.androidtan.favoperdana.androidngunyah.Model.Food;
import com.androidtan.favoperdana.androidngunyah.Model.Order;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetailActivity extends AppCompatActivity {

    TextView food_name, food_price, food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId="";

    DatabaseReference food;

    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        setVariable();
        createView();
    }

    private void createView() {
        //Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        food = database.getReference("food");

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        Common.currentUser.getPhone(),
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));

                Toast.makeText(FoodDetailActivity.this,"Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get Food id from Intent
        if (getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty())
        {
            if(Common.isConnectedToInternet(getBaseContext()))
                getDetailFood(foodId);
            else
            {
                Toast.makeText(FoodDetailActivity.this, "please check your connection !!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setVariable() {
        //Init view
        btnCart = findViewById(R.id.btnCart);
        numberButton = findViewById(R.id.number_button);
        food_description = findViewById(R.id.food_description);
        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        food_image = findViewById(R.id.img_food);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
    }

    private void getDetailFood(String foodId) {
        food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                //Set image Picasso
                Picasso.get().load(currentFood.getImage()).into(food_image);
                //Set Food Detail
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
