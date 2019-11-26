package com.androidtan.favoperdana.androidngunyah.ViewHolder;


import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidtan.favoperdana.androidngunyah.CartActivity;
import com.androidtan.favoperdana.androidngunyah.Common.Common;
import com.androidtan.favoperdana.androidngunyah.Database.Database;
import com.androidtan.favoperdana.androidngunyah.Interface.ItemClickListener;
import com.androidtan.favoperdana.androidngunyah.Model.Order;
import com.androidtan.favoperdana.androidngunyah.R;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

    public TextView txt_cart_name, txt_price;
    public ElegantNumberButton btn_quantity;

    private ItemClickListener itemClickListener;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name = itemView.findViewById(R.id.cart_item_name);
        txt_price = itemView.findViewById(R.id.cart_item_price);
        btn_quantity = itemView.findViewById(R.id.btn_quantity);

        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select action");
        menu.add(0,0,getAdapterPosition(),Common.DELETE);
    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    public List<Order> listData;
    public CartActivity cart;

    public CartAdapter(List<Order> listData, CartActivity cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout, viewGroup,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, final int position) {
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(""+listData.get(position).getQuantity(), Color.RED);
//        cartViewHolder.img_cart_count.setImageDrawable(drawable);

        cartViewHolder.btn_quantity.setNumber(listData.get(position).getQuantity());
        cartViewHolder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCart(order);

                //Update txttotal
                //Total Price
                int total = 0;
                List<Order> orders = new Database(cart).getCarts(Common.currentUser.getPhone());
                for(Order item:orders)
                    total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(item.getQuantity()));
                Locale locale = new Locale("en","US");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                cart.txtTotalPrice.setText(fmt.format(total));

            }
        });

        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        cartViewHolder.txt_price.setText(fmt.format(price));

        cartViewHolder.txt_cart_name.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
